package br.com.leonardoferreira

import java.net.HttpURLConnection
import java.net.URL

class HttpRequest(
    private val requestMethod: HttpMethod,
    private val requestUrl: String,
    request: String
) {

    val body: ByteArray
    val headers: Map<String, List<String>>

    init {
        if (request.isEmpty()) {
            headers = emptyMap()
            body = ByteArray(0)
        } else {
            val (
                header,
                body
            ) = request.split(Regex("(?:\\h*\\n){2,}"), 2)

            this.body = body.byteInputStream().readBytes()

            headers = header.lines()
                .filter(String::isNotBlank)
                .map {
                    val (name, value) = it.split(":", limit = 2)
                    Pair(name, value)
                }
                .groupBy { it.first }
                .mapValues { it.value.map { header -> header.second } }
        }
    }

    val connection: HttpURLConnection
        get() = URL(requestUrl).openConnection() as HttpURLConnection

    val method: String
        get() = requestMethod.name

    val hasBody: Boolean
        get() = body.isNotEmpty()

}

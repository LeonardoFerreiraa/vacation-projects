package br.com.leonardoferreira

import tornadofx.*
import java.io.BufferedReader

class HttpRequestController : Controller() {

    fun request(httpRequest: HttpRequest) {
        val connection = httpRequest.connection
        connection.requestMethod = httpRequest.method
        connection.headers = httpRequest.headers
        if (httpRequest.hasBody) {
            connection.doOutput = true

            connection.outputStream.use { os ->
                os.write(httpRequest.body)
            }
        }

        connection.connect()

        val body = connection.inputStream.reader()
            .let(::BufferedReader)
            .use { it.readText() }

        val headers = connection.headerFields
            .filter { it.key != null }
            .map { header ->
                header.value.map { value ->
                    "${header.key}: $value"
                }
            }
            .flatten()

        val httpResponse = HttpResponse(
            headers,
            connection.getHeaderField(0),
            body
        )

        fire(HttpResponseEvent(httpResponse))

        connection.disconnect()
    }

}

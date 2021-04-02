package br.com.leonardoferreira

import java.net.HttpURLConnection

var HttpURLConnection.headers: Map<String, List<String>>
    get() = requestProperties
    set(headers) {
        headers.forEach { header ->
            header.value.forEach { value ->
                this.addRequestProperty(header.key, value)
            }
        }
    }

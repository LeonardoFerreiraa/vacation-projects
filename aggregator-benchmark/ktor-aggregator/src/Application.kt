package br.com.leonardoferreira

import AggregatedResponse
import AnythingRequest
import AnythingResponse
import TodoResponse
import UuidResponse
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.EndpointConfig
import io.ktor.client.engine.cio.endpoint
import io.ktor.client.features.json.GsonSerializer
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import io.ktor.response.respond
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.util.KtorExperimentalAPI
import kotlinx.coroutines.async

fun main(args: Array<String>): Unit = io.ktor.server.cio.EngineMain.main(args)

@KtorExperimentalAPI
@Suppress("unused")
@kotlin.jvm.JvmOverloads
fun Application.module(testing: Boolean = false) {
    install(ContentNegotiation) {
        gson()
    }

    routing {
        get("/aggregations") {
            val httpClient = HttpClient(CIO) {
                install(JsonFeature) {
                    serializer = GsonSerializer()
                }
            }

            try {
                val todoDeferred = async {
                    val anythingResponse = httpClient.post<AnythingResponse>("https://httpbin.org/anything") {
                        contentType(ContentType.Application.Json)
                        body = AnythingRequest(200)
                    }

                    httpClient.get<TodoResponse>("https://jsonplaceholder.typicode.com/todos/${anythingResponse.json.number}")
                }

                val uuidDeferred = async {
                    httpClient.get<UuidResponse>("https://httpbin.org/uuid")
                }

                call.respond(
                    AggregatedResponse(
                        todoTitle = todoDeferred.await().title,
                        uuid = uuidDeferred.await().uuid
                    )
                )
            } catch (e: Exception) {
                call.respond(HttpStatusCode.InternalServerError)
            } finally {
                httpClient.close()
            }
        }
    }
}


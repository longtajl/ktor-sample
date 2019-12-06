package sample.lib

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.features.json.*
import io.ktor.client.request.get
import io.ktor.client.request.post
import io.ktor.client.request.url
import io.ktor.http.ContentType
import io.ktor.http.contentType

class APIClient {

    companion object {

        private val client = HttpClient(CIO) {
            engine {
                maxConnectionsCount = 1000
            }
            install(JsonFeature) {
                serializer = GsonSerializer()
            }
        }

        private suspend inline fun <reified T> post(url: String): T {
            return client.post<T> {
                url(url)
                contentType(ContentType.Application.Json)
            }
        }

        private suspend inline fun <reified T> get(url: String): T {
            return client.get(url) {
                contentType(ContentType.Application.Json)
            }
        }

        public suspend fun getPinpoint(): Response {
            return get("http://weather.livedoor.com/forecast/webservice/json/v1?city=130010")
        }
    }

    }

data class Response(val pinpointLocations: List<PinpointLocation>)
data class PinpointLocation(val link: String, val name: String)
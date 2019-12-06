package sample

import io.ktor.application.*
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.html.*
import io.ktor.http.ContentType
import io.ktor.http.content.*
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import kotlinx.css.*
import sample.lib.APIClient
import sample.template.MultiColumnTemplate

actual class Sample {
    actual fun checkMe() = 42
}

actual object Platform {
    actual val name: String = "JVM"
}

suspend inline fun ApplicationCall.respondCss(builder: CSSBuilder.() -> Unit) {
    this.respondText(CSSBuilder().apply(builder).toString(), ContentType.Text.CSS)
}

fun main(args: Array<String>) {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        install(ContentNegotiation) {
            gson { }
        }
        routing {

            static("/static") {
                resource("sample.js")
            }

            get("/styles.css") {
            }

            get("/") {
                val list = (1..2000).map { "test$it" }
                call.respondHtmlTemplate(MultiColumnTemplate()) {
                    column2 {
                        list.forEach {
                            + it
                        }
                    }
                }
            }

            get("/pp") {
                val a = APIClient.getPinpoint()
                call.respondHtmlTemplate(MultiColumnTemplate()) {
                    column1 {

                    }
                    column2 {
                        a.pinpointLocations.forEach { + "${it.name} ${it.link}" }
                    }
                }
            }

            get("/p") {
                call.respond(APIClient.getPinpoint())
            }

            get("/styles.css") {
                call.respondCss {
                    body {
                        marginRight = LinearDimension.auto
                        marginLeft = LinearDimension.auto
                        width = LinearDimension("900px")
                    }
                    h1 {
                        color = Color.blue
                    }
                }
            }
        }
    }.start(wait = true)
}
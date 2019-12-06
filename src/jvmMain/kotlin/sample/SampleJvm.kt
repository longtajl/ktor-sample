package sample

import io.ktor.application.ApplicationCall
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.html.respondHtmlTemplate
import io.ktor.http.ContentType
import io.ktor.http.content.resource
import io.ktor.http.content.static
import io.ktor.response.respond
import io.ktor.response.respondText
import io.ktor.routing.get
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
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
                val list = (1..4).map { "test$it" }
                call.respondHtmlTemplate(MultiColumnTemplate()) {
                    column1 {
                        list.forEach {
                            +it
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
                        a.pinpointLocations.forEach { +"${it.name} ${it.link}" }
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
                        width = LinearDimension("640px")
                    }
                    rule(".side") {
                        backgroundColor = Color("#fcc")
                        width = LinearDimension("180px")
                        flexGrow = 1.0
                    }
                    rule(".main") {
                        backgroundColor = Color("#fea")
                        flexGrow = 5.0
                    }
                    rule(".flexBox") {
                        backgroundColor = Color.blue
                        display = Display.flex
                    }
                    h1 {
                        color = Color.blue
                    }
                }
            }
        }
    }.start(wait = true)
}
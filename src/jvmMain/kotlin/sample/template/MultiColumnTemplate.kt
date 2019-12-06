package sample.template

import io.ktor.html.*
import io.ktor.http.ContentType
import kotlinx.html.*
import sample.hello

class MultiColumnTemplate(val main: MainTemplate = MainTemplate()) : Template<HTML> {
    val column1 = Placeholder<FlowContent>()
    val column2 = Placeholder<FlowContent>()
    override fun HTML.apply() {
        insert(main) {
            sideMenu {
                item { +"メニュー1" }
                item { +"メニュー2" }
                item { +"メニュー3" }
            }
            content {
                div("column") {
                    insert(column1)
                }
                div("column") {
                    insert(column2)
                }
            }
        }
    }
}

class MainTemplate : Template<HTML> {
    val content = Placeholder<HtmlBlockTag>()
    val sideMenu = TemplatePlaceholder<MenuTemplate>()
    override fun HTML.apply() {
        head {
            link { href = "https://unpkg.com/tailwindcss@^1.0/dist/tailwind.min.css" }
            link(rel = "stylesheet", href = "/styles.css", type = "text/css")
            script(src = "/static/sample.js") { }
            title { +"Template" }
        }
        body {
            header(classes = "header") {
                h1 {
                    +hello()
                }
            }
            div(classes = "flexBox") {
                section(classes = "main") {
                    insert(content)
                }
                section(classes = "side") {
                    insert(MenuTemplate(), sideMenu)
                }
            }
        }
    }
}

class MenuTemplate : Template<FlowContent> {
    val item = PlaceholderList<UL, FlowContent>()
    override fun FlowContent.apply() {
        if (!item.isEmpty()) {
            ul {
                each(item) {
                    li {
                        if (it.first) b {
                            insert(it)
                        } else {
                            insert(it)
                        }
                    }
                }
            }
        }
    }
}

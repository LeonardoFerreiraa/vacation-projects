package br.com.leonardoferreira

import javafx.scene.control.ComboBox
import javafx.scene.control.Label
import javafx.scene.control.TextArea
import javafx.scene.control.TextField
import javafx.scene.layout.Priority
import tornadofx.*

class MainView : View("Getman") {

    private var responseStatusLabel: Label by singleAssign()

    private var responseText: TextArea by singleAssign()

    private var requestMethodTextField: ComboBox<HttpMethod> by singleAssign()

    private var requestUrlTextField: TextField by singleAssign()

    private var requestText: TextArea by singleAssign()

    private val httpRequestController: HttpRequestController by inject()

    override val root = vbox {
        vbox {
            hbox {
                requestMethodTextField = combobox(values = HttpMethod.values().toList()) {
                    value = HttpMethod.GET
                }
                requestUrlTextField = textfield {
                    hgrow = Priority.ALWAYS
                }
            }
            buttonbar {
                button("Enviar") {
                    action {
                        httpRequestController.request(
                            HttpRequest(
                                requestMethodTextField.value,
                                requestUrlTextField.text,
                                requestText.text
                            )
                        )
                    }
                }
            }
        }

        hbox {
            vgrow = Priority.ALWAYS
            vbox {
                hgrow = Priority.ALWAYS
                label("REQUEST")
                requestText = textarea() {
                    hgrow = Priority.ALWAYS
                    vgrow = Priority.ALWAYS
                }
            }

            vbox {
                hgrow = Priority.ALWAYS
                responseStatusLabel = label("RESPONSE")
                responseText = textarea("") {
                    isEditable = false
                    hgrow = Priority.ALWAYS
                    vgrow = Priority.ALWAYS
                }

                subscribe<HttpResponseEvent> { event ->
                    val httpResponse = event.httpResponse

                    responseStatusLabel.text = "RESPONSE: ${httpResponse.response}"
                    responseText.text = """${httpResponse.headers.joinToString(separator = "\n")}

                            |${httpResponse.body}
                        """.trimMargin()
                }
            }

        }
    }

}

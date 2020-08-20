package br.com.leonardoferreira

import tornadofx.*
import java.util.Locale

class Application : App(MainView::class) {
    init {
        FX.locale = Locale("pt", "BR")
    }
}

fun main(args: Array<String>) {
    launch<Application>(args)
}

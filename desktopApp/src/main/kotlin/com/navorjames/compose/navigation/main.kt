package com.navorjames.compose.navigation

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ComposeNavigation",
    ) {
        App()
    }
}
package de.visualdigits.common

import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import co.touchlab.kermit.Logger
import de.visualdigits.common.domain.model.platform.PlatformType
import de.visualdigits.common.domain.service.getPlatformLogWriters
import kotlinx.coroutines.cancel

fun main() {

    val writers = getPlatformLogWriters()
    Logger.setLogWriters(writers)
    Logger.setTag("StephansComponents")

    application {
        val ioScope = rememberCoroutineScope()
        val state = rememberWindowState(
            width = 1200.dp,
            height = 900.dp,
            position = WindowPosition(Alignment.Center)
        )

        Window(
            onCloseRequest = {
                ioScope.cancel("Normal Exit")
                exitApplication()
            },
            title = "Stephans Components",
            state = state
        ) {
            App(PlatformType.jvm)
        }
    }
}

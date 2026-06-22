package de.visualdigits.common.demo

import androidx.compose.ui.graphics.Color
import co.touchlab.kermit.Severity
import de.visualdigits.common.demo.form.DC
import de.visualdigits.common.demo.form.DemoConfiguration
import de.visualdigits.common.demo.form.Language
import de.visualdigits.common.domain.model.ui.UiText

data class DemoState(
    val uiMessage: UiText? = null,
    val uiMessageSeverity: Severity? = null,
    val editedConfiguration: DemoConfiguration? = null,
    val configuration: DemoConfiguration = DemoConfiguration(mapOf(
        DC.text to "",
        DC.language to Language.EN,
        DC.switch to false,
        DC.color to Color.White
    )),
    val switchState: Boolean = false
)

package de.visualdigits.common.demo

import co.touchlab.kermit.Severity
import de.visualdigits.common.demo.form.DemoConfiguration
import de.visualdigits.common.domain.model.UiText

data class DemoState(
    val uiMessage: UiText? = null,
    val uiMessageSeverity: Severity? = null,
    val configuration: DemoConfiguration = DemoConfiguration(),
    val switchState: Boolean = false
)

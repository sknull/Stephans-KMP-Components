package de.visualdigits.common.demo.form

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Severity
import de.visualdigits.common.demo.AppCompositionProvider
import de.visualdigits.common.demo.DemoState
import de.visualdigits.common.domain.model.platform.PlatformType
import de.visualdigits.common.domain.model.ui.UiText
import de.visualdigits.common.presentation.components.form.ConfigurationEditForm
import de.visualdigits.common.presentation.model.ScrollIntent

@Composable
fun FormDemo(
    state: DemoState,
    platformType: PlatformType,
    setState: (DemoState) -> Unit
) {
    val scrollPosition= mutableMapOf<String, Triple<Int, Int?, ScrollIntent>>()

    AppCompositionProvider {
        Box(
            modifier = Modifier
                .fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            ConfigurationEditForm(
                platformType = platformType,
                configuration = state.editedConfiguration!!,
                scrollbarModifier = Modifier
                    .clip(MaterialTheme.shapes.small)
                    .width(10.dp)
                    .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
                scrollPosition = scrollPosition,
                scrollbarId = "configuration_settings",
                onValueChange = { keyValue ->
                    setState(
                        state.copy(
                            editedConfiguration = state.editedConfiguration.copy(
                                key = keyValue.descriptor.key as DC,
                                value = keyValue.value
                            )
                        )
                    )
                },
                onCancelClick = {
                    setState(
                        state.copy(
                            uiMessage = UiText.DynamicString("Cancel clicked"),
                            uiMessageSeverity = Severity.Info
                        )
                    )
                },
                onOkClick = {
                    setState(
                        state.copy(
                            uiMessage = UiText.DynamicString("Ok clicked"),
                            uiMessageSeverity = Severity.Info,
                            configuration = state.editedConfiguration
                        )
                    )
                },
                onCommonAction = { action ->
                }
            )
        }
    }
}

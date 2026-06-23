package de.visualdigits.common.demo.form

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Severity
import de.visualdigits.common.demo.DemoState
import de.visualdigits.common.domain.model.form.EditableListResources
import de.visualdigits.common.domain.model.ui.UiText
import de.visualdigits.common.presentation.components.form.ConfigurationEditForm
import de.visualdigits.common.presentation.model.PlatformScrollbarStyle
import de.visualdigits.common.presentation.model.ScrollIntent
import org.jetbrains.compose.resources.painterResource
import stephans_kmp_components.composeapp.generated.resources.Res
import stephans_kmp_components.composeapp.generated.resources.icon_add_24px
import stephans_kmp_components.composeapp.generated.resources.icon_cancel_24px
import stephans_kmp_components.composeapp.generated.resources.icon_check_small_24px
import stephans_kmp_components.composeapp.generated.resources.icon_delete_24px
import stephans_kmp_components.composeapp.generated.resources.icon_edit_24px
import stephans_kmp_components.composeapp.generated.resources.icon_folder_open_24px

@Composable
fun FormDemo(
    state: DemoState,
    setState: (DemoState) -> Unit
) {
    val scrollPosition= mutableMapOf<String, Triple<Int, Int?, ScrollIntent>>()

    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        ConfigurationEditForm(
            configuration = state.editedConfiguration!!,
            scrollbarModifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .width(10.dp)
                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
            titleChooseDirectory = UiText.DynamicString("Choose Directory"),
            titleChooseFile = UiText.DynamicString("Choose File"),
            iconFolder = painterResource(Res.drawable.icon_folder_open_24px),
            editableListResources = EditableListResources(
                tooltipAdd = UiText.DynamicString("Add..."),
                titleAdd = UiText.DynamicString("Add"),
                iconAdd = Res.drawable.icon_add_24px,
                titleEdit = UiText.DynamicString("Edit"),
                iconEdit = Res.drawable.icon_edit_24px,
                toolTipDelete = UiText.DynamicString("Delete"),
                iconDelete = Res.drawable.icon_delete_24px,
                toolTipEdit = UiText.DynamicString("Edit"),
                labelOk = UiText.DynamicString("Ok"),
                iconOk = Res.drawable.icon_check_small_24px,
                labelCancel = UiText.DynamicString("Cancel"),
                iconCancel = Res.drawable.icon_cancel_24px
            ),
            tooltipOk = UiText.DynamicString(""),
            iconOk = painterResource(Res.drawable.icon_check_small_24px),
            tooltipCancel = UiText.DynamicString(""),
            iconCancel = painterResource(Res.drawable.icon_cancel_24px),
            scrollPosition = scrollPosition,
            scrollbarId = "configuration_settings",
            scrollbarStyle = PlatformScrollbarStyle(
                minimalHeight = 16.dp,
                thickness = 8.dp,
                shape = RoundedCornerShape(4.dp),
                hoverDurationMillis = 300,
                unhoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
                hoverColor = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
            ),
            fieldHeight = 50.dp,
            onValueChange = { keyValue ->
                setState(state.copy(
                    editedConfiguration = state.editedConfiguration.copy(
                        key = keyValue.descriptor.key as DC,
                        value = keyValue.value
                    )
                ))
            },
            onCancelClick = {
                setState(state.copy(
                    uiMessage = UiText.DynamicString("Cancel clicked"),
                    uiMessageSeverity = Severity.Info
                ))
            },
            onOkClick = {
                setState(state.copy(
                    uiMessage = UiText.DynamicString("Ok clicked"),
                    uiMessageSeverity = Severity.Info,
                    configuration = state.editedConfiguration
                ))
            },
            onCommonAction = { action ->

            }
        )
    }
}

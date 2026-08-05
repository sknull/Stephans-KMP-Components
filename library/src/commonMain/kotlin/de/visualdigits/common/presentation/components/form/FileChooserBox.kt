package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.configuration.FileFieldDescriptor
import de.visualdigits.common.domain.model.form.LocalFileChooserResources
import de.visualdigits.common.domain.model.form.LocalFormFieldResources
import de.visualdigits.common.domain.model.ui.FileMode
import de.visualdigits.common.presentation.components.PlatformFileChooser
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight
import de.visualdigits.common.presentation.components.util.outlinedTextFieldColors
import kotlinx.io.files.Path

@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> FileChooserBox(
    modifier: Modifier,
    fieldState: FieldState<K, FK>,
    unfocusedBorderColor: Color,
    startDirectory: Path,
    onValueChange: (String) -> Unit,
    onOk: (Path) -> Unit
) {
    val formFieldResources = LocalFormFieldResources.current
    val fileChooserResources = LocalFileChooserResources.current

    val halfHeight = minimizedLabelHalfHeight(formFieldResources.textStyle)
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .height(formFieldResources.fieldHeight + halfHeight),
        textStyle = formFieldResources.textStyle,
        enabled = fieldState.fieldDescriptor.enabled && fieldState.fieldDescriptor.enabledCondition(fieldState.configuration, null),
        value = fieldState.currentValue?.toString() ?: "",
        label = {
            Text(
                text = fieldState.fieldDescriptor.label.asString(),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        leadingIcon = fileChooserResources.leadingIcon,
        trailingIcon = {
            fileChooserResources.trailingIcon?.let { ti -> ti() }

            if (fieldState.fieldDescriptor.enabled && fieldState.fieldDescriptor.enabledCondition(fieldState.configuration, null)) {
                PlatformFileChooser(
                    buttonTextStyle = MaterialTheme.typography.bodySmall,
                    buttonTextAlign = TextAlign.Start,
                    buttonWidth = 30.dp,
                    buttonHeight = 30.dp,
                    title = when ((fieldState.fieldDescriptor as FileFieldDescriptor<*,*>).fileMode) {
                        FileMode.DIRECTORIES_ONLY -> fileChooserResources.titleDirectories
                        FileMode.FILES_ONLY -> fileChooserResources.titleFiles
                    },
                    fileMode = FileMode.FILES_ONLY,
                    buttonColor = Color.Transparent,
                    leadingIcon = fileChooserResources.iconFolder,
                    leadingIconTint = fileChooserResources.iconTint,
                    startDirectory = startDirectory,
                    onOkPath = onOk
                )
            }
        },
        shape = formFieldResources.shape,
        onValueChange = onValueChange,
        singleLine = true,
        colors = outlinedTextFieldColors(formFieldResources.focusedBorderColor, unfocusedBorderColor)
    )
}

package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.configuration.FileFieldDescriptor
import de.visualdigits.common.domain.model.ui.FileMode
import de.visualdigits.common.presentation.components.PlatformFileChooser
import de.visualdigits.common.presentation.components.PlatformToolTip
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight
import de.visualdigits.common.presentation.components.util.outlinedTextFieldColors
import kotlinx.io.files.Path

@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> FileChooserBox(
    modifier: Modifier,
    fieldState: FieldState<K, FK>,
    iconFolder: Painter,
    space: Dp,
    toolTipBackgroundColor: Color,
    toolTipShape: Shape,
    fieldHeight: Dp,
    textStyle: TextStyle,
    leadingIcon: @Composable (() -> Unit)?,
    trailingIcon: @Composable (() -> Unit)?,
    iconTint: Color,
    buttonShape: Shape,
    buttonColor: Color,
    titleDirectories: String,
    titleFiles: String,
    startDirectory: Path,
    finalUnfocusedBorderColor: Color,
    focusedBorderColor: Color,
    onValueChange: (String) -> Unit,
    onOk: (Path) -> Unit
) {
    val halfHeight = minimizedLabelHalfHeight(textStyle)
    PlatformToolTip(
        text = fieldState.fieldDescriptor.toolTip?.asString(),
        space = space,
        backgroundColor = toolTipBackgroundColor,
        shape = toolTipShape,
        content = {
            OutlinedTextField(
                modifier = modifier
                    .fillMaxWidth()
                    .height(fieldHeight + halfHeight),
                textStyle = textStyle,
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
                leadingIcon = leadingIcon,
                trailingIcon = {
                    trailingIcon?.let { ti -> ti() }

                    if (fieldState.fieldDescriptor.enabled && fieldState.fieldDescriptor.enabledCondition(fieldState.configuration, null)) {
                        PlatformFileChooser(
                            buttonTextStyle = MaterialTheme.typography.bodySmall,
                            buttonTextAlign = TextAlign.Start,
                            title = when ((fieldState.fieldDescriptor as FileFieldDescriptor<*,*>).fileMode) {
                                FileMode.DIRECTORIES_ONLY -> titleDirectories
                                FileMode.FILES_ONLY -> titleFiles
                            },
                            fileMode = FileMode.FILES_ONLY,
                            buttonColor = buttonColor,
                            leadingIcon = iconFolder,
                            leadingIconTint = iconTint,
                            startDirectory = startDirectory,
                            onOkPath = onOk
                        )
                    }
                },
                shape = buttonShape,
                onValueChange = onValueChange,
                singleLine = true,
                colors = outlinedTextFieldColors(focusedBorderColor, finalUnfocusedBorderColor)
            )
        }
    )
}

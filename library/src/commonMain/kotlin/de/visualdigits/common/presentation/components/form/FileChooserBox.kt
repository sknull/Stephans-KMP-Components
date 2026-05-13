package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.FileMode
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.configuration.FileFieldDescriptor
import de.visualdigits.common.presentation.components.PlatformToolTip
import de.visualdigits.common.presentation.components.button.IndicatorButton
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File

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
    startDirectory: File,
    finalUnfocusedBorderColor: Color,
    focusedBorderColor: Color,
    onValueChange: (String) -> Unit,
    onOk: (File) -> Unit
) {
    val scope = rememberCoroutineScope()

    val halfHeight = minimizedLabelHalfHeight(textStyle)
    PlatformToolTip(
        text = fieldState.fieldDescriptor.toolTip?.asString(),
        space = space,
        backgroundColor = toolTipBackgroundColor,
        shape = toolTipShape
    ) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .height(fieldHeight + halfHeight),
            textStyle = textStyle,
            enabled = fieldState.fieldDescriptor.enabled,
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

                if (fieldState.fieldDescriptor.enabled) {
                    IndicatorButton(
                        modifier = Modifier
                            .padding(start = 5.dp),
                        width = 30.dp,
                        height = 30.dp,
                        leadingIcon = iconFolder,
                        leadingIconTint = iconTint,
                        shape = buttonShape,
                        buttonColor = buttonColor,
                        onClick = {
                            scope.launch(Dispatchers.IO) {
                                desktopFileChooser(
                                    fieldState = fieldState,
                                    title = when ((fieldState.fieldDescriptor as FileFieldDescriptor<*,*>).fileMode) {
                                        FileMode.DIRECTORIES_ONLY -> titleDirectories
                                        FileMode.FILES_ONLY -> titleFiles
                                    },
                                    fileMode = fieldState.fieldDescriptor.fileMode,
                                    startDirectory = startDirectory,
                                    onOk = onOk
                                )
                            }
                        }
                    )
                }
            },
            shape = buttonShape,
            onValueChange = onValueChange,
            singleLine = true,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = finalUnfocusedBorderColor,
                focusedBorderColor = focusedBorderColor,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                focusedLabelColor = focusedBorderColor
            )
        )
    }
}

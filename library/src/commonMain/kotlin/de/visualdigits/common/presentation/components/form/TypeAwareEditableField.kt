package de.visualdigits.common.presentation.components.form

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.Enumerable
import de.visualdigits.common.domain.model.KeyValue
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.color
import de.visualdigits.common.domain.model.configuration.ColorPickerFieldDescriptor
import de.visualdigits.common.domain.model.configuration.EnumFieldDescriptor
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.configuration.FileFieldDescriptor
import de.visualdigits.common.domain.model.configuration.PasswordFieldDescriptor
import de.visualdigits.common.domain.model.configuration.ReferenceListFieldDescriptor
import de.visualdigits.common.domain.model.configuration.keyfactory.BooleanEnum
import java.io.File

@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> TypeAwareEditableField(
    modifier: Modifier = Modifier,
    fieldState: FieldState<K, FK>,
    currentValue: Any? = null,
    titleChooseDirectory: UiText,
    titleChooseFile: UiText,
    iconFolder: Painter,
    space: Dp = 8.dp,
    toolTipBackgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    toolTipShape: Shape = MaterialTheme.shapes.extraSmall,
    fieldHeight: Dp = Dp.Unspecified,
    focusedBorderColor: Color = MaterialTheme.colorScheme.outline,
    unfocusedBorderColor: Color = MaterialTheme.colorScheme.onSurface,
    textStyle: TextStyle,
    visibilityIcon: Painter? = null,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    buttonShape: Shape = MaterialTheme.shapes.extraSmall,
    buttonColor: Color = MaterialTheme.colorScheme.surface,
    colorPickerUseOnlySliders: Boolean = false,
    onValueChange: (KeyValue) -> Unit,
    leadingIcon: @Composable (() -> Unit)? = null,
    trailingIcon: @Composable (() -> Unit)? = null,
) {
    val finalUnfocusedBorderColor = if (!fieldState.valid) {
        Severity.Error.color()
    } else if (fieldState.currentValue == null) {
        Severity.Warn.color()
    } else {
        unfocusedBorderColor
    }

    when {
        fieldState.fieldDescriptor is EnumFieldDescriptor<*,*>
                || fieldState.fieldDescriptor is ReferenceListFieldDescriptor
                || fieldState.fieldDescriptor.itemClass?.java?.let { fc -> Enumerable::class.java.isAssignableFrom(fc) } == true -> {
            if (fieldState.fieldDescriptor.fieldClass == BooleanEnum::class) {
                SwitchBox(
                    enabled = fieldState.fieldDescriptor.enabled,
                    currentValue = fieldState.currentValue,
                    label = fieldState.fieldDescriptor.label.asString(),
                    fieldHeight = fieldHeight,
                    focusedBorderColor = focusedBorderColor,
                    unfocusedBorderColor = finalUnfocusedBorderColor,
                    buttonShape = buttonShape,
                    textStyle = textStyle
                ) { value ->
                    onValueChange(KeyValue(fieldState.fieldDescriptor, value))
                }
            } else {
                ComboBox(
                    fieldState = fieldState,
                    space = space,
                    toolTipBackgroundColor = toolTipBackgroundColor,
                    toolTipShape = toolTipShape,
                    textStyle = textStyle,
                    fieldHeight = fieldHeight,
                    focusedBorderColor = focusedBorderColor,
                    unfocusedBorderColor = finalUnfocusedBorderColor,
                    buttonShape = buttonShape,
                    onValueChange = { value ->
                        onValueChange(KeyValue(fieldState.fieldDescriptor, value))
                    },
                )
            }
        }

        fieldState.fieldDescriptor is FileFieldDescriptor<*,*> -> {
            FileChooserBox(
                modifier = modifier,
                fieldState = fieldState,
                iconFolder = iconFolder,
                space = space,
                toolTipBackgroundColor = toolTipBackgroundColor,
                toolTipShape = toolTipShape,
                fieldHeight = fieldHeight,
                textStyle = textStyle,
                leadingIcon = leadingIcon,
                trailingIcon = trailingIcon,
                iconTint = iconTint,
                buttonShape = buttonShape,
                buttonColor = buttonColor,
                titleDirectories = titleChooseDirectory.asString(),
                titleFiles = titleChooseFile.asString(),
                startDirectory = (fieldState.currentValue as? File) ?: fieldState.fieldDescriptor.startDirectory(
                    fieldState.configuration
                ),
                finalUnfocusedBorderColor = finalUnfocusedBorderColor,
                focusedBorderColor = focusedBorderColor,
                onValueChange = { value: String ->
                    onValueChange(KeyValue(fieldState.fieldDescriptor, value))
                },
                onOk = { value ->
                    onValueChange(KeyValue(fieldState.fieldDescriptor, value))
                }
            )
        }

        fieldState.fieldDescriptor is ColorPickerFieldDescriptor<*,*> -> {
            ColorPickerBox(
                modifier = modifier,
                fieldState = fieldState,
                space = space,
                label = fieldState.fieldDescriptor.label.asString(),
                fieldHeight = fieldHeight,
                focusedBorderColor = focusedBorderColor,
                unfocusedBorderColor = unfocusedBorderColor,
                buttonShape = buttonShape,
                textStyle = textStyle,
                slidersOnly = colorPickerUseOnlySliders,
                onValueChange = { value ->
                    onValueChange(KeyValue(fieldState.fieldDescriptor, value))
                },
            )
        }

        fieldState.fieldDescriptor is PasswordFieldDescriptor<*,*> -> {
            PasswordBox(
                modifier = modifier,
                fieldState = fieldState,
                currentValue = currentValue,
                space = space,
                toolTipBackgroundColor = toolTipBackgroundColor,
                toolTipShape = toolTipShape,
                fieldHeight = fieldHeight,
                textStyle = textStyle,
                visibilityIcon = visibilityIcon,
                buttonShape = buttonShape,
                finalUnfocusedBorderColor = finalUnfocusedBorderColor,
                focusedBorderColor = focusedBorderColor,
            ) { value ->
                onValueChange(KeyValue(fieldState.fieldDescriptor, value))
            }
        }

        else -> {
            TextBox(
                modifier = modifier,
                fieldState = fieldState,
                currentValue = currentValue,
                space = space,
                toolTipBackgroundColor = toolTipBackgroundColor,
                toolTipShape = toolTipShape,
                fieldHeight = fieldHeight,
                textStyle = textStyle,
                buttonShape = buttonShape,
                finalUnfocusedBorderColor = finalUnfocusedBorderColor,
                focusedBorderColor = focusedBorderColor,
            ) { value ->
                onValueChange(KeyValue(fieldState.fieldDescriptor, value))
            }
        }
    }
}

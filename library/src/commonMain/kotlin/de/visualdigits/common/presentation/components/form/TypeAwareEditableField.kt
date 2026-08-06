package de.visualdigits.common.presentation.components.form

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.configuration.ColorPickerFieldDescriptor
import de.visualdigits.common.domain.model.configuration.DateTimeFieldDescriptor
import de.visualdigits.common.domain.model.configuration.EnumFieldDescriptor
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.configuration.FileFieldDescriptor
import de.visualdigits.common.domain.model.configuration.LocalDateFieldDescriptor
import de.visualdigits.common.domain.model.configuration.LocalTimeFieldDescriptor
import de.visualdigits.common.domain.model.configuration.PasswordFieldDescriptor
import de.visualdigits.common.domain.model.configuration.ReferenceListFieldDescriptor
import de.visualdigits.common.domain.model.configuration.keyfactory.BooleanEnum
import de.visualdigits.common.domain.model.form.LocalFormFieldResources
import de.visualdigits.common.domain.model.ui.Enumerable
import de.visualdigits.common.domain.model.ui.KeyValue
import de.visualdigits.common.domain.model.ui.UiPlatform
import de.visualdigits.common.domain.util.color
import de.visualdigits.common.presentation.components.androidPlatform
import kotlinx.io.files.Path

@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> TypeAwareEditableField(
    modifier: Modifier = Modifier,
    fieldState: FieldState<K, FK>,
    currentValue: Any? = null,
    colorPickerUseOnlySliders: Boolean = false,
    onValueChange: (KeyValue) -> Unit,
) {
    val androidPlatform = androidPlatform()

    val finalUnfocusedBorderColor = if (!fieldState.fieldDescriptor.enabled) {
        Color.Gray
    } else if (fieldState.valid == Severity.Error || fieldState.valid == Severity.Warn) {
        fieldState.valid.color()
    } else {
        LocalFormFieldResources.current.unfocusedBorderColor
    }

    when {
        fieldState.fieldDescriptor is EnumFieldDescriptor<*,*>
                || fieldState.fieldDescriptor is ReferenceListFieldDescriptor
                || fieldState.fieldDescriptor.itemClass?.java?.let { fc -> Enumerable::class.java.isAssignableFrom(fc) } == true -> {
            if (fieldState.fieldDescriptor.fieldClass == BooleanEnum::class) {
                SwitchBox(
                    enabled = fieldState.fieldDescriptor.enabled && fieldState.fieldDescriptor.enabledCondition(fieldState.configuration, null),
                    currentValue = fieldState.currentValue,
                    label = fieldState.fieldDescriptor.label.asString(),
                ) { value ->
                    onValueChange(KeyValue(fieldState.fieldDescriptor, value))
                }
            } else {
                ComboBox(
                    fieldState = fieldState,
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
                startDirectory = (fieldState.currentValue as? Path) ?: fieldState.fieldDescriptor.startDirectory(
                    fieldState.configuration
                ),
                unfocusedBorderColor = finalUnfocusedBorderColor,
                onValueChange = { value: String ->
                    onValueChange(KeyValue(fieldState.fieldDescriptor, value))
                },
                onOk = { path ->
                    onValueChange(KeyValue(fieldState.fieldDescriptor, path))
                }
            )
        }

        fieldState.fieldDescriptor is ColorPickerFieldDescriptor<*,*> -> {
            ColorPickerBox(
                modifier = modifier,
                fieldState = fieldState,
                label = fieldState.fieldDescriptor.label.asString(),
                slidersOnly = colorPickerUseOnlySliders,
                onValueChange = { value ->
                    onValueChange(KeyValue(fieldState.fieldDescriptor, value))
                },
            )
        }

        fieldState.fieldDescriptor is LocalTimeFieldDescriptor<*,*> -> {
            LocalTimePickerBox(
                modifier = modifier,
                fieldState = fieldState,
                unfocusedBorderColor = finalUnfocusedBorderColor,
                onValueChange = { value ->
                    onValueChange(KeyValue(fieldState.fieldDescriptor, value))
                },
            )
        }

        fieldState.fieldDescriptor is LocalDateFieldDescriptor<*,*> -> {
            LocalDatePickerBox(
                modifier = modifier,
                fieldState = fieldState,
                unfocusedBorderColor = finalUnfocusedBorderColor,
                onValueChange = { value ->
                    onValueChange(KeyValue(fieldState.fieldDescriptor, value))
                },
            )
        }

        fieldState.fieldDescriptor is DateTimeFieldDescriptor<*,*> -> {
            DateTimePickerBox(
                modifier = modifier,
                fieldState = fieldState,
                unfocusedBorderColor = finalUnfocusedBorderColor,
                onValueChange = { value ->
                    onValueChange(KeyValue(fieldState.fieldDescriptor, value))
                },
            )
        }

        fieldState.fieldDescriptor is PasswordFieldDescriptor<*,*> && androidPlatform != UiPlatform.UI_MODE_TYPE_TELEVISION -> {
            PasswordBox(
                modifier = modifier,
                fieldState = fieldState,
                currentValue = currentValue,
                unfocusedBorderColor = finalUnfocusedBorderColor,
            ) { value ->
                onValueChange(KeyValue(fieldState.fieldDescriptor, value))
            }
        }

        else -> {
            TextBox(
                modifier = modifier,
                fieldState = fieldState,
                currentValue = currentValue,
                unfocusedBorderColor = finalUnfocusedBorderColor,
            ) { value ->
                onValueChange(KeyValue(fieldState.fieldDescriptor, value))
            }
        }
    }
}

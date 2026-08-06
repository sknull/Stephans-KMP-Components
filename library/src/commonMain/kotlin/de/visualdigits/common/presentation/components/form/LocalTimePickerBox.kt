package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.common.KmpOffsetDateTime.Companion.OFFSET_SYSTEM_DEFAULT
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.form.LocalDateTimeFieldResources
import de.visualdigits.common.domain.model.form.LocalFormFieldResources
import de.visualdigits.common.domain.util.format
import de.visualdigits.common.domain.util.now
import de.visualdigits.common.presentation.components.button.IndicatorButton
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight
import de.visualdigits.common.presentation.components.util.outlinedTextFieldColors
import kotlinx.datetime.LocalTime

private const val FORMAT_TIME = "HH:mm"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> LocalTimePickerBox(
    modifier: Modifier,
    fieldState: FieldState<K, FK>,
    unfocusedBorderColor: Color,
    onValueChange: (LocalTime?) -> Unit
) {
    val formFieldResources = LocalFormFieldResources.current
    val dateTimeFieldResources = LocalDateTimeFieldResources.current

    val now = LocalTime.now(OFFSET_SYSTEM_DEFAULT)

    var selectedTime by remember { mutableStateOf<LocalTime?>(fieldState.currentValue as? LocalTime ?: now) }

    var showTimeDialog by remember { mutableStateOf(false) }

    val textFieldState = rememberTextFieldState(selectedTime?.format(FORMAT_TIME)?:" ")
    val timePickerState = rememberTimePickerState(initialHour = now.hour, initialMinute = now.minute, is24Hour = true)

    val halfHeight = minimizedLabelHalfHeight(formFieldResources.textStyle)
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .height(formFieldResources.fieldHeight + halfHeight),
        textStyle = formFieldResources.textStyle,
        label = {
            Text(
                text = fieldState.fieldDescriptor.label.asString(),
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        enabled = fieldState.fieldDescriptor.enabled && fieldState.fieldDescriptor.enabledCondition(fieldState.configuration, null),
        shape = formFieldResources.shape,
        readOnly = true,
        state = textFieldState,
        trailingIcon = {
            if (fieldState.fieldDescriptor.enabled && fieldState.fieldDescriptor.enabledCondition(fieldState.configuration, null)) {
                IndicatorButton(
                    width = 30.dp,
                    height = 30.dp,
                    padding = 0.dp,
                    buttonColor = Color.Transparent,
                    content = {
                        Icon(
                            painter = dateTimeFieldResources.timeIcon,
                            contentDescription = null,
                            tint = formFieldResources.iconTint
                        )
                    }
                ) {
                    showTimeDialog = true
                }
            }
        },
        colors = outlinedTextFieldColors(formFieldResources.focusedBorderColor, unfocusedBorderColor)
    )

    if (showTimeDialog) {
        AlertDialog(
            containerColor = dateTimeFieldResources.timePickerColors.containerColor,
            onDismissRequest = { showTimeDialog = false },
            confirmButton = {
                IndicatorButton(
                    buttonColor = Color.Black,
                    textColor = Color.White,
                    text = dateTimeFieldResources.labelOk.asString(),
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    selectedTime = LocalTime(timePickerState.hour, timePickerState.minute)
                    textFieldState.edit { replace(0, length, selectedTime?.format(FORMAT_TIME)?:" ") }
                    onValueChange(selectedTime)
                    showTimeDialog = false
                }
            },
            dismissButton = {
                IndicatorButton(
                    buttonColor = Color.Black,
                    textColor = Color.White,
                    text = dateTimeFieldResources.labelCancel.asString(),
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    showTimeDialog = false
                }
            },
            text = {
                TimePicker(
                    state = timePickerState,
                    colors = dateTimeFieldResources.timePickerColors,
                    modifier = Modifier
                )
            }
        )
    }
}

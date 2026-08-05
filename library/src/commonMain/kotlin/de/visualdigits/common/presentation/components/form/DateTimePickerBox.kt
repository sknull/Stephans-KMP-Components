package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
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
import de.visualdigits.common.domain.model.common.KmpOffsetDateTime
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.form.LocalDateTimeFieldResources
import de.visualdigits.common.domain.model.form.LocalFormFieldResources
import de.visualdigits.common.presentation.components.button.IndicatorButton
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight
import de.visualdigits.common.presentation.components.util.outlinedTextFieldColors
import kotlinx.datetime.LocalDate

private const val FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> DateTimePickerBox(
    modifier: Modifier,
    fieldState: FieldState<K, FK>,
    unfocusedBorderColor: Color,
    onValueChange: (KmpOffsetDateTime) -> Unit
) {
    val formFieldResources = LocalFormFieldResources.current
    val dateTimeFieldResources = LocalDateTimeFieldResources.current

    var selectedDate by remember { mutableStateOf<KmpOffsetDateTime?>(fieldState.currentValue as? KmpOffsetDateTime ?: KmpOffsetDateTime.now()) }

    var showDateDialog by remember { mutableStateOf(false) }
    var showTimeDialog by remember { mutableStateOf(false) }

    val textFieldState = rememberTextFieldState(selectedDate?.format(FORMAT_DATE_TIME)?:" ")
    val datePickerState = rememberDatePickerState()
    val timePickerState = rememberTimePickerState(is24Hour = true)

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
                Row() {
                    IndicatorButton(
                        width = 30.dp,
                        height = 30.dp,
                        padding = 0.dp,
                        buttonColor = Color.Black,
                        textColor = Color.White,
                        shape = MaterialTheme.shapes.extraSmall,
                        content = {
                            Icon(
                                painter = dateTimeFieldResources.dateIcon,
                                contentDescription = null,
                                tint = formFieldResources.iconTint
                            )
                        }
                    ) {
                        showDateDialog = true
                        showTimeDialog = false
                    }
                    IndicatorButton(
                        width = 30.dp,
                        height = 30.dp,
                        padding = 0.dp,
                        buttonColor = Color.Black,
                        textColor = Color.White,
                        shape = MaterialTheme.shapes.extraSmall,
                        content = {
                            Icon(
                                painter = dateTimeFieldResources.timeIcon,
                                contentDescription = null,
                                tint = formFieldResources.iconTint
                            )
                        }
                    ) {
                        showTimeDialog = true
                        showDateDialog = false
                    }
                }
                dateTimeFieldResources.dateIcon
            }
        },
        colors = outlinedTextFieldColors(formFieldResources.focusedBorderColor, unfocusedBorderColor)
    )

    if (showDateDialog) {
        AlertDialog(
            containerColor = dateTimeFieldResources.datePickerColors.containerColor,
            onDismissRequest = { showDateDialog = false },
            confirmButton = {
                IndicatorButton(
                    buttonColor = Color.Black,
                    textColor = Color.White,
                    text = dateTimeFieldResources.labelOk.asString(),
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    selectedDate = datePickerState.selectedDateMillis?.let { dm -> selectedDate?.withLocalDate(LocalDate.fromEpochDays(dm / (1000L * 60 * 60 * 24))) }
                    textFieldState.edit { replace(0, length, selectedDate?.format(FORMAT_DATE_TIME)?:" ") }
                    onValueChange(selectedDate ?: KmpOffsetDateTime.now())
                    showDateDialog = false
                }
            },
            dismissButton = {
                IndicatorButton(
                    buttonColor = Color.Black,
                    textColor = Color.White,
                    text = dateTimeFieldResources.labelCancel.asString(),
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    showDateDialog = false
                }
            },
            text = {
                DatePicker(
                    state = datePickerState,
                    showModeToggle = false,
                    colors = dateTimeFieldResources.datePickerColors,
                    modifier = Modifier
                )
            }
        )
    }

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
                    selectedDate = (selectedDate ?: KmpOffsetDateTime.now()).withTime(timePickerState.hour, timePickerState.minute)
                    textFieldState.edit { replace(0, length, selectedDate?.format(FORMAT_DATE_TIME)?:" ") }
                    onValueChange(selectedDate ?: KmpOffsetDateTime.now())
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

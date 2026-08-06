package de.visualdigits.common.presentation.components.form

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
import androidx.compose.material3.getSelectedDate
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.form.LocalDateTimeFieldResources
import de.visualdigits.common.domain.model.form.LocalFormFieldResources
import de.visualdigits.common.domain.util.format
import de.visualdigits.common.domain.util.now
import de.visualdigits.common.presentation.components.button.IndicatorButton
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight
import de.visualdigits.common.presentation.components.util.outlinedTextFieldColors
import kotlinx.datetime.LocalDate
import kotlinx.datetime.toJavaLocalDate
import kotlinx.datetime.toKotlinMonth

private const val FORMAT_DATE = "yyyy-MM-dd"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> LocalDatePickerBox(
    modifier: Modifier,
    fieldState: FieldState<K, FK>,
    unfocusedBorderColor: Color,
    onValueChange: (LocalDate) -> Unit
) {
    val formFieldResources = LocalFormFieldResources.current
    val dateTimeFieldResources = LocalDateTimeFieldResources.current

    val now = LocalDate.now()

    var selectedDate by remember { mutableStateOf<LocalDate?>(fieldState.currentValue as? LocalDate ?: now) }

    var showDateDialog by remember { mutableStateOf(false) }

    val textFieldState = rememberTextFieldState(selectedDate?.format(FORMAT_DATE)?:" ")
    val datePickerState = rememberDatePickerState(initialSelectedDate = now.toJavaLocalDate())

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
                            painter = dateTimeFieldResources.dateIcon,
                            contentDescription = null,
                            tint = formFieldResources.iconTint
                        )
                    }
                ) {
                    showDateDialog = true
                }
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
                    buttonColor = Color.Transparent,
                    textColor = Color.White,
                    text = dateTimeFieldResources.labelOk.asString(),
                    shape = MaterialTheme.shapes.extraSmall
                ) {
                    selectedDate = datePickerState.getSelectedDate()?.let { dm ->
                        LocalDate(dm.year, dm.month.toKotlinMonth(), dm.dayOfMonth)
                    }
                    textFieldState.edit { replace(0, length, selectedDate?.format(FORMAT_DATE)?:" ") }
                    onValueChange(selectedDate ?: now)
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
}

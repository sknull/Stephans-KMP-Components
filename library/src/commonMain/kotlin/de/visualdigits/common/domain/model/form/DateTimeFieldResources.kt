package de.visualdigits.common.domain.model.form

import androidx.compose.material3.DatePickerColors
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TimePickerColors
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import de.visualdigits.common.domain.model.ui.UiText

@OptIn(ExperimentalMaterial3Api::class)
data class DateTimeFieldResources(
    val datePickerColors: DatePickerColors,
    val timePickerColors: TimePickerColors,
    val dateIcon: Painter,
    val timeIcon: Painter,
    val labelOk: UiText,
    val labelCancel: UiText,
)

@Composable
fun dateDefaultColors(): DatePickerColors = DatePickerDefaults.colors()
    .copy(
        containerColor = Color(0xFF888888),

        weekdayContentColor = Color(0xFF000000),

        dayContentColor = Color(0xFF000000),
        selectedDayContainerColor = Color(0xFFBFBFFF),
        selectedDayContentColor = Color(0xFF515196),

        todayContentColor =Color(0xFFFF0000),
        todayDateBorderColor = Color(0xFFFF8888),
        dayInSelectionRangeContainerColor = Color(0xFFFE4040),
        dayInSelectionRangeContentColor = Color(0xFF028D86),

        yearContentColor = Color(0xFF000000),
        currentYearContentColor = Color(0xFF138600),
        selectedYearContainerColor = Color(0xFFFF0000),
        selectedYearContentColor = Color(0xFF8B4A4A),

        disabledYearContentColor = Color(0xFF888888),
        disabledSelectedYearContentColor = Color(0xFF888888),
        disabledSelectedYearContainerColor = Color(0xFF888888),
        disabledDayContentColor = Color(0xFF888888),
        disabledSelectedDayContentColor = Color(0xFF888888),
        disabledSelectedDayContainerColor = Color(0xFF888888),

        titleContentColor = Color(0xFF000000),
        headlineContentColor = Color(0xFF000000),
        subheadContentColor = Color(0xFF000000),
        navigationContentColor = Color(0xFF000000),
        dividerColor = Color(0xFF000000),
        dateTextFieldColors = TextFieldDefaults.colors().copy(
            focusedTextColor = Color.Red,
            unfocusedTextColor = Color.Red,
            focusedPlaceholderColor = Color.Cyan,
            unfocusedPlaceholderColor = Color.Cyan,
            focusedLabelColor = Color.Green,
            unfocusedLabelColor = Color.Green,
            errorTextColor = Color.Red,

            focusedIndicatorColor = Color.Red,
            unfocusedIndicatorColor = Color.Black
        ),
    )

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun timeDefaultColors(): TimePickerColors = TimePickerDefaults.colors()
    .copy(
        containerColor = Color(0xFF888888),

        clockDialColor = Color(0xFF000000),
        clockDialSelectedContentColor = Color(0xFFFF0000),
        clockDialUnselectedContentColor = Color(0xFF000000),

        selectorColor = Color(0xFFFF6B6B),

//        periodSelectorBorderColor = TODO(),
//        periodSelectorSelectedContainerColor = TODO(),
//        periodSelectorUnselectedContainerColor = TODO(),
//        periodSelectorSelectedContentColor = TODO(),
//        periodSelectorUnselectedContentColor = TODO(),

        timeSelectorSelectedContainerColor = Color(0xFF000000),
        timeSelectorUnselectedContainerColor = Color(0xFFAAAAAA),
        timeSelectorSelectedContentColor = Color(0xFFFFFFFF),
        timeSelectorUnselectedContentColor = Color(0xFF444444)
    )

val LocalDateTimeFieldResources = staticCompositionLocalOf<DateTimeFieldResources> {
    error("No DateTimeFieldResources provided")
}

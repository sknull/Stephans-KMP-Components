package de.visualdigits.common.domain.model.form

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

data class FormFieldResources(
    val fieldHeight: Dp = Dp.Unspecified,
    val textStyle: TextStyle,
    val iconTint: Color,
    val shape: Shape,
    val focusedBorderColor: Color,
    val unfocusedBorderColor: Color,
    val visibilityIcon: Painter? = null,
)

val LocalFormFieldResources = staticCompositionLocalOf<FormFieldResources> {
    error("No FormFieldResources provided")
}

package de.visualdigits.common.domain.model.form

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.ui.UiText

data class FormResources(
    val backgroundColor: Color,
    val buttonShape: Shape,
    val buttonColor: Color,
    val iconOk: Painter,
    val tooltipOk: UiText? = null,
    val iconCancel: Painter,
    val tooltipCancel: UiText? = null,
    val containerShape: Shape,
    val space: Dp = 8.dp,
)

val LocalFormResources = staticCompositionLocalOf<FormResources> {
    error("No FormResources provided")
}

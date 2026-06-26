package de.visualdigits.common.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp

@Composable
actual fun PlatformToolTip(
    modifier: Modifier,
    text: String?,
    textStyle: TextStyle,
    shadowSize: Dp,
    space: Dp,
    backgroundColor: Color,
    shape: Shape,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier
    ) {
        content()
    }
}

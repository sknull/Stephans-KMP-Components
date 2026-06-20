package de.visualdigits.common.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import de.visualdigits.common.domain.model.toHsvColor
import de.visualdigits.common.presentation.components.util.drawLed

@Composable
fun Led(
    modifier: Modifier = Modifier,
    radius: Dp,
    colorOn: Color,
    colorOff: Color = Color(0xFF333333),
    isOn: Boolean = true
) {
    val baseColor = if (isOn) colorOn else colorOff
    val glossColor = Color.White.copy(alpha = if (isOn) 0.8f else 0.2f)
    val glowColor = if (isOn) baseColor.copy(alpha = 0.5f) else Color.Transparent

    Box(
        modifier = modifier
            .width(radius * 2)
            .height(radius * 2)
            .drawWithCache {
                onDrawWithContent {
                    val radius = size.width / 2.0
                    drawLed(
                        x = radius,
                        y = radius,
                        radius = radius,
                        glowColor = glowColor,
                        baseColor = baseColor.toHsvColor(),
                        glossColor = glossColor
                    )
                }
            }
    )
}

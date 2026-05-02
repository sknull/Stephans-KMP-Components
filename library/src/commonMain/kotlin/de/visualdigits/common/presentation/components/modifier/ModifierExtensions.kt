package de.visualdigits.common.presentation.components.modifier

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.withTransform
import kotlin.math.PI
import kotlin.math.atan
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sin
import kotlin.math.sqrt

fun Outline.createPath(): Path = when (this) {
    is Outline.Generic -> path
    is Outline.Rectangle -> Path().apply { addRect(rect) }
    is Outline.Rounded -> Path().apply { addRoundRect(roundRect) }
}

fun ContentDrawScope.draw(
    offsetX: Float,
    offsetY: Float? = null,
    outline: Outline,
    color: Color? = null,
    brush: Brush? = null,
    width: Float
) {
    withTransform({
        translate(left = offsetX, top = offsetY?:offsetX)
    }) {
        if (color != null) {
            drawOutline(
                outline = outline,
                color = color,
                style = Stroke(width = width)
            )
        } else if (brush != null) {
            drawOutline(
                outline = outline,
                brush = brush,
                style = Stroke(width = width)
            )
        }
    }
}

fun createBevelBrush(
    widthPx: Float,
    heightPx: Float,
    borderAlpha: Float,
    inset: Boolean
): Brush {
    val color1 = Color.White.copy(alpha = borderAlpha)
    val color2 = Color.Black.copy(alpha = borderAlpha)
    val (startColor, endColor) = if (inset) {
        Pair(color2, color1)
    } else {
        Pair(color1, color2)
    }

    val alphaRad = atan(-heightPx / widthPx)
    val gradientAngleRad = alphaRad + (PI / 2.0)

    val x = cos(gradientAngleRad).toFloat()
    val y = sin(gradientAngleRad).toFloat()

    val center = Offset(widthPx / 2.0f, heightPx / 2.0f)
    val radius = sqrt(widthPx.pow(2) + heightPx.pow(2)) / 2f

    val brush = Brush.linearGradient(
        colorStops = arrayOf(
            0.0f to startColor,
            0.48f to startColor,
            0.52f to endColor,
            1.0f to endColor,
        ),
        start = center - Offset(x * radius, y * radius),
        end = center + Offset(x * radius, y * radius)
    )

    return brush
}

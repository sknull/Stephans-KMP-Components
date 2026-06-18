package de.visualdigits.common.presentation.components.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.innerShadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

fun Modifier.angledInnerShadow(
    /**
     * Angle in degrees 0 - 360 (0 is east, 90 is north, 180 is west, 270 is south)
     * Negative values will be normalized to the 0 - 360 degree range.
     */
    angle: Float,
    distance: Dp,
    shape: Shape = RectangleShape,
    radius: Dp = 6.dp,
    spread: Dp = 2.dp,
    color: Color = Color.Black,
    alpha: Float = 0.5f,
    drawInsets: Boolean = true,
    insetSize: Dp = 1.dp,
    insetColorLight: Color = Color.White,
    insetColorShadow: Color = Color.Black
): Modifier {
    val angleInDegrees = ((angle % 360f) + 360f) % 360f
    val shadowModifier = innerShadow(
        shape = shape,
        shadow = Shadow(
            radius = radius,
            spread = spread,
            color = color.copy(alpha = alpha),
            offset = calculatePhotoshopOffset(angleInDegrees, distance)
        )
    )

    return if (drawInsets) {
        shadowModifier.drawBehind {
            if (angleInDegrees in 0f..180f) {
                drawLineBottom(insetColorLight, insetSize)
                drawLineTop(insetColorShadow, insetSize)
            }
            if (angleInDegrees in 0f..90f || angleInDegrees in 270f..360f) {
                drawLineStart(insetColorLight, insetSize)
                drawLineEnd(insetColorShadow, insetSize)
            }
            if (angleInDegrees in 90f..270f) {
                drawLineEnd(insetColorLight, insetSize)
                drawLineStart(insetColorShadow, insetSize)
            }
            if (angleInDegrees in 180f..360f) {
                drawLineTop(insetColorLight, insetSize)
                drawLineBottom(insetColorShadow, insetSize)
            }
        }
    } else {
        shadowModifier
    }
}

private fun DrawScope.drawLineTop(
    edgeColor: Color,
    insetSize: Dp
) {
    drawLine(
        color = edgeColor,
        start = Offset(0f, (insetSize / 2).toPx()),
        end = Offset(size.width, (insetSize / 2).toPx()),
        strokeWidth = insetSize.toPx()
    )
}

private fun DrawScope.drawLineBottom(
    edgeColor: Color,
    insetSize: Dp
) {
    drawLine(
        color = edgeColor,
        start = Offset(0f, size.height - (insetSize / 2).toPx()),
        end = Offset(size.width, size.height - (insetSize / 2).toPx()),
        strokeWidth = insetSize.toPx()
    )
}

private fun DrawScope.drawLineStart(
    edgeColor: Color,
    insetSize: Dp
) {
    drawLine(
        color = edgeColor,
        start = Offset((insetSize / 2).toPx(), 0f),
        end = Offset((insetSize / 2).toPx(), size.height),
        strokeWidth = insetSize.toPx()
    )
}

private fun DrawScope.drawLineEnd(
    edgeColor: Color,
    insetSize: Dp
) {
    drawLine(
        color = edgeColor,
        start = Offset(size.width - (insetSize / 2).toPx(), 0f),
        end = Offset(size.width - (insetSize / 2).toPx(), size.height),
        strokeWidth = insetSize.toPx()
    )
}

private fun calculatePhotoshopOffset(
    angleInDegrees: Float,
    distance: Dp
): DpOffset {
    // Photoshop-Winkel an das Koordinatensystem von Compose anpassen (Y-Achse verläuft nach unten)
    // Ein Winkel von 120° bedeutet in Photoshop: Licht kommt von oben links, Schatten fällt nach unten rechts.
    val angleInRadians = Math.toRadians((angleInDegrees).toDouble())

    // Distanz aufteilen in X- und Y-Vektoren
    // Da das Licht aus der Richtung kommt, fällt der Schatten in die entgegengesetzte Richtung (-cos / -sin)
    // In Compose zeigt +Y nach unten, daher passen wir die Vorzeichen für den Schattenwurf an:
    val x = (-cos(angleInRadians) * distance.value).toFloat().dp
    val y = (sin(angleInRadians) * distance.value).toFloat().dp

    return DpOffset(x = x, y = y)
}

package de.visualdigits.common.presentation.components.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

fun Modifier.beveledBorder(
    width: Dp = Dp.Unspecified,
    height: Dp = Dp.Unspecified,
    longSide: Dp = Dp.Unspecified,
    aspect: Float? = null,
    borderSize: Dp = 1.dp,
    shape: Shape,
    alpha: Float = 0.4f,
    inset: Boolean = false
): Modifier {
    return drawWithCache {

        val (widthPx, heightPx) = if (width != Dp.Unspecified && height != Dp.Unspecified) {
            width.toPx() to height.toPx()
        } else if (longSide != Dp.Unspecified && aspect != null) {
            val longSidePx = longSide.toPx()
            if (aspect >= 1.0f) {
                longSidePx to longSidePx / aspect
            } else {
                longSidePx * aspect to longSidePx
            }
        } else {
            size.width to size.height
        }

        val outerBevelOutline = shape.createOutline(
            size = Size(
                width =  widthPx - 2.0f,
                height = heightPx - 2.0f
            ), layoutDirection = layoutDirection, density = this@drawWithCache
        )

        val outerClipPath = shape.createOutline(
            size = Size(
                width =  widthPx,
                height = heightPx
            ), layoutDirection = layoutDirection, density = this@drawWithCache
        ).createPath()

        val outerBevelBrush = createBevelBrush(
            widthPx = widthPx,
            heightPx = heightPx,
            borderAlpha = alpha,
            inset = inset
        )

        onDrawWithContent {
            clipPath(outerClipPath) {
                this@onDrawWithContent.drawContent()
                this@onDrawWithContent.draw(
                    offsetX = 1.0f,
                    outline = outerBevelOutline,
                    brush = outerBevelBrush,
                    width = borderSize.toPx()
                )
            }
        }
    }
}

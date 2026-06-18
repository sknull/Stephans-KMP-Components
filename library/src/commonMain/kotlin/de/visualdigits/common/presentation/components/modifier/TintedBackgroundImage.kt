package de.visualdigits.common.presentation.components.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.toRect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

fun Modifier.tintedBackgroundImage(
    image: ImageBitmap,
    tint: Color,
    contentScale: ContentScale = ContentScale.FillBounds,
    finalZoomFactor: Float = 1.0f,
    finalOffsetX: Dp = 0.dp,
    finalOffsetY: Dp = 0.dp,
    finalAlpha: Float = 1.0f,
): Modifier {
    return drawWithCache {
        val srcSize = Size(image.width.toFloat(), image.height.toFloat())
        val scaleFactor = contentScale.computeScaleFactor(
            srcSize = srcSize,
            dstSize = size
        )
        val targetWidth = (srcSize.width * scaleFactor.scaleX * finalZoomFactor).roundToInt()
        val targetHeight = (srcSize.height * scaleFactor.scaleY * finalZoomFactor).roundToInt()
        val targetSize = IntSize(targetWidth, targetHeight)

        onDrawBehind {
            // calculate dynamically when outside parameters change due to animation, etc.
            val finalOffsetXPx = finalOffsetX.toPx()
            val finalOffsetYPx = finalOffsetY.toPx()

            val dx = ((size.width - targetWidth) / 2 + finalOffsetXPx).roundToInt()
            val dy = ((size.height - targetHeight) / 2 + finalOffsetYPx).roundToInt()
            val targetOffset = IntOffset(dx, dy)

            drawIntoCanvas { canvas ->
                canvas.saveLayer(size.toRect(), Paint().apply { alpha = finalAlpha })
                drawRect(
                    color = tint,
                    size = size
                )
                drawImage(
                    image = image,
                    dstOffset = targetOffset,
                    dstSize = targetSize,
                    blendMode = BlendMode.Hardlight
                )
                canvas.restore()
            }
        }
    }
}

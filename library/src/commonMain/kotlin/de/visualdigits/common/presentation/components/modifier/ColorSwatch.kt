package de.visualdigits.common.presentation.components.modifier

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.graphics.drawscope.withTransform
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.ColorPickerController
import de.visualdigits.common.domain.model.HsvColor
import de.visualdigits.common.domain.util.copyFactor

fun Modifier.colorSwatch(
    size: Dp,
    selectedColor: Color = Color.Transparent,
    controller: ColorPickerController? = null,
    flatLook: Boolean = false,
    hoverColor: Color = Color.White.copy(alpha = 0.2f),
    borderSize: Dp = 1.dp,
    borderAlpha: Float = 0.4f,
    shape: Shape,
    isHovered: Boolean = false
): Modifier {
    val color = controller?.selectedColor?.value ?: selectedColor

    return drawWithCache {
        val sizePx = size.toPx()

        val outerBevelOutline = shape.createOutline(
            size = Size(
                width = sizePx - 2.0f,
                height = sizePx - 2.0f
            ), layoutDirection = layoutDirection, density = this@drawWithCache
        )
        val outerClipPath = outerBevelOutline.createPath()
        val outerBevelBrush = createBevelBrush(
            widthPx = sizePx - 2.0f,
            heightPx = sizePx - 2.0f,
            borderAlpha = borderAlpha,
            inset = false
        )

        val glossBrush = Brush.radialGradient(
            center = Offset(x = sizePx / 2.0f, y = sizePx / 2.0f),
            radius = sizePx,
            colorStops = arrayOf(
                0.0f to Color.Transparent, // center
                0.45f to Color.White.copy(alpha = 0.2f),
                0.5f to Color.Transparent, // edge
            )
        )

        onDrawWithContent {
            clipPath(outerClipPath) {
                // background
                if (!flatLook) {
                    val hsvColor = HsvColor.fromComposeColor(color)
                    val valueFactor = if (hsvColor.value < 0.5f) 1.5f else 0.5f
                    drawRect(
                        brush = Brush.verticalGradient(
                            colorStops = arrayOf(
                                0.0f to color,
                                1.0f to hsvColor.copy(value = hsvColor.value * valueFactor).toComposeColor(),
                            )
                        )
                    )
                } else {
                    drawRect(
                        color = color
                    )
                }

                this@onDrawWithContent.drawContent()

                // gloss
                if (!flatLook) {
                    clipPath(outerClipPath) {
                        withTransform({
                            scale(scaleX = 15.0f, scaleY = 1.0f)
                            rotate(-10.0f)
                            translate(top = sizePx / -2.0f)
                        }) {
                            drawCircle(
                                brush = glossBrush
                            )
                        }
                    }
                }

                // outer bevel
                if (!flatLook) {
                    this@onDrawWithContent.draw(offsetX = 1.0f, outline = outerBevelOutline, brush = outerBevelBrush, width = borderSize.toPx())
                }

                if (isHovered) {
                    drawRect(
                        topLeft = Offset(1),
                        size = Size(width = sizePx - 2, height = sizePx - 2),
                        color = hoverColor,
                        blendMode = BlendMode.Screen
                    )
                }
            }
        }
    }
}

package de.visualdigits.common.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.shadow.Shadow
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import de.visualdigits.common.domain.model.HsvColor
import de.visualdigits.common.presentation.components.button.IndicatorButton
import de.visualdigits.common.presentation.components.colorfulslider.ColorfulSlider
import de.visualdigits.common.presentation.components.colorfulslider.MaterialSliderDefaults
import de.visualdigits.common.presentation.components.colorfulslider.SliderBrushColor
import kotlin.math.roundToInt


@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    label: String? = null,
    initialColor: HsvColor?,
    size: Dp = 200.dp,
    space: Dp,
    hasSwatch: Boolean = true,
    slidersOnly: Boolean = false,
    onColorChanged: (hsvColor: HsvColor) -> Unit = {}
) {
    val controller = rememberColorPickerController()

    var currentColor by remember(initialColor) {
        mutableStateOf(initialColor ?: HsvColor.fromComposeColor(Color.White))
    }

    LaunchedEffect(initialColor) {
        initialColor?.let {
            val composeColor = it.toComposeColor()
            if (controller.selectedColor.value != composeColor) {
                controller.selectByColor(composeColor, fromUser = false)
            }
        }
    }

    Column(
        modifier = modifier
            .width(size)
            .height(if (slidersOnly) {
                (if (label != null) 35.dp + space else 0.dp) + // label
                (35.dp + space) * 3 + // sliders
                (if (hasSwatch) 50.dp + space else 0.dp) // swatch
            } else {
                size + // color wheel
                (if (label != null) 35.dp + space else 0.dp) + // label
                35.dp + space + // sliders
                (if (hasSwatch) 50.dp + space else 0.dp) // swatch
            }),
        verticalArrangement = Arrangement.spacedBy(space),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        label?.let { l ->
            Text(
                modifier = Modifier
                    .height(35.dp),
                text = l,
                style = MaterialTheme.typography.titleSmall,
                textAlign = TextAlign.Start
            )
        }

        if (slidersOnly) {
            // Hue
            val hueGradientBrush = remember {
                Brush.linearGradient(
                    colors = listOf(
                        Color(0xFFFF0000), // Rot (0°)
                        Color(0xFFFFFF00), // Gelb (60°)
                        Color(0xFF00FF00), // Grün (120°)
                        Color(0xFF00FFFF), // Cyan (180°)
                        Color(0xFF0000FF), // Blau (240°)
                        Color(0xFFFF00FF), // Magenta (300°)
                        Color(0xFFFF0000)  // Rot (360°)
                    ),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, 0f)
                )
            }

            ColorfulSlider(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(size),
                value = initialColor?.hue?.toFloat() ?: 0.0f,
                onValueChange = {
                    currentColor = currentColor.copy(hue = it.roundToInt())
                    onColorChanged(currentColor)
                },
                valueRange = 0.0f .. 360.0f,
                trackHeight = 28.dp,
                thumbRadius = 17.dp,
                colors = MaterialSliderDefaults.materialColors(
                    thumbColor = SliderBrushColor(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    inactiveTrackColor = SliderBrushColor(
                        brush = hueGradientBrush
                    ),
                    activeTrackColor = SliderBrushColor(
                        brush = hueGradientBrush,
                    )
                )
            )

            // Saturation
            val satGradientBrush = remember(currentColor.hue, currentColor.value) {
                Brush.linearGradient(
                    colors = listOf(
                        currentColor.copy(saturation = 0.0f).toComposeColor(),
                        currentColor.copy(saturation = 1.0f).toComposeColor()
                    ),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, 0f)
                )
            }
            ColorfulSlider(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(size),
                value = initialColor?.saturation ?: 1.0f,
                onValueChange = {
                    currentColor = currentColor.copy(saturation = it)
                    onColorChanged(currentColor)
                },
                valueRange = 0.0f .. 1.0f,
                trackHeight = 28.dp,
                thumbRadius = 17.dp,
                colors = MaterialSliderDefaults.materialColors(
                    thumbColor = SliderBrushColor(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    inactiveTrackColor = SliderBrushColor(
                        brush = satGradientBrush
                    ),
                    activeTrackColor = SliderBrushColor(
                        brush = satGradientBrush
                    )
                )
            )

            // Value
            val valGradientBrush = remember(currentColor.hue, currentColor.saturation) {
                Brush.linearGradient(
                    colors = listOf(
                        currentColor.copy(value = 0.0f).toComposeColor(),
                        currentColor.copy(value = 1.0f).toComposeColor()
                    ),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, 0f)
                )
            }
            ColorfulSlider(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(size),
                value = initialColor?.value ?: 1.0f,
                onValueChange = {
                    currentColor = currentColor.copy(value = it)
                    onColorChanged(currentColor)
                },
                valueRange = 0.0f .. 1.0f,
                trackHeight = 28.dp,
                thumbRadius = 17.dp,
                colors = MaterialSliderDefaults.materialColors(
                    thumbColor = SliderBrushColor(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    inactiveTrackColor = SliderBrushColor(
                        brush = valGradientBrush
                    ),
                    activeTrackColor = SliderBrushColor(
                        brush = valGradientBrush
                    )
                )
            )

            if (hasSwatch) {
                IndicatorButton(
                    modifier = Modifier
                        .dropShadow(
                            shape = RoundedCornerShape(4.dp),
                            shadow = Shadow(
                                radius = 1.dp,
                                spread = 1.dp,
                                color = Color.Black.copy(alpha = 0.2f),
                                offset = DpOffset((-1).dp, 1.dp)
                            )
                        ),
                    textColor = Color.White,
                    buttonColor = currentColor.toComposeColor(),
                    flatLook = true,
                    isHoverable = false,
                    width = 50.dp,
                    height = 50.dp,
                    shape = RoundedCornerShape(4.dp),
                )
            }
        } else {
            HsvColorPicker(
                modifier = Modifier
                    .pointerHoverIcon(PointerIcon.Hand)
                    .width(size)
                    .height(size),
                controller = controller,
                onColorChanged = { colorEnvelope ->
                    if (colorEnvelope.fromUser) {
                        onColorChanged(HsvColor.fromComposeColor(colorEnvelope.color))
                    }
                }
            )

            // Value
            val valGradientBrush = remember(currentColor.hue, currentColor.saturation) {
                Brush.linearGradient(
                    colors = listOf(
                        currentColor.copy(value = 0.0f).toComposeColor(),
                        currentColor.copy(value = 1.0f).toComposeColor()
                    ),
                    start = androidx.compose.ui.geometry.Offset(0f, 0f),
                    end = androidx.compose.ui.geometry.Offset(Float.POSITIVE_INFINITY, 0f)
                )
            }
            ColorfulSlider(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .width(size),
                value = initialColor?.value ?: 1.0f,
                onValueChange = {
                    currentColor = currentColor.copy(value = it)
                    onColorChanged(currentColor)
                },
                valueRange = 0.0f .. 1.0f,
                trackHeight = 28.dp,
                thumbRadius = 17.dp,
                colors = MaterialSliderDefaults.materialColors(
                    thumbColor = SliderBrushColor(
                        color = MaterialTheme.colorScheme.onSurface
                    ),
                    inactiveTrackColor = SliderBrushColor(
                        brush = valGradientBrush
                    ),
                    activeTrackColor = SliderBrushColor(
                        brush = valGradientBrush
                    )
                )
            )

// todo broken since 05.06.2026
//            BrightnessSlider(
//                modifier = Modifier
//                    .pointerHoverIcon(PointerIcon.Hand)
//                    .height(35.dp),
//                controller = controller,
//            )

            if (hasSwatch) {
                IndicatorButton(
                    modifier = Modifier
                        .dropShadow(
                            shape = RoundedCornerShape(4.dp),
                            shadow = Shadow(
                                radius = 1.dp,
                                spread = 1.dp,
                                color = Color.Black.copy(alpha = 0.2f),
                                offset = DpOffset((-1).dp, 1.dp)
                            )
                        ),
                    textColor = Color.White,
                    buttonColor = controller.selectedColor.value,
                    flatLook = true,
                    isHoverable = false,
                    width = 50.dp,
                    height = 50.dp,
                    shape = RoundedCornerShape(4.dp),
                )
            }
        }
    }
}

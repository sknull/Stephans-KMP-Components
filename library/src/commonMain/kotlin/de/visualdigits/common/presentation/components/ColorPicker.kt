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
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.dropShadow
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


@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    label: String? = null,
    initialColor: HsvColor?,
    size: Dp = 200.dp,
    space: Dp,
    hasSwatch: Boolean = true,
    onColorChanged: (hsvColor: HsvColor) -> Unit = {}
) {
    val controller = rememberColorPickerController()

    val composeColor = remember(initialColor) {
        initialColor?.toComposeColor() ?: Color.Transparent
    }

    LaunchedEffect(composeColor) {
        if (controller.selectedColor.value != composeColor && composeColor != Color.Transparent) {
            controller.selectByColor(composeColor, fromUser = false)
        }
    }

    Column(
        modifier = modifier
            .width(size)
            .height(size + // color wheel
                    (if (label != null) 35.dp + space else 0.dp) + // label
                    35.dp + space + // slider
                    (if (hasSwatch) 50.dp + space else 0.dp) // swatch
            ),
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

        BrightnessSlider(
            modifier = Modifier
                .pointerHoverIcon(PointerIcon.Hand)
                .height(35.dp),
            controller = controller,
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

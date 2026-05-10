package de.visualdigits.common.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.github.skydoves.colorpicker.compose.BrightnessSlider
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import de.visualdigits.common.domain.model.HsvColor
import de.visualdigits.common.presentation.components.button.IndicatorButton


@Composable
fun ColorPicker(
    modifier: Modifier = Modifier,
    label: String,
    initialColor: HsvColor?,
    size: Dp = 200.dp,
    space: Dp,
    hasSwatch: Boolean = true,
    onColorChanged: (colorEnvelope: ColorEnvelope) -> Unit = {}
) {
    val controller = rememberColorPickerController()
    Column(
        modifier = modifier
            .width(size)
            .height(size + 70.dp + space * 3 + if (hasSwatch) 50.dp + space else 0.dp),
        verticalArrangement = Arrangement.spacedBy(space),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            modifier = Modifier
                .height(35.dp),
            text = label,
            style = MaterialTheme.typography.titleSmall,
            textAlign = TextAlign.Start
        )

        val initialColor1 = initialColor?.toColor() ?: Color.Transparent

        HsvColorPicker(
            modifier = Modifier
                .pointerHoverIcon(PointerIcon.Hand)
                .width(size)
                .height(size),
            controller = controller,
            initialColor = initialColor1,
            onColorChanged = onColorChanged
        )

        BrightnessSlider(
            modifier = Modifier
                .pointerHoverIcon(PointerIcon.Hand)
                .height(35.dp),
            initialColor = initialColor1,
            controller = controller,
        )

        if (hasSwatch) {
            IndicatorButton(
                textColor = Color.White,
                buttonColor = controller.selectedColor.value,
                flatLook = true,
                width = 50.dp,
                height = 50.dp,
                shape = RoundedCornerShape(4.dp),
            )
        }
    }
}

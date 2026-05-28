package de.visualdigits.common.domain.util

import androidx.compose.ui.graphics.Color
import de.visualdigits.common.domain.model.HsvColor

/**
 * Returns a copy of this color with the given [hue], [saturation] and [value] values.
 *
 * hue 0 - 360
 * saturation 0.0f - 1.0f
 * value 0.0f - 1.0f
 *
 */
fun Color.copy(hue: Int = 0, saturation: Float = 1.0f, value: Float = 1.0f, alpha: Float = 1.0f): Color {
    return HsvColor.fromComposeColor(this)
        .copy(
            hue = ((hue % 360) + 360) % 360,
            saturation = saturation.coerceIn(0f, 1f),
            value = value.coerceIn(0f, 1f),
        )
        .toComposeColor()
        .copy(alpha = alpha)
}

fun Color.copyFactor(hueShift: Int = 0, saturationFactor: Float = 1.0f, valueFactor: Float = 1.0f, alphaFactor: Float = 1.0f): Color {
    val hsvColor = HsvColor.fromComposeColor(this)
    return hsvColor
        .copy(
            hue = (((hsvColor.hue + hueShift) % 360) + 360) % 360,
            saturation = (hsvColor.saturation * saturationFactor).coerceIn(0f, 1f),
            value = (hsvColor.value * valueFactor).coerceIn(0f, 1f),
        )
        .toComposeColor()
        .copy(alpha = this.alpha * alphaFactor)
}

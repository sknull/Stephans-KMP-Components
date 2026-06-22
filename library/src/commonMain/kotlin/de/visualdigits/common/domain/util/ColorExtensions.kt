package de.visualdigits.common.domain.util

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import de.visualdigits.common.domain.model.color.HsvColor

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
            saturation = saturation.toDouble().coerceIn(0.0, 1.0),
            value = value.toDouble().coerceIn(0.0, 1.0),
        )
        .toComposeColor()
        .copy(alpha = alpha)
}

fun Color.copyFactor(hueShift: Int = 0, saturationFactor: Float = 1.0f, valueFactor: Float = 1.0f, alphaFactor: Float = 1.0f): Color {
    val hsvColor = HsvColor.fromComposeColor(this)
    return hsvColor
        .copy(
            hue = (((hsvColor.hue + hueShift) % 360) + 360) % 360,
            saturation = (hsvColor.saturation * saturationFactor).coerceIn(0.0, 1.0),
            value = (hsvColor.value * valueFactor).coerceIn(0.0, 1.0),
        )
        .toComposeColor()
        .copy(alpha = this.alpha * alphaFactor)
}

fun String.toComposeColor(): Color {
    val hex = this.removePrefix("#").removePrefix("0x")

    return when (hex.length) {
        6 -> {
            Color("FF$hex".toLong(16))
        }
        8 -> {
            Color(hex.toLong(16))
        }
        else -> throw IllegalArgumentException("Invalid format: $this")
    }
}

fun String.toHsvColor(): HsvColor {
    return if (startsWith("#") || startsWith("0x")) {
        HsvColor.fromComposeColor(toComposeColor())
    } else {
        HsvColor.fromHex(this)
    }
}

fun Color.toWebColor(): String {
    return String.format("#%08X", toArgb())
}

fun Color.toWebColorShort(): String {
    return String.format("#%06X", 0xFFFFFF and toArgb())
}

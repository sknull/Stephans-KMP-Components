package de.visualdigits.common.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import de.visualdigits.common.domain.util.toComposeColor
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Immutable
data class HsvColor(
    val hue: Int,
    val saturation: Float,
    val value: Float
) {

    private constructor(hsv: FloatArray) : this(hsv[0].roundToInt(), hsv[1], hsv[2])

    companion object {

        private const val CHARS = "0123456789abcdef"

        fun fromComposeColor(color: Color): HsvColor {
            val r = color.red
            val g = color.green
            val b = color.blue
            val max = max(r, max(g, b))
            val min = min(r, min(g, b))
            val delta = max - min
            val hue = when {
                delta == 0f -> 0f
                max == r -> ((g - b) / delta) % 6f
                max == g -> ((b - r) / delta) + 2f
                else -> ((r - g) / delta) + 4f
            } * 60f
            val normalizedHue = ((hue % 360f) + 360f) % 360f
            val saturation = if (max == 0f) 0f else delta / max
            val value = max
            return HsvColor(
                floatArrayOf(
                    normalizedHue,
                    saturation,
                    value
                )
            )
        }

        fun fromHex(hex: String): HsvColor {
            return if (hex.startsWith("#") || hex.startsWith("0x")) {
                fromComposeColor(hex.toComposeColor())
            } else {
                fromLong(hex.toLong(16))
            }
        }

        fun fromLong(value: Long): HsvColor {
            return HsvColor(
                hue = ((value and 0xFFFF0000L) shr 16).toInt(),
                saturation = ((value and 0x0000FF00L) shr 8).toFloat() / 255f,
                value = (value and 0x000000FFL).toFloat() / 255f
            )
        }
    }

    fun value(): Long {
        val hBits = hue.toLong() and 0xFFFFL
        val sBits = (saturation * 255f).roundToInt().toLong() and 0xFFL
        val vBits = (value * 255f).roundToInt().toLong() and 0xFFL
        return (hBits shl 16) or (sBits shl 8) or vBits
    }

    fun hex(): String {
        var v = value()
        val sb = StringBuilder()
        (0 until 8).forEach { _ ->
            sb.append(CHARS[(v and 0xF).toInt()])
            v = v shr 4
        }
        return sb.reverse().toString()
    }

    fun toComposeColor(): Color {
        val hh = hue
        val ss = saturation
        val vv = value

        val c = vv * ss
        val x = c * (1 - abs((hh / 60f) % 2 - 1))
        val m = vv - c
        val (r, g, b) = when {
            hh < 60 -> Triple(c, x, 0f)
            hh < 120 -> Triple(x, c, 0f)
            hh < 180 -> Triple(0f, c, x)
            hh < 240 -> Triple(0f, x, c)
            hh < 300 -> Triple(x, 0f, c)
            else -> Triple(c, 0f, x)
        }

        return Color(red = r + m, green = g + m, blue = b + m)
    }
}

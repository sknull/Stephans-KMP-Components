package de.visualdigits.common.domain.model

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color
import de.visualdigits.common.domain.util.toComposeColor
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.max
import kotlin.math.min
import kotlin.math.roundToInt

@Immutable
data class HsvColor(
    val hue: Int,
    val saturation: Double,
    val value: Double
) {

    private constructor(hsv: DoubleArray) : this(hsv[0].roundToInt(), hsv[1], hsv[2])

    companion object {

        private const val CHARS = "0123456789abcdef"

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
                saturation = ((value and 0x0000FF00L) shr 8).toDouble() / 255f,
                value = (value and 0x000000FFL).toDouble() / 255f
            )
        }

        fun fromComposeColor(color: Color): HsvColor {
            val r = color.red.toDouble()
            val g = color.green.toDouble()
            val b = color.blue.toDouble()
            val min = min(r, min(g, b))
            val max = max(r, max(g, b))
            val delta = max - min
            val s: Double
            var h: Double
            if (max == 0.0) {
                s = 0.0
                h = 0.0
            } else {
                s = delta / max
                h = if (r == max) {
                    (g - b) / delta
                } else if (g == max) {
                    2 + (b - r) / delta
                } else {
                    4 + (r - g) / delta
                }
                h *= 60.0
                if (h < 0) {
                    h += 360.0
                }
                if (java.lang.Double.isNaN(h)) {
                    h = 0.0
                }
            }
            return HsvColor(
                doubleArrayOf(
                    h,
                    s,
                    max
                )
            )
        }
    }

    fun clone(): HsvColor {
        return HsvColor(hue, saturation, value)
    }

    fun toComposeColor(): Color {
        val h = hue / 360.0
        val s = saturation
        val v = value
        val components = (if (s == 0.0) {
            listOf(v, v, v)
        } else {
            val varH = h * 6
            val varI = floor(varH)
            val var1 = v * (1 - s)
            val var2 = v * (1 - s * (varH - varI))
            val var3 = v * (1 - s * (1 - (varH - varI)))

            when (varI) {
                0.0 -> listOf(v, var3, var1)
                1.0 -> listOf(var2, v, var1)
                2.0 -> listOf(var1, v, var3)
                3.0 -> listOf(var1, var2, v)
                4.0 -> listOf(var3, var1, v)
                else -> listOf(v, var1, var2)
            }
        }).map { 255.coerceAtMost((it * 255.0).roundToInt()) }

        return Color(
            red = components[0],
            green = components[1],
            blue = components[2]
        )
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
}

fun Color.toHsvColor(): HsvColor = HsvColor.fromComposeColor(this)

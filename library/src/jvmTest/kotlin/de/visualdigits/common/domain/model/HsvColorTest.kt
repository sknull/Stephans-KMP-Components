package de.visualdigits.common.domain.model

import de.visualdigits.common.domain.model.color.HsvColor
import org.junit.Test
import org.junit.jupiter.api.Assertions.*

class HsvColorTest {

    @Test
    fun testColorConversion() {
        val color = HsvColor(
            hue = 0,
            saturation = 0.0,
            value = 1.0
        )

        val step = 0.01
        var v = 1.0 - step
        var rgbOld = color.toComposeColor()
        while (v >= 0.0) {
            val hsv = color.copy(value = v)
            val rgb = hsv.toComposeColor()
            val red = rgb.red
            val red1 = rgbOld.red
            val green = rgb.green
            val green1 = rgbOld.green
            val blue = rgb.blue
            val blue1 = rgbOld.blue
            assertTrue(red < red1 && green < green1 && blue < blue1)
            v -= step
            rgbOld = rgb
        }
    }
}

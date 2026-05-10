package de.visualdigits.common.domain.model.configuration.keyfactory

import androidx.compose.ui.graphics.Color
import de.visualdigits.common.domain.util.toComposeColor
import de.visualdigits.common.domain.util.toWebColor

class ColorKeyFactory {

    companion object : KeyFactory<Color> {

        override fun fromString(value: String?): Color?  = value?.toComposeColor()

        override fun fromValue(value: Any?): Color? {
            return when (value) {
                is String -> fromString(value)
                is Color -> value
                is Number -> Color(value.toInt())
                else -> null
            }
        }

        override fun stringValue(value: Any?): String? = (value as? Color)?.toWebColor()?:value?.toString()
    }
}

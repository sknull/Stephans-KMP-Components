package de.visualdigits.common.domain.model.configuration.keyfactory

import androidx.compose.ui.graphics.Color
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.util.toComposeColor
import de.visualdigits.common.domain.util.toWebColor
import org.jetbrains.compose.resources.DrawableResource

class ColorKeyFactory {

    companion object : KeyFactory<Color> {

        override val options: List<Triple<Color, UiText?, DrawableResource?>> = listOf()

        override fun fromString(value: String?): Color? {
            return value?.toComposeColor()
        }

        override fun fromValue(value: Any?): Color? {
            return when (value) {
                is String -> fromString(value)
                is Color -> value
                is Number -> Color(value.toInt())
                else -> null
            }
        }

        override fun stringValue(value: Any?): String {
            return when (value) {
                is String -> value
                is Color -> value.toWebColor()
                else -> value.toString()
            }
        }
    }
}

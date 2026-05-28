package de.visualdigits.common.domain.model.configuration.keyfactory

import androidx.compose.ui.graphics.Color
import de.visualdigits.common.domain.model.HsvColor
import de.visualdigits.common.domain.model.UiText
import org.jetbrains.compose.resources.DrawableResource

class ColorKeyFactory {

    companion object : KeyFactory<HsvColor> {

        override val options: List<Triple<HsvColor, UiText?, DrawableResource?>> = listOf()

        override fun fromString(value: String?): HsvColor? {
            return value?.let { v -> HsvColor.fromHex(v) }
        }

        override fun fromValue(value: Any?): HsvColor? {
            return when (value) {
                is String -> fromString(value)
                is Color -> HsvColor.fromComposeColor(value)
                is Number -> HsvColor.fromLong(value.toLong())
                is HsvColor -> value
                else -> null
            }
        }

        override fun stringValue(value: Any?): String {
            return when (value) {
                is String -> value
                is Color -> HsvColor.fromComposeColor(value).hex()
                is Number -> HsvColor.fromLong(value.toLong()).hex()
                is HsvColor -> value.hex()
                else -> value.toString()
            }
        }
    }
}

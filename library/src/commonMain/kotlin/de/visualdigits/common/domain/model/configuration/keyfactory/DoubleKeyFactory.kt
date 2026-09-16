package de.visualdigits.common.domain.model.configuration.keyfactory

import de.visualdigits.common.domain.model.ui.UiText
import org.jetbrains.compose.resources.DrawableResource

class DoubleKeyFactory {

    companion object : KeyFactory<Double> {

        override val options: List<Triple<Double, UiText?, DrawableResource?>> = listOf()

        override fun fromString(value: String?): Double?  = value?.toDouble()

        override fun fromValue(value: Any?): Double? {
            return when (value) {
                is String -> fromString(value)
                is Double -> value
                is Number -> value.toDouble()
                else -> null
            }
        }

        override fun stringValue(value: Any?): String? {
            return when (value) {
                is String -> value
                is Number -> value.toString()
                else -> null
            }
        }
    }
}

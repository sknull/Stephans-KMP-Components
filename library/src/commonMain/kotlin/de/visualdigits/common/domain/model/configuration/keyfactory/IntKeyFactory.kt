package de.visualdigits.common.domain.model.configuration.keyfactory

import de.visualdigits.common.domain.model.UiText
import org.jetbrains.compose.resources.DrawableResource
import java.io.File

class IntKeyFactory {

    companion object : KeyFactory<Int> {

        override val options: List<Triple<Int, UiText?, DrawableResource?>> = listOf()

        override fun fromString(value: String?): Int?  = value?.toInt()

        override fun fromValue(value: Any?): Int? {
            return when (value) {
                is String -> fromString(value)
                is Int -> value
                is Number -> value.toInt()
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

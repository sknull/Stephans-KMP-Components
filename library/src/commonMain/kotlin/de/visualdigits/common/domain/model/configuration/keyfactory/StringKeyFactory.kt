package de.visualdigits.common.domain.model.configuration.keyfactory

import de.visualdigits.common.domain.model.UiText
import org.jetbrains.compose.resources.DrawableResource
import java.io.File

class StringKeyFactory {

    companion object : KeyFactory<String> {

        override val options: List<Triple<String, UiText?, DrawableResource?>> = listOf()

        override fun fromString(value: String?): String?  = value

        override fun fromValue(value: Any?): String? {
            return when (value) {
                is String -> fromString(value)
                else -> value.toString()
            }
        }

        override fun stringValue(value: Any?): String? {
            return when (value) {
                is String -> fromString(value)
                else -> value.toString()
            }
        }
    }
}

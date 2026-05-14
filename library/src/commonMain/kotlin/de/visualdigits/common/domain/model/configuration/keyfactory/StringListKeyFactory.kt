package de.visualdigits.common.domain.model.configuration.keyfactory

import de.visualdigits.common.domain.model.UiText
import org.jetbrains.compose.resources.DrawableResource

class StringListKeyFactory {

    companion object : KeyFactory<MutableList<String>> {

        override val options: List<Triple<MutableList<String>, UiText?, DrawableResource?>> = listOf()

        override fun fromString(value: String?): MutableList<String> {
            return if (value?.isNotEmpty() == true) {
                value
                    .split(",")
                    .map { v -> v.trim() }
                    .toMutableList()
            } else {
                mutableListOf()
            }
        }

        override fun fromValue(value: Any?): MutableList<String>? {
            return when (value) {
                is String -> fromString(value)
                is List<*> -> value.map { e -> e.toString() }.toMutableList()
                else -> null
            }
        }

        override fun stringValue(value: Any?): String? {
            return when (value) {
                is String -> value
                is List<*> -> value.joinToString(",") { v -> v.toString() }
                else -> null
            }
        }
    }
}

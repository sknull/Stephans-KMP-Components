package de.visualdigits.common.domain.model.configuration.keyfactory

import de.visualdigits.common.domain.model.common.KmpOffsetDateTime
import de.visualdigits.common.domain.model.ui.UiText
import org.jetbrains.compose.resources.DrawableResource

class DateTimeKeyFactory {

    companion object : KeyFactory<KmpOffsetDateTime> {

        override val options: List<Triple<KmpOffsetDateTime, UiText?, DrawableResource?>> = listOf()

        override fun fromString(value: String?): KmpOffsetDateTime?  = value?.let { v -> KmpOffsetDateTime.fromString(v) }

        override fun fromValue(value: Any?): KmpOffsetDateTime? {
            return when (value) {
                is String -> fromString(value)
                is KmpOffsetDateTime -> value
                else -> null
            }
        }

        override fun stringValue(value: Any?): String? {
            return when (value) {
                is String -> value
                is KmpOffsetDateTime -> value.toString()
                else -> null
            }
        }
    }
}

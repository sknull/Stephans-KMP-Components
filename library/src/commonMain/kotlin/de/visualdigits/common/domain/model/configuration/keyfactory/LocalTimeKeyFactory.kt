package de.visualdigits.common.domain.model.configuration.keyfactory

import de.visualdigits.common.domain.model.ui.UiText
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.DrawableResource

class LocalTimeKeyFactory {

    companion object : KeyFactory<LocalTime> {

        override val options: List<Triple<LocalTime, UiText?, DrawableResource?>> = listOf()

        override fun fromString(value: String?): LocalTime?  = value?.let { v -> LocalTime.parse(v) }

        override fun fromValue(value: Any?): LocalTime? {
            return when (value) {
                is String -> fromString(value)
                is LocalTime -> value
                else -> null
            }
        }

        override fun stringValue(value: Any?): String? {
            return when (value) {
                is String -> value
                is LocalTime -> value.toString()
                else -> null
            }
        }
    }
}

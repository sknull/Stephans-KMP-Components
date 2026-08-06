package de.visualdigits.common.domain.model.configuration.keyfactory

import de.visualdigits.common.domain.model.ui.UiText
import kotlinx.datetime.LocalDate
import org.jetbrains.compose.resources.DrawableResource

class LocalDateKeyFactory {

    companion object : KeyFactory<LocalDate> {

        override val options: List<Triple<LocalDate, UiText?, DrawableResource?>> = listOf()

        override fun fromString(value: String?): LocalDate?  = value?.let { v -> LocalDate.parse(v) }

        override fun fromValue(value: Any?): LocalDate? {
            return when (value) {
                is String -> fromString(value)
                is LocalDate -> value
                else -> null
            }
        }

        override fun stringValue(value: Any?): String? {
            return when (value) {
                is String -> value
                is LocalDate -> value.toString()
                else -> null
            }
        }
    }
}

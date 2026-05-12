package de.visualdigits.common.demo.form

import de.visualdigits.common.domain.model.StringResourceEnumerable
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.keyfactory.KeyFactory
import org.jetbrains.compose.resources.DrawableResource
import stephans_kmp_components.composeapp.generated.resources.Res
import stephans_kmp_components.composeapp.generated.resources.flag_de
import stephans_kmp_components.composeapp.generated.resources.flag_en
import java.util.Locale

enum class Language(
    override val uiText: UiText,
    override val drawableResourceId: DrawableResource?,
    val locale: Locale
) : StringResourceEnumerable<Language> {

    DE(UiText.DynamicString("Deutsch"), Res.drawable.flag_de, Locale.GERMANY),
    EN(UiText.DynamicString("Englisch"), Res.drawable.flag_en, Locale.US),
    ;

    companion object : KeyFactory<Language> {

        override val options: List<Triple<Language, UiText?, DrawableResource?>> = entries.map { e -> Triple(e, e.uiText, e.drawableResourceId) }

        override fun fromString(value: String?): Language? {
            return entries.find { e -> e.name == value }
        }

        override fun fromValue(value: Any?): Language? {
            return when (value) {
                is String -> fromString(value)
                is Language -> value
                is Locale -> entries.find { e -> e.locale == value }
                else -> null
            }
        }

        override fun stringValue(value: Any?): String? {
            return when (value) {
                is String -> value
                is Language -> value.name
                is Locale -> entries.find { e -> e.locale == value }?.name
                else -> null
            }
        }
    }
}

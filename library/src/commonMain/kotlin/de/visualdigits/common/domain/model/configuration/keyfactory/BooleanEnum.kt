package de.visualdigits.common.domain.model.configuration.keyfactory

import de.visualdigits.common.domain.model.StringResourceEnumerable
import de.visualdigits.common.domain.model.UiText
import org.jetbrains.compose.resources.DrawableResource

enum class BooleanEnum(
    override val uiText: UiText,
    override val drawableResourceId: DrawableResource?,
    val booleanValue: Boolean
) : StringResourceEnumerable<BooleanEnum> {

    TRUE(UiText.DynamicString("true"),  null, true),
    FALSE(UiText.DynamicString("false"),  null, false),
    ;

    override fun toString(): String = name

    companion object : KeyFactory<BooleanEnum> {

        override val options: List<Triple<BooleanEnum, UiText?, DrawableResource?>> = entries.map { e -> Triple(e, e.uiText, e.drawableResourceId) }

        override fun fromString(value: String?): BooleanEnum? {
            return entries.find { e -> e.name.equals(value, ignoreCase = true) }
        }

        override fun fromValue(value: Any?): BooleanEnum? {
            val v = when (value) {
                is String -> fromString(value)
                is Boolean -> entries.find { e -> e.booleanValue == value }
                is BooleanEnum -> value
                else -> null
            }
            return v
        }

        override fun stringValue(value: Any?): String? {
            val v = when (value) {
                is String -> value
                is Boolean -> entries.find { e -> e.booleanValue == value }?.name
                is BooleanEnum -> value.name
                else -> null
            }
            return v
        }
    }
}

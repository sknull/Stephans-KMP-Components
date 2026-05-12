package de.visualdigits.common.domain.model.configuration.keyfactory

import de.visualdigits.common.domain.model.UiText
import org.jetbrains.compose.resources.DrawableResource

interface KeyFactory<T> {

    val options: List<Triple<T, UiText?, DrawableResource?>>

    fun fromString(value: String?): T?

    fun fromValue(value: Any?): T?

    fun stringValue(value: Any?): String?
}

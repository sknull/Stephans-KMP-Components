package de.visualdigits.common.domain.model.configuration.keyfactory

import de.visualdigits.common.domain.model.UiText
import org.jetbrains.compose.resources.DrawableResource
import java.io.File

class FileKeyFactory {

    companion object : KeyFactory<File> {

        override val options: List<Triple<File, UiText?, DrawableResource?>> = listOf()

        override fun fromString(value: String?): File?  = value?.let { v -> File(v) }

        override fun fromValue(value: Any?): File? {
            return when (value) {
                is String -> fromString(value)
                is File -> value
                else -> null
            }
        }

        override fun stringValue(value: Any?): String? {
            return when (value) {
                is String -> value
                is File -> value.canonicalPath
                else -> null
            }
        }
    }
}

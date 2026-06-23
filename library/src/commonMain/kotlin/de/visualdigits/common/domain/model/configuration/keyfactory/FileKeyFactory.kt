package de.visualdigits.common.domain.model.configuration.keyfactory

import de.visualdigits.common.domain.model.ui.UiText
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import org.jetbrains.compose.resources.DrawableResource

class FileKeyFactory {

    companion object : KeyFactory<Path> {

        override val options: List<Triple<Path, UiText?, DrawableResource?>> = listOf()

        override fun fromString(value: String?): Path?  = value?.let { v -> Path(v) }

        override fun fromValue(value: Any?): Path? {
            return when (value) {
                is String -> fromString(value)
                is Path -> value
                else -> null
            }
        }

        override fun stringValue(value: Any?): String? {
            return when (value) {
                is String -> value
                is Path -> SystemFileSystem.resolve(value).toString()
                else -> null
            }
        }
    }
}

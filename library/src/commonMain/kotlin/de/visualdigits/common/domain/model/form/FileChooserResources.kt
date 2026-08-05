package de.visualdigits.common.domain.model.form

import androidx.compose.runtime.Composable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter

data class FileChooserResources(
    val leadingIcon: @Composable (() -> Unit)? = null,
    val trailingIcon: @Composable (() -> Unit)? = null,
    val iconFolder: Painter,
    val iconTint: Color,
    val buttonColor: Color,
    val titleDirectories: String,
    val titleFiles: String,
)

val LocalFileChooserResources = staticCompositionLocalOf<FileChooserResources> {
    error("No FileChooserResources provided")
}

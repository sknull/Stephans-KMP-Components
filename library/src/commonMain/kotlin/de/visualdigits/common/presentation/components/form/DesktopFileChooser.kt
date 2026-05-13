package de.visualdigits.common.presentation.components.form

import de.visualdigits.common.domain.model.FileMode
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import org.jetbrains.compose.resources.DrawableResource
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter


fun <K : FieldKey<K>, FK : FieldKey<FK>> desktopFileChooser(
    fieldState: FieldState<K, FK>,
    title: String,
    fileMode: FileMode,
    startDirectory: File,
    onOk: (File) -> Unit
) {
    val mode = fileMode.jFileChooserMode
    val chooser = JFileChooser().apply {
        if (fileMode == FileMode.FILES_ONLY) {
            val filter =
                FileNameExtensionFilter(
                    fieldState.options.map { o -> o.first }
                        .joinToString(", ") { o -> "*.$o" },
                    *fieldState.options.map { o -> o.first.toString() }.toTypedArray()
                )
            this.fileFilter = filter
            this.isAcceptAllFileFilterUsed = false
        }
        currentDirectory = startDirectory
        fileSelectionMode = mode
        dialogTitle = title
    }
    val result = chooser.showOpenDialog(null)
    if (result == JFileChooser.APPROVE_OPTION) {
        onOk(chooser.selectedFile)
    } else {
        // nothing to do
    }
}

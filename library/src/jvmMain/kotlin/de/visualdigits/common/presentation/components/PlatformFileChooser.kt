package de.visualdigits.common.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import de.visualdigits.common.domain.model.ui.FileMode
import de.visualdigits.common.presentation.components.button.IndicatorButton
import kotlinx.io.Source
import kotlinx.io.asSource
import kotlinx.io.buffered
import kotlinx.io.files.Path
import java.io.File
import javax.swing.JFileChooser
import javax.swing.filechooser.FileNameExtensionFilter


@Composable
actual fun PlatformFileChooser(
    label: String?,
    buttonTextStyle: TextStyle,
    buttonTextAlign: TextAlign,
    title: String,
    fileMode: FileMode,
    options: List<String>,
    buttonShape: Shape,
    buttonColor: Color,
    buttonWidth: Dp,
    buttonHeight: Dp,
    leadingIcon: Painter?,
    leadingIconTint: Color,
    toolTip: String?,
    startDirectory: Path,
    onCancel: (() -> Unit)?,
    onOkSource: ((String, Source) -> Unit)?,
    onOkPath: ((Path) -> Unit)?
) {
    val mode = fileMode.jFileChooserMode
    val chooser = JFileChooser().apply {
        if (fileMode == FileMode.FILES_ONLY && options.isNotEmpty()) {
            val filter =
                FileNameExtensionFilter(
                    options
                        .joinToString(", ") { o -> "*.$o" },
                    *options.toTypedArray()
                )
            this.fileFilter = filter
            this.isAcceptAllFileFilterUsed = false
        } else {
            this.isAcceptAllFileFilterUsed = true
        }
        currentDirectory = File(startDirectory.toString())
        fileSelectionMode = mode
        dialogTitle = title
    }

    IndicatorButton(
        modifier = Modifier,
        width = buttonWidth,
        height = buttonHeight,
        text = label,
        textStyle = buttonTextStyle,
        textAlign = buttonTextAlign,
        buttonColor = buttonColor,
        shape = buttonShape,
        leadingIcon = leadingIcon,
        leadingIconTint = leadingIconTint,
        toolTip = toolTip,
    ) {
        val result = chooser.showOpenDialog(null)
        if (result == JFileChooser.APPROVE_OPTION) {
            val name = chooser.selectedFile.name
            val kmpSource = chooser.selectedFile.inputStream().asSource().buffered()
            onOkSource?.also { onOk -> onOk(name, kmpSource) }
            onOkPath?.also { onOk -> onOk(Path(chooser.selectedFile.canonicalPath)) }
        } else if (result == JFileChooser.CANCEL_OPTION) {
            onCancel?.also { oc -> oc() }
        }
    }
}

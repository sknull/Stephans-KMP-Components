package de.visualdigits.common.presentation.components

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import co.touchlab.kermit.Logger
import de.visualdigits.common.domain.model.ui.FileMode
import de.visualdigits.common.presentation.components.button.IndicatorButton
import kotlinx.io.Sink
import kotlinx.io.asSink
import kotlinx.io.buffered
import kotlinx.io.files.Path

@Composable
actual fun PlatformFileSaver(
    label: String?,
    labelSaveButton: String?,
    buttonTextStyle: TextStyle,
    buttonTextAlign: TextAlign,
    title: String,
    fileMode: FileMode,
    suggestedFileName: String,
    buttonShape: Shape,
    buttonColor: Color,
    buttonWidth: Dp,
    buttonHeight: Dp,
    leadingIcon: Painter?,
    leadingIconTint: Color,
    toolTip: String?,
    startDirectory: Path,
    onCancel: (() -> Unit)?,
    onOk: (String, Sink) -> Unit
) {
    val context = LocalContext.current
    val log = Logger.withTag("PlatformFileSaver")

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.CreateDocument("*/*")
    ) { uri: Uri? ->
        if (uri != null) {
            var fileName = "unknown"
            context.contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                val nameIndex = cursor.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                if (cursor.moveToFirst()) {
                    fileName = cursor.getString(nameIndex)
                }
                if (fileName == "unknown") {
                    fileName = uri.path?.substringAfterLast('/') ?: "file"
                }
            }
            try {
                val outs = context.contentResolver.openOutputStream(uri)
                outs?.asSink()?.buffered()?.also { buffer -> onOk(fileName, buffer) }
            } catch (e: Exception) {
                log.e("Could not save file", e)
            }
        } else {
            onCancel?.invoke()
        }
    }

    IndicatorButton(
        modifier = Modifier,
        width = buttonWidth,
        height = buttonHeight,
        text = label,
        textStyle = buttonTextStyle,
        textAlign = buttonTextAlign,
        shape = buttonShape,
        buttonColor = buttonColor,
        leadingIcon = leadingIcon,
        leadingIconTint = leadingIconTint,
        toolTip = toolTip,
    ) {
        launcher.launch(suggestedFileName)
    }
}

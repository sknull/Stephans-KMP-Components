package de.visualdigits.common.presentation.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.platform.ConnectivityMode
import de.visualdigits.common.domain.model.ui.FileMode
import de.visualdigits.common.domain.model.ui.UiPlatform
import de.visualdigits.common.presentation.model.CommonAction
import de.visualdigits.common.presentation.model.PlatformScrollbarStyle
import de.visualdigits.common.presentation.model.ScrollIntent
import de.visualdigits.common.presentation.model.defaultScrollbarStyle
import kotlinx.io.Sink
import kotlinx.io.Source
import kotlinx.io.files.Path

expect class ConnectivityManager {
    fun connectivityMode(): ConnectivityMode
}

expect fun applyAppLanguage(languageTag: String)

expect fun currentLanguageTag(): String

@Composable
expect fun BindBackHandler(isEnabled: Boolean, onBack: () -> Unit)

@Composable
expect fun PlatformVerticalScrollbarBox(
    modifier: Modifier = Modifier,
    space: Dp = 0.dp,
    backgroundColor: Color = Color.Unspecified,
    backgroundImage: (@Composable () -> Unit)? = null,
    scrollbarModifier: Modifier = Modifier,
    scrollbarStyle: PlatformScrollbarStyle = defaultScrollbarStyle(),
    scrollbarId: String? = null,
    scrollPosition: MutableMap<String, Triple<Int, Int?, ScrollIntent>> = mutableMapOf(),
    onCommonAction: ((CommonAction) -> Unit)? = null,
    verticalArrangementGap: Dp = 8.dp,
    rows: () -> List<Pair<String, @Composable () -> Unit>>
)

@Composable
expect fun PlatformVerticalScrollbar(
    modifier: Modifier = Modifier,
    style: PlatformScrollbarStyle = defaultScrollbarStyle(),
    scrollState: ScrollState,
    interactionSource: MutableInteractionSource
)

@Composable
expect fun PlatformLazyVerticalScrollbar(
    modifier: Modifier = Modifier,
    scrollState: LazyListState,
    interactionSource: MutableInteractionSource
)

@Composable
expect fun PlatformToolTip(
    modifier: Modifier = Modifier,
    text: String?,
    textStyle: TextStyle = MaterialTheme.typography.bodySmall,
    shadowSize: Dp = 5.dp,
    space: Dp,
    backgroundColor: Color,
    shape: Shape,
    content: @Composable () -> Unit
)

@Composable
expect fun androidPlatform(): UiPlatform

@Composable
expect fun Modifier.platformFocus(
    onClick: (() -> Unit)? = null
): Modifier


@Composable
expect fun PlatformFileChooser(
    label: String? = null,
    buttonTextStyle: TextStyle = MaterialTheme.typography.labelMedium,
    buttonTextAlign: TextAlign = TextAlign.Center,
    title: String,
    fileMode: FileMode,
    options: List<String> = listOf(),
    buttonShape: Shape = MaterialTheme.shapes.extraSmall,
    buttonColor: Color = MaterialTheme.colorScheme.surface,
    buttonWidth: Dp = 120.dp,
    buttonHeight: Dp = 50.dp,
    leadingIcon: Painter? = null,
    leadingIconTint: Color = MaterialTheme.colorScheme.onSurface,
    toolTip: String? = null,
    startDirectory: Path,
    onCancel: (() -> Unit)? = null,
    onOkSource: ((String, Source) -> Unit)? = null,
    onOkPath: ((Path) -> Unit)? = null
)

@Composable
expect fun PlatformFileSaver(
    label: String? = null,
    labelSaveButton: String? = null,
    buttonTextStyle: TextStyle = MaterialTheme.typography.labelMedium,
    buttonTextAlign: TextAlign = TextAlign.Center,
    title: String,
    fileMode: FileMode,
    options: List<String> = listOf(),
    suggestedFileName: String,
    buttonShape: Shape = MaterialTheme.shapes.extraSmall,
    buttonColor: Color = MaterialTheme.colorScheme.surface,
    buttonWidth: Dp = 120.dp,
    buttonHeight: Dp = 50.dp,
    leadingIcon: Painter? = null,
    leadingIconTint: Color = MaterialTheme.colorScheme.onSurface,
    toolTip: String? = null,
    startDirectory: Path,
    onCancel: (() -> Unit)? = null,
    onOk: (String, Sink) -> Unit
)

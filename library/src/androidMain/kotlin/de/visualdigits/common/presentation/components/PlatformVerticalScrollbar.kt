package de.visualdigits.common.presentation.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.visualdigits.common.presentation.model.PlatformScrollbarStyle

@Composable
actual fun PlatformVerticalScrollbar(
    modifier: Modifier,
    style: PlatformScrollbarStyle,
    adapter: PlatformScrollbarAdapter,
    interactionSource: MutableInteractionSource
) {
    // not supported in android
}

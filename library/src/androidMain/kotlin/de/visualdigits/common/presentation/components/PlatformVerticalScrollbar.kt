package de.visualdigits.common.presentation.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.visualdigits.common.presentation.model.PlatformScrollbarStyle

@Composable
actual fun PlatformVerticalScrollbar(
    modifier: Modifier,
    style: PlatformScrollbarStyle,
    scrollState: ScrollState,
    interactionSource: MutableInteractionSource
) {
    // not supported in android
}

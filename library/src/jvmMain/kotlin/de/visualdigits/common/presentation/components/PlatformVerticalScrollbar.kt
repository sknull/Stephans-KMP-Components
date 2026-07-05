package de.visualdigits.common.presentation.components

import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.visualdigits.common.presentation.model.PlatformScrollbarStyle

@Composable
actual fun PlatformVerticalScrollbar(
    modifier: Modifier,
    style: PlatformScrollbarStyle,
    lazyListState: LazyListState,
    interactionSource: MutableInteractionSource
) {
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(scrollState = lazyListState),
        modifier = modifier,
        style = ScrollbarStyle(
            minimalHeight = style.minimalHeight,
            thickness = style.thickness,
            shape = style.shape,
            hoverDurationMillis = style.hoverDurationMillis,
            unhoverColor = style.unhoverColor,
            hoverColor = style.hoverColor
        ),
        interactionSource = interactionSource
    )
}

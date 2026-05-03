package de.visualdigits.common.presentation.components

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.ScrollbarStyle
import androidx.compose.foundation.VerticalScrollbar
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.defaultScrollbarStyle
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.rememberScrollbarAdapter
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.visualdigits.common.presentation.model.PlatformScrollbarStyle

@Composable
actual fun PlatformVerticalScrollbar(
    modifier: Modifier,
    style: PlatformScrollbarStyle,
    scrollState: ScrollState,
    interactionSource: MutableInteractionSource
) {
    VerticalScrollbar(
        adapter = rememberScrollbarAdapter(scrollState = scrollState),
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

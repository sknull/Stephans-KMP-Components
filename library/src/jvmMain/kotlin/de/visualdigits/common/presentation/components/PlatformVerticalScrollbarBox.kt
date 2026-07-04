package de.visualdigits.common.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.visualdigits.common.presentation.model.CommonAction
import de.visualdigits.common.presentation.model.PlatformScrollbarStyle
import de.visualdigits.common.presentation.model.ScrollIntent
import kotlinx.coroutines.flow.collectLatest

@Composable
actual fun PlatformVerticalScrollbarBox(
    modifier: Modifier,
    space: Dp,
    backgroundColor: Color,
    backgroundImage: (@Composable () -> Unit)?,
    scrollbarModifier: Modifier,
    scrollbarStyle: PlatformScrollbarStyle,
    scrollbarId: String?,
    scrollPosition: MutableMap<String, Triple<Int, Int?, ScrollIntent>>,
    onCommonAction: ((CommonAction) -> Unit)?,
    verticalArrangementGap: Dp,
    rows: () -> List<Pair<String, @Composable () -> Unit>>
) {
    val interactionSource = remember { MutableInteractionSource() }

    // Start-Position sicher initialisieren
    val initialScroll = remember(scrollbarId) { scrollPosition[scrollbarId]?.first ?: 0 }
    val scrollState = rememberScrollState(initialScroll)

    val currentRows = rows()
    val cachedRows = remember(currentRows) {
        currentRows
    }

    LaunchedEffect(scrollbarId, scrollPosition[scrollbarId]) {
        val currentPosition = scrollPosition[scrollbarId] ?: return@LaunchedEffect
        if (currentPosition.third == ScrollIntent.scrollToStart) {
            scrollState.scrollTo(0)
        } else if (currentPosition.first != scrollState.value && !scrollState.isScrollInProgress) {
            scrollState.scrollTo(currentPosition.first)
        }
    }

    LaunchedEffect(scrollState, scrollbarId) {
        snapshotFlow { scrollState.value }
            .collectLatest { position ->
                onCommonAction?.invoke(
                    CommonAction.OnScrollPositionChange(scrollbarId, position, 0, ScrollIntent.standard)
                )
            }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        if (backgroundImage != null) {
            backgroundImage()
        }
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(backgroundColor)
                .padding(end = 10.dp + space)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(verticalArrangementGap)
        ) {
            cachedRows.forEach { row ->
                row.second()
            }
        }

        PlatformVerticalScrollbar(
            interactionSource = interactionSource,
            modifier = scrollbarModifier
                .fillMaxHeight()
                .width(10.dp)
                .align(Alignment.CenterEnd),
            style = scrollbarStyle,
            scrollState = scrollState
        )
    }
}

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.rememberScrollbarAdapter
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
import de.visualdigits.common.presentation.components.container.DesktopScrollbarAdapter
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
    forceLazy: Boolean,
    rows: () -> List<Pair<String, @Composable () -> Unit>>
) {
    if (forceLazy) {
        PlatformVerticalScrollbarBoxLazy(
            modifier = modifier,
            space = space,
            backgroundColor = backgroundColor,
            backgroundImage = backgroundImage,
            scrollbarModifier = scrollbarModifier,
            scrollbarStyle =scrollbarStyle,
            scrollbarId = scrollbarId,
            scrollPosition = scrollPosition,
            onCommonAction = onCommonAction,
            verticalArrangementGap = verticalArrangementGap,
            rows = rows
        )
    } else {
        PlatformVerticalScrollbarBoxUnlazy(
            modifier = modifier,
            space = space,
            backgroundColor = backgroundColor,
            backgroundImage = backgroundImage,
            scrollbarModifier = scrollbarModifier,
            scrollbarStyle =scrollbarStyle,
            scrollbarId = scrollbarId,
            scrollPosition = scrollPosition,
            onCommonAction = onCommonAction,
            verticalArrangementGap = verticalArrangementGap,
            rows = rows
        )
    }
}

@Composable
private fun PlatformVerticalScrollbarBoxUnlazy(
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
    val currentRows = rows()

    if (currentRows.isNotEmpty()) {
        val interactionSource = remember { MutableInteractionSource() }

        val initialScroll = remember(scrollbarId) { scrollPosition[scrollbarId]?.first ?: 0 }
        val scrollState = rememberScrollState(initialScroll)

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

            val adapter = DesktopScrollbarAdapter(rememberScrollbarAdapter(scrollState))
            PlatformVerticalScrollbar(
                interactionSource = interactionSource,
                modifier = scrollbarModifier
                    .fillMaxHeight()
                    .width(10.dp)
                    .align(Alignment.CenterEnd),
                style = scrollbarStyle,
                adapter = adapter
            )
        }
    } else {
        Box(Modifier.fillMaxSize())
    }
}

@Composable
private fun PlatformVerticalScrollbarBoxLazy(
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
    val currentRows = rows()

    if (currentRows.isNotEmpty()) {
        val interactionSource = remember { MutableInteractionSource() }

        val initialIndex = remember(scrollbarId) { scrollPosition[scrollbarId]?.first ?: 0 }
        val initialOffset = remember(scrollbarId) { scrollPosition[scrollbarId]?.second ?: 0 }

        val lazyListState = rememberLazyListState(
            initialFirstVisibleItemIndex = initialIndex,
            initialFirstVisibleItemScrollOffset = initialOffset
        )

        LaunchedEffect(scrollbarId, scrollPosition[scrollbarId]) {
            val currentPosition = scrollPosition[scrollbarId] ?: return@LaunchedEffect
            if (currentPosition.third == ScrollIntent.scrollToStart) {
                lazyListState.scrollToItem(0, 0)
            } else if (!lazyListState.isScrollInProgress) {
                if (currentPosition.first != lazyListState.firstVisibleItemIndex ||
                    currentPosition.second != lazyListState.firstVisibleItemScrollOffset) {
                    lazyListState.scrollToItem(currentPosition.first, currentPosition.second ?: 0)
                }
            }
        }

        LaunchedEffect(lazyListState, scrollbarId) {
            snapshotFlow {
                Pair(lazyListState.firstVisibleItemIndex, lazyListState.firstVisibleItemScrollOffset)
            }.collectLatest { (index, offset) ->
                onCommonAction?.invoke(
                    CommonAction.OnScrollPositionChange(scrollbarId, index, offset, ScrollIntent.standard)
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
            LazyColumn(
                modifier = modifier
                    .fillMaxWidth()
                    .background(backgroundColor)
                    .padding(end = 10.dp + space),
                verticalArrangement = Arrangement.spacedBy(verticalArrangementGap),
                state = lazyListState
            ) {
                items(
                    items = currentRows,
                    key = { row -> row.first }
                ) {(_, rowContent) ->
                    rowContent()
                }
            }

            val adapter = DesktopScrollbarAdapter(rememberScrollbarAdapter(lazyListState))
            PlatformVerticalScrollbar(
                interactionSource = interactionSource,
                modifier = scrollbarModifier
                    .fillMaxHeight()
                    .width(10.dp)
                    .align(Alignment.CenterEnd),
                style = scrollbarStyle,
                adapter = adapter
            )
        }
    } else {
        Box(Modifier.fillMaxSize())
    }
}

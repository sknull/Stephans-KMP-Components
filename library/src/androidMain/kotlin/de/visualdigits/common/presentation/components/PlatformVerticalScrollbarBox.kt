package de.visualdigits.common.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
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
    val currentRows = rows()

    if (currentRows.isNotEmpty()) {
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

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainer),
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
    } else {
        Box(Modifier.fillMaxSize())
    }
}

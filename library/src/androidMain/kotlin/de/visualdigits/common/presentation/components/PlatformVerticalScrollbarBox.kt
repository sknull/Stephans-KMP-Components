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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.errorhandling.LogMessage.Companion.log
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
    val items = rows()

    if (items.isNotEmpty()) {
        val lazyListState = rememberLazyListState(
            initialFirstVisibleItemIndex = scrollPosition[scrollbarId]?.first?:0,
            initialFirstVisibleItemScrollOffset = scrollPosition[scrollbarId]?.second?:0
        )

        LaunchedEffect(lazyListState) {
            if (scrollbarId != null && onCommonAction != null) {
                snapshotFlow {
                    lazyListState.firstVisibleItemIndex to lazyListState.firstVisibleItemScrollOffset
                }.collectLatest { (index, offset) ->
                    if (scrollPosition[scrollbarId]?.third == ScrollIntent.scrollToStart) {
                        lazyListState.scrollToItem(0, 0)
                        onCommonAction(CommonAction.OnScrollPositionChange(scrollbarId, 0, 0, ScrollIntent.standard))
                    } else {
                        onCommonAction(CommonAction.OnScrollPositionChange(scrollbarId, index, offset, ScrollIntent.standard))
                    }
                }
            }
        }
        LaunchedEffect(scrollPosition[scrollbarId]) {
            val current = scrollPosition[scrollbarId]
            if (current?.third == ScrollIntent.scrollToStart) {
                lazyListState.scrollToItem(0, 0)
                onCommonAction?.invoke(CommonAction.OnScrollPositionChange(scrollbarId, 0, 0, ScrollIntent.standard))
            } else {
                onCommonAction?.invoke(CommonAction.OnScrollPositionChange(scrollbarId, current?.first?:0, current?.second, ScrollIntent.standard))
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
                items = items,
                key = { row -> row.first }
            ) {(_, rowContent) ->
                rowContent()
            }
        }
    } else {
        Box(Modifier.fillMaxSize())
    }
}

package de.visualdigits.common.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.platform.PlatformType
import de.visualdigits.common.presentation.model.CommonAction
import de.visualdigits.common.presentation.model.PlatformScrollbarStyle
import de.visualdigits.common.presentation.model.ScrollIntent
import de.visualdigits.common.presentation.model.defaultScrollbarStyle
import kotlinx.coroutines.flow.collectLatest

@Composable
fun PlatformVerticalScrollbarBox(
    modifier: Modifier = Modifier,
    platformType: PlatformType,
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
                    .padding(end = if (platformType == PlatformType.jvm) 10.dp + space else 0.dp),
                verticalArrangement = Arrangement.spacedBy(verticalArrangementGap),
                state = lazyListState
            ) {
                items(
                    items = currentRows,
                    key = { row -> row.first }
                ) { (_, rowContent) ->
                    rowContent()
                }
            }

            PlatformVerticalScrollbar(
                interactionSource = interactionSource,
                modifier = scrollbarModifier
                    .fillMaxHeight()
                    .width(10.dp)
                    .align(Alignment.CenterEnd),
                style = scrollbarStyle,
                lazyListState = lazyListState
            )
        }
    } else {
        Box(Modifier.fillMaxSize())
    }
}

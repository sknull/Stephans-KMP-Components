package de.visualdigits.common.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import de.visualdigits.common.presentation.model.CommonAction
import de.visualdigits.common.presentation.model.PlatformScrollbarStyle
import kotlinx.coroutines.flow.collectLatest

@Composable
actual fun PlatformVerticalScrollbarBox(
    modifier: Modifier,
    backgroundColor: Color,
    scrollbarModifier: Modifier,
    scrollbarStyle: PlatformScrollbarStyle,
    scrollbarId: String,
    scrollPosition: MutableMap<String, Pair<Int, Int?>>,
    onCommonAction: (CommonAction) -> Unit,
    space: Dp,
    scrollToTop: (@Composable (LazyListState) -> Unit)?,
    rows: () -> List<Pair<String, @Composable () -> Unit>>
) {
    val items = rows()

    if (items.isNotEmpty()) {
        val lazyListState = rememberLazyListState(
            initialFirstVisibleItemIndex = scrollPosition[scrollbarId]?.first?:0,
            initialFirstVisibleItemScrollOffset = scrollPosition[scrollbarId]?.second?:0
        )

        scrollToTop?.let { st -> st(lazyListState) }

        LaunchedEffect(lazyListState) {
            snapshotFlow {
                lazyListState.firstVisibleItemIndex to lazyListState.firstVisibleItemScrollOffset
            }.collectLatest { (index, offset) ->
                onCommonAction(CommonAction.OnScrollPositionChange(scrollbarId, index, offset))
            }
        }

        LazyColumn(
            modifier = modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surfaceContainerLow)
                .padding(space),
            verticalArrangement = Arrangement.spacedBy(space),
            state = lazyListState
        ) {
            items(
                items = rows(),
                key = { row -> row.first }
            ) {(_, rowContent) ->
                rowContent()
            }
        }
    } else {
        Box(Modifier.fillMaxSize())
    }
}

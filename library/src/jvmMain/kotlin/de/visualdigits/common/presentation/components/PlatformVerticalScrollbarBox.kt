package de.visualdigits.common.presentation.components

import androidx.compose.foundation.ScrollState
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
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.visualdigits.common.presentation.model.CommonAction
import de.visualdigits.common.presentation.model.PlatformScrollbarStyle
import de.visualdigits.common.presentation.model.ScrollIntent

@Composable
actual fun PlatformVerticalScrollbarBox(
    modifier: Modifier,
    backgroundColor: Color,
    backgroundImage: (@Composable () -> Unit)?,
    scrollbarModifier: Modifier,
    scrollbarStyle: PlatformScrollbarStyle,
    scrollbarId: String?,
    scrollPosition: MutableMap<String, Triple<Int, Int?, ScrollIntent>>,
    onCommonAction: ((CommonAction) -> Unit)?,
    verticalArrangementGap: Dp,
    scrollToTop: (@Composable (ScrollState, ScrollIntent?) -> Unit)?,
    scrollToTopLazy: (@Composable (LazyListState, ScrollIntent?) -> Unit)?,
    rows: () -> List<Pair<String, @Composable () -> Unit>>
) {
    val interactionSource = remember { MutableInteractionSource() }

    val scrollState = rememberScrollState(scrollPosition[scrollbarId]?.first?:0)
    LaunchedEffect(scrollState.value) {
        if (scrollbarId != null && onCommonAction != null) {
            onCommonAction(CommonAction.OnScrollPositionChange(scrollbarId, scrollState.value))
        }
    }
    LaunchedEffect(scrollPosition[scrollbarId]) {
        if (scrollPosition[scrollbarId]?.third == ScrollIntent.scrollToStart) {
            scrollState.animateScrollTo(0)
        }
    }
    scrollToTop?.let { st -> st(scrollState, scrollPosition[scrollbarId]?.third) }

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
                .padding(end = 10.dp)
                .verticalScroll(scrollState),
            verticalArrangement = Arrangement.spacedBy(verticalArrangementGap)
        ) {
            rows().forEach { row ->
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

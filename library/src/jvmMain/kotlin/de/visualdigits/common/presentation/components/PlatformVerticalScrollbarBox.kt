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
    val interactionSource = remember { MutableInteractionSource() }
    val scrollState = rememberScrollState(scrollPosition[scrollbarId]?.first?:0)
    val currentRows = rows()
    val cachedRows = remember(currentRows) {
        currentRows.ifEmpty { null }
    } ?: currentRows

    // 3. Nur EIN zentraler Effekt für die Synchronisation von AUßEN nach INNEN
    LaunchedEffect(scrollbarId, scrollPosition[scrollbarId]) {
        snapshotFlow { scrollPosition[scrollbarId] }
            .collectLatest { scrollPosition ->
                if (scrollPosition != null) {
                    if (scrollPosition.third == ScrollIntent.scrollToStart) {
                        log(Severity.Info, "LaunchedEffect 1 TOP")
                        scrollState.scrollTo(0)
                    } else if (scrollPosition.first > 0) {
                        log(Severity.Info, "LaunchedEffect 1 - ${scrollPosition.first}")
                        // Stellt die Position beim ersten Laden oder nach Wiederherstellung her
                        scrollState.scrollTo(scrollPosition.first)
                    }
                }
            }
    }

    // 4. Nur EIN Effekt, um Änderungen von INNEN nach AUßEN zu melden (Debounced über snapshotFlow)
    LaunchedEffect(scrollbarId, scrollState) {
        snapshotFlow { scrollState.value }
            .collectLatest { position ->
                // Verhindere das Zurückmelden, wenn gerade ein 'scrollToStart' erzwungen wird
                if (scrollPosition[scrollbarId]?.third != ScrollIntent.scrollToStart && position > 0) {
                    log(Severity.Info, "LaunchedEffect 2 - $scrollbarId $position")
                    onCommonAction?.invoke(CommonAction.OnScrollPositionChange(scrollbarId, position, 0, ScrollIntent.standard))
                }
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

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

    // Start-Position sicher initialisieren
    val initialScroll = remember(scrollbarId) { scrollPosition[scrollbarId]?.first ?: 0 }
    val scrollState = rememberScrollState(initialScroll)

    // Korrektes Caching der Zeilen-Inhalte über den Inhalt, nicht über die Listen-Referenz
    val currentRows = rows()
    val cachedRows = remember(currentRows) {
        currentRows
    }

    // 1. Von AUßEN nach INNEN (Programmatisches Scrollen)
    LaunchedEffect(scrollbarId, scrollPosition[scrollbarId]) {
        val currentPosition = scrollPosition[scrollbarId] ?: return@LaunchedEffect

        if (currentPosition.third == ScrollIntent.scrollToStart) {
            log(Severity.Info, "ScrollToStart erzwungen für $scrollbarId")
            scrollState.scrollTo(0)
        } else if (currentPosition.first != scrollState.value && !scrollState.isScrollInProgress) {
            // Nur scrollen, wenn der Wert sich unterscheidet und der Nutzer NICHT selbst scrollt
            scrollState.scrollTo(currentPosition.first)
        }
    }

    // 2. Von INNEN nach AUßEN (Nur bei aktiver Nutzer-Interaktion!)
    LaunchedEffect(scrollState, scrollbarId) {
        snapshotFlow { Pair(scrollState.value, scrollState.isScrollInProgress) }
            .collectLatest { (position, isMoving) ->
                // WICHTIG: Nur melden, wenn der Nutzer die Hand am Rad/Maus hat!
                if (isMoving && scrollPosition[scrollbarId]?.third != ScrollIntent.scrollToStart) {
                    onCommonAction?.invoke(
                        CommonAction.OnScrollPositionChange(scrollbarId, position, 0, ScrollIntent.standard)
                    )
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

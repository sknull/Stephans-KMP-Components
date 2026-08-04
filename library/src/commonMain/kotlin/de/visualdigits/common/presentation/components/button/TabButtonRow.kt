package de.visualdigits.common.presentation.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun TabButtonRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    items: LinkedHashMap<String, @Composable () -> Unit>,
    selectedTab: () -> Int,
    loadingContent: (@Composable () -> Unit)? = null,
    button: @Composable (Int) -> Unit
) {
    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        items.keys.forEachIndexed { index, label ->
            button(index)
        }
    }

    loadingContent
        ?.let { lc -> lc() }
        ?: items.toList()[selectedTab()].second()
}

@Composable
fun TabButtonRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    verticalArrangement: Arrangement.Vertical,
    initializeViewModel: (() -> Unit)? = null,
    items: LinkedHashMap<Pair<String, (@Composable () -> Unit)?>, @Composable () -> Unit>,
    selectedTab: () -> Int,
    button: @Composable ((@Composable () -> Unit)?, String, Int) -> Unit
) {
    if (initializeViewModel != null) {
        initializeViewModel()
    }

    FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement,
        verticalArrangement = verticalArrangement
    ) {
        items.keys.forEachIndexed { index, entry ->
            button(entry.second,entry.first, index)
        }
    }

    items.toList()[selectedTab()].second()
}

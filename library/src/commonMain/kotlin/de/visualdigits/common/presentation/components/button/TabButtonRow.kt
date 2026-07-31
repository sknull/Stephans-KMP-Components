package de.visualdigits.common.presentation.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.visualdigits.common.domain.model.ui.UiText

@Composable
fun TabButtonRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    initializeViewModel: (() -> Unit)? = null,
    items: LinkedHashMap<String, @Composable () -> Unit>,
    selectedTab: () -> Int,
    button: @Composable (Int) -> Unit
) {
    if (initializeViewModel != null) {
        initializeViewModel()
    }

    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        items.keys.forEachIndexed { index, label ->
            button(index)
        }
    }

    items.toList()[selectedTab()].second()
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

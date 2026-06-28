package de.visualdigits.common.presentation.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.visualdigits.common.domain.model.ui.UiText

@Composable
fun TabButtonRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    initializeViewModel: (() -> Unit)? = null,
    items: LinkedHashMap<Pair<String, UiText>, @Composable () -> Unit>,
    selectedTab: () -> Int,
    button: @Composable (UiText, Int) -> Unit
) {
    if (initializeViewModel != null) {
        initializeViewModel()
    }

    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        items.keys.forEachIndexed { index, label ->
            button(label.second, index)
        }
    }

    items.toList()[selectedTab()].second()
}

@Composable
fun TabButtonRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    initializeViewModel: (() -> Unit)? = null,
    items: LinkedHashMap<Triple<String, (@Composable () -> Unit)?, UiText>, @Composable () -> Unit>,
    selectedTab: () -> Int,
    button: @Composable ((@Composable () -> Unit)?, UiText, Int) -> Unit
) {
    if (initializeViewModel != null) {
        initializeViewModel()
    }

    Row(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        items.keys.forEachIndexed { index, entry ->
            button(entry.second,entry.third, index)
        }
    }

    items.toList()[selectedTab()].second()
}

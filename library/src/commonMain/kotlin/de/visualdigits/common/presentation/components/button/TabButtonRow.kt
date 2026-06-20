package de.visualdigits.common.presentation.components.button

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.visualdigits.common.domain.model.UiText

@Composable
fun TabButtonRow(
    modifier: Modifier = Modifier,
    horizontalArrangement: Arrangement.Horizontal,
    items: LinkedHashMap<Pair<String, UiText>, @Composable () -> Unit>,
    selectedTab: () -> Int,
    button: @Composable (UiText, Int) -> Unit
) {
    FlowRow(
        modifier = modifier,
        horizontalArrangement = horizontalArrangement
    ) {
        items.keys.forEachIndexed { index, label ->
            button(label.second, index)
        }
    }

    items.toList()[selectedTab()].second()
}

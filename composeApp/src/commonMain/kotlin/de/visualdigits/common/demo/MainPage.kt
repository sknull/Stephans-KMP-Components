package de.visualdigits.common.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.visualdigits.common.demo.buttons.ButtonDemo
import de.visualdigits.common.demo.misc.StudioClockDemo
import de.visualdigits.common.presentation.components.button.IndicatorButton
import de.visualdigits.common.presentation.components.button.TabButtonRow
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.tab_buttondemo
import de.visualdigits.compose.resources.tab_studio_clock
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun MainPage() {

    val items = linkedMapOf<StringResource, @Composable (() -> Unit)>(
        Res.string.tab_studio_clock to {
            StudioClockDemo()
        },
        Res.string.tab_buttondemo to {
            ButtonDemo()
        },
    )

    var selectedTabIndex by remember { mutableStateOf(0) }

    Column(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TabButtonRow(
            modifier = Modifier
                .fillMaxWidth()
                .drawBehind() {
                    val strokeWidth = 2.dp.toPx()
                    drawLine(
                        color = Color(0xaa111111),
                        start = Offset(0f, size.height - strokeWidth / 2),
                        end = Offset(size.width, size.height - strokeWidth / 2),
                        strokeWidth = strokeWidth
                    )
                },
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            selectedTab = { selectedTabIndex },
            initializeViewModel = {},
            items = items
        ) { label, index ->
            IndicatorButton(
                flatLook = false,
                buttonColor = Color.Black,
                textColor = Color.White,
                text = stringResource(label),
                indicatorPosition = Alignment.BottomCenter,
                indicatorColor = Color(0xFFFF1B55),
                shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
                selected = selectedTabIndex == index,
            ) {
                selectedTabIndex = index
            }
        }
    }
}

package de.visualdigits.common.demo

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
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
import de.visualdigits.common.demo.form.FormDemo
import de.visualdigits.common.demo.misc.StudioClockDemo
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.presentation.components.button.IndicatorButton
import de.visualdigits.common.presentation.components.button.TabButtonRow
import de.visualdigits.common.presentation.components.container.ErrorCard

@Composable
fun MainPage() {

    var state by remember { mutableStateOf(DemoState()) }

    val items = linkedMapOf<UiText, @Composable (() -> Unit)>(
        UiText.DynamicString("Studio Clock") to {
            StudioClockDemo()
        },
        UiText.DynamicString("Button Demo") to {
            ButtonDemo()
        },
        UiText.DynamicString("Form Demo") to {
            FormDemo(
                state = state,
                setState = { newState ->
                    state = newState
                }
            )
        },
    )

    var selectedTabIndex by remember { mutableStateOf(0) }

    MaterialTheme(
        colorScheme = darkColorScheme(
            primary = Color(0xFF3C3F40),
            onPrimary = Color(0xFFFFFFFF),
            onPrimaryContainer = Color(0xFFFFFFFF),

            secondary = Color(0xFF313030),
            onSecondary = Color(0xFFFFFFFF),

            secondaryContainer = Color(0xFFE1E1E1),
            onSecondaryContainer = Color(0xFF9A9A9A),

            background = Color(0xFF3C3F40),
            onBackground = Color(0xFFFFFFFF),

            surface = Color.Transparent,
            onSurface = Color(0xFF439DDE), // deco color

            inverseSurface = Color(0xFFFFFFFF),
            surfaceContainer = Color(0xFFFFFFFF),
            surfaceContainerHigh = Color.Transparent,
            surfaceContainerLow = Color.Transparent,
            surfaceContainerLowest = Color(0xFF373737),
            surfaceDim = Color(0xFF393939),

            error = Color(0xffff002a),
            onError = Color(0xFFFFFFFF),
            errorContainer = Color(0xffff002a),
            onErrorContainer = Color(0xFFFFFFFF),

            outline = Color(0xFFFFFFFF),

            primaryFixed = Color(0xAA000000),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            ErrorCard(
                errorMessage = state.uiMessage,
                severity = state.uiMessageSeverity,
                shapeContainer = MaterialTheme.shapes.small
            )

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
                    text = label.asString(),
                    indicatorPosition = Alignment.BottomCenter,
                    indicatorColor = Color(0xFFFF1B55),
                    shape = RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp, bottomStart = 0.dp, bottomEnd = 0.dp),
                    selected = selectedTabIndex == index,
                ) {
                    selectedTabIndex = index
                    state = state.copy(
                        editedConfiguration = if(index == 2) state.configuration else null,
                        uiMessage = null,
                        uiMessageSeverity = null
                    )
                }
            }
        }
    }
}

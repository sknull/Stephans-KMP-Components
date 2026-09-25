package de.visualdigits.common.presentation.components.container

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import de.visualdigits.common.domain.model.errorhandling.LogMessage
import de.visualdigits.common.domain.model.platform.PlatformType
import de.visualdigits.common.domain.util.color
import de.visualdigits.common.presentation.components.PlatformVerticalScrollbarBox
import de.visualdigits.common.presentation.model.CommonAction
import de.visualdigits.common.presentation.model.ScrollIntent


@Composable
fun TerminalWindow(
    modifier: Modifier = Modifier,
    platformType: PlatformType,
    terminalId: String,
    scrollPosition: MutableMap<String, Triple<Int, Int?, ScrollIntent>> = mutableMapOf(),
    space: Dp = 8.dp,
    shapeContainer: Shape = MaterialTheme.shapes.small,
    title: String,
    titleBarColor: Color = Color.White,
    backGroundColor: Color = MaterialTheme.colorScheme.primaryFixed,
    messages: () -> List<LogMessage>,
    onCommonAction: ((CommonAction) -> Unit)? = null
) {
    val listState = rememberLazyListState()

    LaunchedEffect(messages().size) {
        if (messages().isNotEmpty()) {
            listState.scrollToItem(messages().size - 1)
        }
    }

    Column(
        modifier = modifier
            .clip(shapeContainer)
            .fillMaxSize(),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(30.dp)
                .background(titleBarColor),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = space),
                text = title,
                color = Color.Black,
                style = MaterialTheme.typography.titleSmall,
            )
        }

        PlatformVerticalScrollbarBox(
            modifier = Modifier
                .fillMaxSize()
                .background(backGroundColor)
                .padding(end = if (platformType == PlatformType.jvm) 20.dp else 0.dp),
            scrollbarModifier = Modifier
                .clip(MaterialTheme.shapes.small)
                .width(10.dp)
                .border(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)),
            platformType = platformType,
            scrollbarId = "terminal_$terminalId",
            scrollPosition = scrollPosition,
            onCommonAction = onCommonAction
        ) {
            val logMessages = messages()
            if (logMessages.isNotEmpty()) {
                logMessages.map { message ->
                    Pair("message_${message.id}", @Composable {
                        Text(
                            text = message.toString(),
                            color = message.severity.color(),
                            fontFamily = FontFamily.Monospace,
                            style = MaterialTheme.typography.bodySmall,
                            lineHeight = 1.5.em,
                            softWrap = false
                        )
                    })
                }
            } else {
                listOf(Pair("message_dummy", @Composable {
                    Box(modifier = Modifier.fillMaxSize())
                }))
            }
        }
    }
}

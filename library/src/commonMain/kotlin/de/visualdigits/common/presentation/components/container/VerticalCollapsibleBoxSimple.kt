package de.visualdigits.common.presentation.components.container

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.visualdigits.common.presentation.components.util.conditional

@Composable
fun VerticalCollapsibleBoxSimple(
    modifier: Modifier = Modifier,
    modifierHeader: Modifier = Modifier,
    modifierContent: Modifier = Modifier,
    iconArrowRight: Painter? = null,
    iconArrowDown: Painter? = null,
    space: Dp = 8.dp,
    paddingContainer: PaddingValues = PaddingValues(bottom = 8.dp),
    title: String? = null,
    titleContent: (@Composable () -> Unit)? = null,
    isTitleHoverable: Boolean = false,
    titleHoverColor: Color = Color.Transparent,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    shape: Shape = MaterialTheme.shapes.extraSmall,
    onStateChange: (Boolean) -> Unit,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    isExpanded: Boolean,
    trailingIcon: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    Column(
        modifier = modifier
            .clip(shape)
            .fillMaxWidth()
            .background(backgroundColor),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // header row
        Row(
            modifier = modifierHeader
                .fillMaxWidth()
                .height(30.dp)
                .conditional(isTitleHoverable) { hoverable(interactionSource = interactionSource) }
                .conditional(isTitleHoverable) { pointerHoverIcon(PointerIcon.Hand) }
                .conditional(isHovered) { background(titleHoverColor) }
                .pointerHoverIcon(PointerIcon.Hand)
                .clickable {
                    onStateChange(!isExpanded)
                },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .weight(1f)
            ) {
                if (title != null) {
                    Text(
                        modifier = Modifier
                            .padding(space),
                        text = title,
                        style = MaterialTheme.typography.titleSmall
                    )
                } else if (titleContent != null) {
                    titleContent()
                }
            }

            Box(
                modifier = Modifier
                    .width(40.dp),
                contentAlignment = Alignment.Center
            ) {
                if (isExpanded && iconArrowDown != null) {
                    Icon(
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .padding(space),
                        painter = iconArrowDown,
                        contentDescription = null,
                        tint = iconTint
                    )
                } else if (iconArrowRight != null){
                    Icon(
                        modifier = Modifier
                            .width(30.dp)
                            .height(30.dp)
                            .padding(space),
                        painter = iconArrowRight,
                        contentDescription = null,
                        tint = iconTint
                    )
                }
            }

            trailingIcon?.let { ti ->
                Box(
                    modifier = Modifier
                ) {
                    ti()
                }
            }
        }

        if (isExpanded) {
            Box(
                modifier = modifierContent
                    .padding(paddingContainer),
            ) {
                content()
            }
        }
    }
}

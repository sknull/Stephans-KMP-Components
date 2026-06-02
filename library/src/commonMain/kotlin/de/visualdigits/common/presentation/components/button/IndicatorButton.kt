package de.visualdigits.common.presentation.components.button

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import de.visualdigits.common.presentation.components.PlatformToolTip
import de.visualdigits.common.presentation.components.modifier.indicator
import de.visualdigits.common.presentation.components.modifier.ledRing
import de.visualdigits.common.presentation.components.platformFocus
import de.visualdigits.common.presentation.components.util.conditional

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun IndicatorButton(
    modifier: Modifier = Modifier,
    isHoverable: Boolean = true,
    space: Dp = 8.dp,
    text: String? = null,
    maxLines: Int = 1,
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    textColorDisabled: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    textAlign: TextAlign = TextAlign.Center,
    toolTip: String? = null,
    width: Dp = 160.dp,
    height: Dp = 50.dp,
    padding: Dp = 5.dp,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    buttonColor: Color? = MaterialTheme.colorScheme.surface,
    indicatorPosition: Alignment? = null,
    indicatorColor: Color? = null,
    flatLook: Boolean = true,
    horizontalColors: List<Color>? = null,
    hoverColor: Color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
    shape: Shape = MaterialTheme.shapes.extraSmall,
    selected: Boolean = false,
    leadingImage: (@Composable () -> Unit)? = null,
    leadingIcon: Painter? = null,
    leadingIconTint: Color = MaterialTheme.colorScheme.onSurface,
    leadingIconTintDisabled: Color = leadingIconTint.copy(alpha = 0.5f),
    trailingIcon: Painter? = null,
    trailingIconTint: Color = MaterialTheme.colorScheme.onSurface,
    trailingIconTintDisabled: Color = trailingIconTint.copy(alpha = 0.5f),
    enabled: Boolean = true,
    onClick: (() -> Unit)? = null,
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    var indicatorModifier = modifier
        .semantics { role = Role.Button }
        .width(width)
        .height(height)
    if (enabled) {
        indicatorModifier = indicatorModifier
            .conditional(isHoverable) { hoverable(interactionSource = interactionSource) }
            .conditional(isHoverable) { pointerHoverIcon(PointerIcon.Hand) }
            .platformFocus(onClick)
            .conditional(onClick != null) {
                clickable(
                    enabled = true,
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = onClick!!
                )
            }
    }

    if (indicatorPosition == Alignment.Center) {
        indicatorModifier = indicatorModifier
            .ledRing(
                width = width,
                height = height,
                shape = shape,
                buttonColor = buttonColor,
                ringColor = indicatorColor,
                flatLook = flatLook,
                hoverColor = hoverColor,
                horizontalColors = horizontalColors,
                isSelected = selected,
                isHovered = isHovered
            )
    } else {
        indicatorModifier = indicatorModifier
            .indicator(
                width = width,
                height = height,
                shape = shape,
                buttonColor = buttonColor,
                indicatorPosition = indicatorPosition,
                indicatorColor = indicatorColor,
                flatLook = flatLook,
                hoverColor = hoverColor,
                horizontalColors = horizontalColors,
                isSelected = selected,
                isHovered = isHovered
            )
    }

    Box(
        modifier = indicatorModifier,
        contentAlignment = Alignment.Center
    ) {
        val paddings = when (indicatorPosition) {
            Alignment.TopCenter -> listOf(
                padding * 3 + paddingValues.calculateTopPadding(),
                padding + paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                padding + paddingValues.calculateBottomPadding(),
                padding + paddingValues.calculateStartPadding(LayoutDirection.Ltr)
            )
            Alignment.BottomCenter -> listOf(
                padding + paddingValues.calculateTopPadding(),
                padding + paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                padding * 3 + paddingValues.calculateBottomPadding(),
                padding + paddingValues.calculateStartPadding(LayoutDirection.Ltr)
            )
            Alignment.CenterStart -> listOf(
                padding + paddingValues.calculateTopPadding(),
                padding + paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                padding + paddingValues.calculateBottomPadding(),
                padding * 3 + paddingValues.calculateStartPadding(LayoutDirection.Ltr)
            )
            Alignment.CenterEnd -> listOf(
                padding + paddingValues.calculateTopPadding(),
                padding * 3 + paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                padding + paddingValues.calculateBottomPadding(),
                padding + paddingValues.calculateStartPadding(LayoutDirection.Ltr)
            )
            Alignment.Center -> listOf(
                padding * 3 + paddingValues.calculateTopPadding(),
                padding * 3 + paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                padding * 3 + paddingValues.calculateBottomPadding(),
                padding * 3 + paddingValues.calculateStartPadding(LayoutDirection.Ltr)
            )
            else -> listOf(
                padding + paddingValues.calculateTopPadding(),
                padding + paddingValues.calculateEndPadding(LayoutDirection.Ltr),
                padding + paddingValues.calculateBottomPadding(),
                padding + paddingValues.calculateStartPadding(LayoutDirection.Ltr)
            )
        }
        PlatformToolTip(
            text = toolTip,
            space = space,
            shape = MaterialTheme.shapes.small,
            backgroundColor = MaterialTheme.colorScheme.surfaceContainerLowest
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = paddings[0], end = paddings[1], bottom = paddings[2], start = paddings[3]),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (leadingIcon != null) {
                    Icon(
                        modifier = Modifier,
                        painter = leadingIcon,
                        contentDescription = null,
                        tint = if (enabled) leadingIconTint else leadingIconTintDisabled
                    )
                    if (text != null) Spacer(Modifier.width(space))
                } else if (leadingImage != null) {
                    leadingImage()
                    if (text != null) Spacer(Modifier.width(space))
                }

                if (text?.isNotEmpty() == true) {
                    Text(
                        modifier = Modifier
                            .weight(1f),
                        text = text,
                        textAlign = textAlign,
                        maxLines = maxLines,
                        overflow = if (maxLines == 1) TextOverflow.Ellipsis else TextOverflow.Clip,
                        softWrap = maxLines > 1,
                        style = textStyle,
                        color = if (enabled) textColor else textColorDisabled,
                    )
                }

                if (trailingIcon != null) {
                    if (text != null || leadingIcon != null) Spacer(Modifier.width(space))
                    Icon(
                        modifier = Modifier,
                        painter = trailingIcon,
                        contentDescription = null,
                        tint = if (enabled) trailingIconTint else trailingIconTintDisabled
                    )
                }
            }
        }
    }
}

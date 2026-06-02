package de.visualdigits.common.presentation.components.container

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsHoveredAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.UiPlatform
import de.visualdigits.common.presentation.components.androidPlatform
import de.visualdigits.common.presentation.components.platformFocus
import de.visualdigits.common.presentation.components.util.conditional
import de.visualdigits.common.presentation.components.util.outlinedTextFieldColors

@Composable
fun VerticalCollapsibleBox(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    iconArrowRight: Painter? = null,
    iconArrowDown: Painter? = null,
    space: Dp = 8.dp,
    paddingContainer: PaddingValues = PaddingValues(bottom = 8.dp),
    title: String? = null,
    titleContent: (@Composable () -> Unit)? = null,
    isTitleHoverable: Boolean = false,
    titleHoverColor: Color = Color.Transparent,
    focusedBorderColor: Color = MaterialTheme.colorScheme.outline,
    unfocusedBorderColor: Color = MaterialTheme.colorScheme.onSurface,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    shape: Shape = MaterialTheme.shapes.extraSmall,
    animateContent: Boolean = false,
    onStateChange: (Boolean) -> Unit,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    isExpanded: Boolean,
    trailingIcon: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    if (androidPlatform() == UiPlatform.UI_MODE_TYPE_TELEVISION) {
        VerticalCollapsibleBoxTv(
            modifier = modifier,
            paddingContainer = paddingContainer,
            space = space,
            title = title,
            titleContent = titleContent,
            backgroundColor = backgroundColor,
            shape = shape,
            trailingIcon = trailingIcon,
            content = content
        )
    } else {
        VerticalCollapsibleBoxFull(
            modifier = modifier,
            enabled = enabled,
            isTitleHoverable = isTitleHoverable,
            titleHoverColor = titleHoverColor,
            paddingContainer = paddingContainer,
            iconArrowRight = iconArrowRight,
            iconArrowDown = iconArrowDown,
            space = space,
            title = title,
            titleContent = titleContent,
            unfocusedBorderColor = unfocusedBorderColor,
            focusedBorderColor = focusedBorderColor,
            backgroundColor = backgroundColor,
            shape = shape,
            iconTint = iconTint,
            animateContent = animateContent,
            onStateChange = onStateChange,
            isExpanded = isExpanded,
            trailingIcon = trailingIcon,
            content = content
        )
    }
}

@Composable
fun VerticalCollapsibleBoxFull(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    paddingContainer: PaddingValues,
    iconArrowRight: Painter? = null,
    iconArrowDown: Painter? = null,
    space: Dp = 9.dp,
    title: String? = null,
    titleContent: (@Composable () -> Unit)? = null,
    isTitleHoverable: Boolean = false,
    titleHoverColor: Color,
    unfocusedBorderColor: Color,
    focusedBorderColor: Color,
    backgroundColor: Color,
    shape: Shape,
    iconTint: Color = Color.White,
    animateContent: Boolean,
    onStateChange: (Boolean) -> Unit,
    isExpanded: Boolean,
    trailingIcon: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isHovered by interactionSource.collectIsHoveredAsState()

    BasicTextField(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor, shape)
            .conditional(isTitleHoverable) { hoverable(interactionSource = interactionSource) }
            .conditional(isTitleHoverable) { pointerHoverIcon(PointerIcon.Hand) }
            .conditional(animateContent && enabled) {
                animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
            },
        value = "",
        onValueChange = { },
        readOnly = true,
        singleLine = false,
        decorationBox = { _ ->
            OutlinedTextFieldDefaults.DecorationBox(
                innerTextField = {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(space),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        // header row
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .conditional(isHovered) { background(titleHoverColor) }
                                .pointerHoverIcon(PointerIcon.Hand)
                                .clickable {
                                    onStateChange(!isExpanded)
                                },
//                            horizontalArrangement = Arrangement.spacedBy(space),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
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
                                    .width(40.dp)
                                    .fillMaxHeight(),
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
                                modifier = Modifier
                                    .padding(paddingContainer),
                            ) {
                                content()
                            }
                        }
                    }
                },
                visualTransformation = VisualTransformation.None,
                value = "",
                singleLine = false,
                enabled = true,
                isError = false,
                interactionSource = interactionSource,
                colors = outlinedTextFieldColors(focusedBorderColor, unfocusedBorderColor),
                contentPadding = PaddingValues(0.dp)
            ) {
                OutlinedTextFieldDefaults.Container(
                    enabled = true,
                    isError = false,
                    interactionSource = interactionSource,
                    colors = outlinedTextFieldColors(focusedBorderColor, unfocusedBorderColor),
                    shape = shape,
                )
            }
        }
    )
}

@Composable
fun VerticalCollapsibleBoxTv(
    modifier: Modifier = Modifier,
    paddingContainer: PaddingValues,
    space: Dp = 8.dp,
    title: String? = null,
    titleContent: (@Composable () -> Unit)? = null,
    backgroundColor: Color,
    shape: Shape,
    trailingIcon: (@Composable () -> Unit)? = null,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier
            .background(backgroundColor, shape)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(space)
    ) {
        title?.let { t ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(space)
                    .background(backgroundColor.copy(alpha = 0.4f), shape)
                    .platformFocus(),
                horizontalArrangement = Arrangement.spacedBy(space)
            ) {
                Text(
                    modifier = Modifier
                        .padding(space),
                    text = t,
                    style = MaterialTheme.typography.titleSmall
                )

                trailingIcon?.let { ti -> ti() }
            }
        }

        Box(
            modifier = Modifier
                .padding(paddingContainer),
        ) {
            content()
        }
    }
}

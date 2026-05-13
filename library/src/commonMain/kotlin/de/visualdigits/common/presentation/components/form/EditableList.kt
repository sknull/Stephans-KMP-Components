package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.runtime.toMutableStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.KeyValue
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.AbstractFieldDescriptor
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.form.EditableListResources
import de.visualdigits.common.presentation.components.PlatformVerticalScrollbar
import de.visualdigits.common.presentation.components.button.IndicatorButton
import de.visualdigits.common.presentation.components.util.conditional
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight

@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> EditableList(
    modifier: Modifier = Modifier,
    fieldState: FieldState<K, FK>,
    titleChooseDirectory: UiText,
    titleChooseFile: UiText,
    iconFolder: Painter,
    resources: EditableListResources,
    fieldHeight: Dp = Dp.Unspecified,
    space: Dp,
    focusedBorderColor: Color = MaterialTheme.colorScheme.outline,
    unfocusedBorderColor: Color = MaterialTheme.colorScheme.onSurface,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    buttonShape: Shape = MaterialTheme.shapes.extraSmall,
    containerShape: Shape = MaterialTheme.shapes.small,
    buttonColor: Color =MaterialTheme.colorScheme.onTertiary,
    visibilityIcon: Painter? = null,
    textStyle: TextStyle,
    scrollable: Boolean = false,
    onValueChange: (KeyValue) -> Unit,
    deleteAllowed: (AbstractFieldDescriptor<*,*,*,*,*>?, String) -> Boolean = { _, _ -> true }
) {
    val interactionSource = remember { MutableInteractionSource() }
    val values = (fieldState.currentValue as? List<Any>)?.map { v -> v.toString() }?:listOf()
    val previousItems = remember { values.toMutableStateList() }
    val items = remember { mutableStateListOf<String>() }
    LaunchedEffect(values) {
        if (items != values) {
            items.clear()
            items.addAll(values)
        }
    }
    var showDialog by remember { mutableStateOf(false) }
    var editingIndex by remember { mutableStateOf<Int?>(null) }
    var currentText by remember { mutableStateOf<String?>(null) }

    val halfHeight = minimizedLabelHalfHeight(textStyle)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = halfHeight + 1.dp) // why?
    ) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .drawBehind {
                    drawRoundRect(
                        color = unfocusedBorderColor,
                        style = Stroke(
                            width = 1.5.dp.toPx(),
                        ),
                       cornerRadius = CornerRadius(4.dp.toPx())
                    )
                }
//                .border(1.dp, unfocusedBorderColor, buttonShape)
        ) {
            val scrollState = rememberScrollState(0)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .conditional(scrollable) { verticalScroll(scrollState) },
                verticalArrangement = Arrangement.spacedBy(space)
            ) {
                Text(
                    modifier = Modifier
                        .offset(y = halfHeight * -1),
                    text = fieldState.fieldDescriptor.label.asString(),
                    style = MaterialTheme.typography.bodySmall,
                )


                items.forEachIndexed { index, item ->
                    val allowDelete = deleteAllowed(fieldState.fieldDescriptor, item)

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(fieldHeight * 0.75f),
                        shape = buttonShape,
                        color = Color.Transparent,
                        border = BorderStroke(
                            width = 1.dp,
                            color = unfocusedBorderColor
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent)
                                .padding(start = space, end = 0.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(space)
                        ) {
                            Text(
                                text = item,
                                style = MaterialTheme.typography.bodySmall
                            )

                            Spacer(Modifier.weight(1f))

                            if (fieldState.fieldDescriptor.enabled) {
                                IndicatorButton(
                                    leadingIcon = resources.iconEdit,
                                    toolTip = resources.toolTipEdit.asString(),
                                    width = 30.dp,
                                    height = 30.dp,
                                    onClick = {
                                        editingIndex = index
                                        currentText = item
                                        showDialog = true
                                    }
                                )

                                IndicatorButton(
                                    leadingIcon = resources.iconDelete,
                                    toolTip = resources.toolTipDelete.asString(),
                                    width = 30.dp,
                                    height = 30.dp,
                                    enabled = allowDelete,
                                    onClick = {
                                        editingIndex = null
                                        currentText = null
                                        items.removeAt(index)
                                        showDialog = false
                                        onValueChange(KeyValue(fieldState.fieldDescriptor, items.joinToString(",")))
                                    }
                                )
                            }
                        }
                    }
                }

                if (fieldState.fieldDescriptor.enabled) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        IndicatorButton(
                            modifier = Modifier
                                .align(Alignment.CenterEnd),
                            width = 50.dp,
                            height = 50.dp,
                            toolTip = resources.hintAdd.asString(),
                            buttonColor = buttonColor,
                            shape = buttonShape,
                            leadingIcon = resources.iconAdd,
                            leadingIconTint = iconTint
                        ) {
                            editingIndex = null
                            currentText = ""
                            showDialog = true
                        }
                    }
                }
            }

            if (scrollable) {
                PlatformVerticalScrollbar(
                    interactionSource = interactionSource,
                    modifier = Modifier
                        .clip(containerShape)
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.4f))
                        .width(space),
                    scrollState = scrollState
                )
            }
        }

        if (showDialog) {
            previousItems.update(items)
            fieldState.fieldDescriptor as AbstractFieldDescriptor<Any, Any, K, Any, Any>
            AlertDialog(
                modifier = Modifier
                    .border(1.dp, focusedBorderColor, containerShape),
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = containerShape,
                onDismissRequest = { showDialog = false },
                title = { Text(if (editingIndex == null) resources.titleAdd.asString() else resources.titleEdit.asString()) },
                text = {
                    TypeAwareEditableField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        fieldState = fieldState,
                        currentValue = currentText,
                        titleChooseDirectory = titleChooseDirectory,
                        titleChooseFile = titleChooseFile,
                        iconFolder = iconFolder,
                        fieldHeight = fieldHeight,
                        focusedBorderColor = focusedBorderColor,
                        unfocusedBorderColor = unfocusedBorderColor,
                        textStyle = textStyle,
                        visibilityIcon = visibilityIcon,
                        iconTint = iconTint,
                        buttonShape = buttonShape,
                        buttonColor = buttonColor,
                        onValueChange = { keyValue ->
                            currentText = keyValue.value?.toString() ?: ""
                        }
                    )
                },
                confirmButton = {
                    IndicatorButton(
                        toolTip = resources.labelOk.asString(),
                        width = 50.dp,
                        height = 50.dp,
                        buttonColor = buttonColor,
                        shape = buttonShape,
                        leadingIcon = resources.iconOk,
                        leadingIconTint = iconTint
                    ) {
                        if (editingIndex != null) {
                            currentText?.also { ct -> items[editingIndex!!] = ct }
                        } else {
                            currentText?.also { ct -> items.add(ct) }
                        }
                        onValueChange(
                            KeyValue(
                                descriptor =fieldState.fieldDescriptor,
                                value = items
                            )
                        )
                        showDialog = false
                    }
                },
                dismissButton = {
                    IndicatorButton(
                        toolTip = resources.labelCancel.asString(),
                        width = 50.dp,
                        height = 50.dp,
                        buttonColor = buttonColor,
                        shape = buttonShape,
                        leadingIcon = resources.iconCancel,
                        leadingIconTint = iconTint
                    ) {
                        items.update(previousItems)
                        onValueChange(KeyValue(fieldState.fieldDescriptor, items.joinToString(",")))
                        showDialog = false
                    }
                }
            )
        }
    }
}

private fun <T> SnapshotStateList<T>.update(values: MutableList<T>) {
    clear()
    addAll(values)
}

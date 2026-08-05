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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.configuration.AbstractFieldDescriptor
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.form.LocalEditableListResources
import de.visualdigits.common.domain.model.form.LocalFormFieldResources
import de.visualdigits.common.domain.model.form.LocalFormResources
import de.visualdigits.common.domain.model.ui.KeyValue
import de.visualdigits.common.presentation.components.PlatformVerticalScrollbar
import de.visualdigits.common.presentation.components.button.IndicatorButton
import de.visualdigits.common.presentation.components.util.conditional
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight
import org.jetbrains.compose.resources.painterResource

@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> EditableList(
    modifier: Modifier = Modifier,
    fieldState: FieldState<K, FK>,
    scrollable: Boolean = false,
    onValueChange: (KeyValue) -> Unit,
    colorPickerUseOnlySliders: Boolean = false,
    deleteAllowed: (AbstractFieldDescriptor<*,*,*,*,*>?, String) -> Boolean = { _, _ -> true }
) {
    val formResources = LocalFormResources.current
    val formFieldResources = LocalFormFieldResources.current
    val editableListResources = LocalEditableListResources.current

    val interactionSource = remember { MutableInteractionSource() }
    val initialIndex = remember { 0 }
    val initialOffset = remember { 0 }

    val lazyListState = rememberLazyListState(
        initialFirstVisibleItemIndex = initialIndex,
        initialFirstVisibleItemScrollOffset = initialOffset
    )
    val values = (fieldState.currentValue as? List<*>)?.map { v -> v.toString() }?:listOf()
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

    val halfHeight = minimizedLabelHalfHeight(formFieldResources.textStyle)
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
                        color = formFieldResources.unfocusedBorderColor,
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
                    .padding(8.dp)
                    .conditional(scrollable) { verticalScroll(scrollState) },
                verticalArrangement = Arrangement.spacedBy(formResources.space)
            ) {
                Text(
                    text = fieldState.fieldDescriptor.label.asString(),
                    style = MaterialTheme.typography.labelSmall,
                )


                items.forEachIndexed { index, item ->
                    val allowDelete = deleteAllowed(fieldState.fieldDescriptor, item)

                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(formFieldResources.fieldHeight * 0.75f),
                        shape = formFieldResources.shape,
                        color = Color.Transparent,
                        border = BorderStroke(
                            width = 1.dp,
                            color = formFieldResources.unfocusedBorderColor
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color.Transparent)
                                .padding(start = formResources.space, end = 0.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(formResources.space)
                        ) {
                            Text(
                                modifier = Modifier.weight(1f),
                                text = item,
                                style = MaterialTheme.typography.bodySmall
                            )

                            Spacer(Modifier.weight(1f))

                            if (fieldState.fieldDescriptor.enabled && fieldState.fieldDescriptor.enabledCondition(fieldState.configuration, null)) {
                                IndicatorButton(
                                    leadingIcon = editableListResources.iconEdit?.let { r -> painterResource(r) },
                                    leadingIconTint = formFieldResources.iconTint,
                                    toolTip = editableListResources.toolTipEdit.asString(),
                                    width = 30.dp,
                                    height = 30.dp,
                                    onClick = {
                                        editingIndex = index
                                        currentText = item
                                        showDialog = true
                                    }
                                )

                                IndicatorButton(
                                    leadingIcon = editableListResources.iconDelete?.let { r -> painterResource(r) },
                                    leadingIconTint = formFieldResources.iconTint,
                                    toolTip = editableListResources.toolTipDelete.asString(),
                                    width = 30.dp,
                                    height = 30.dp,
                                    enabled = allowDelete,
                                    onClick = {
                                        editingIndex = null
                                        currentText = null
                                        items.removeAt(index)
                                        showDialog = false
                                        onValueChange(KeyValue(fieldState.fieldDescriptor, items))
                                    }
                                )
                            }
                        }
                    }
                }

                if (fieldState.fieldDescriptor.enabled && fieldState.fieldDescriptor.enabledCondition(fieldState.configuration, null)) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                    ) {
                        IndicatorButton(
                            modifier = Modifier
                                .align(Alignment.CenterEnd),
                            width = 50.dp,
                            height = 50.dp,
                            toolTip = editableListResources.tooltipAdd.asString(),
                            buttonColor = formResources.buttonColor,
                            shape = formResources.buttonShape,
                            leadingIcon = editableListResources.iconAdd?.let { r -> painterResource(r) },
                            leadingIconTint = formFieldResources.iconTint
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
                        .clip(formResources.containerShape)
                        .align(Alignment.CenterEnd)
                        .fillMaxHeight()
                        .background(MaterialTheme.colorScheme.surfaceContainer.copy(alpha = 0.4f))
                        .width(formResources.space),
                    lazyListState = lazyListState
                )
            }
        }

        if (showDialog) {
            previousItems.update(items)
            fieldState.fieldDescriptor as AbstractFieldDescriptor<Any, Any, K, Any, Any>
            AlertDialog(
                modifier = Modifier
                    .border(1.dp, formFieldResources.focusedBorderColor, formResources.containerShape),
                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                shape = formResources.containerShape,
                onDismissRequest = { showDialog = false },
                title = { Text(if (editingIndex == null) editableListResources.titleAdd.asString() else editableListResources.titleEdit.asString()) },
                text = {
                    TypeAwareEditableField(
                        modifier = Modifier
                            .fillMaxWidth(),
                        fieldState = fieldState,
                        currentValue = currentText,
                        colorPickerUseOnlySliders = colorPickerUseOnlySliders,
                        onValueChange = { keyValue ->
                            currentText = keyValue.value?.toString() ?: ""
                        }
                    )
                },
                confirmButton = {
                    IndicatorButton(
                        toolTip = editableListResources.labelOk.asString(),
                        width = 50.dp,
                        height = 50.dp,
                        buttonColor = formResources.buttonColor,
                        shape = formResources.buttonShape,
                        leadingIcon = editableListResources.iconOk?.let { r -> painterResource(r) },
                        leadingIconTint = formFieldResources.iconTint
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
                        toolTip = editableListResources.labelCancel.asString(),
                        width = 50.dp,
                        height = 50.dp,
                        buttonColor = formResources.buttonColor,
                        shape = formResources.buttonShape,
                        leadingIcon = editableListResources.iconCancel?.let { r -> painterResource(r) },
                        leadingIconTint = formFieldResources.iconTint
                    ) {
                        items.update(previousItems)
                        onValueChange(KeyValue(
                            descriptor = fieldState.fieldDescriptor,
                            value = items
                        ))
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

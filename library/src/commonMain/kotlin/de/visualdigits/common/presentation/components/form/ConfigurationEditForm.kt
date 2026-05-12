package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.KeyValue
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.AbstractConfiguration
import de.visualdigits.common.domain.model.configuration.AbstractFieldDescriptor
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.form.EditableListResources
import de.visualdigits.common.presentation.components.PlatformVerticalScrollbarBox
import de.visualdigits.common.presentation.components.button.IndicatorButton
import de.visualdigits.common.presentation.model.CommonAction
import de.visualdigits.common.presentation.model.PlatformScrollbarStyle
import de.visualdigits.common.presentation.model.defaultScrollbarStyle


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <K : FieldKey<K>> ConfigurationEditForm(
    modifier: Modifier = Modifier,
    configuration: AbstractConfiguration<*, K>,
    scrollbarModifier: Modifier = Modifier,
    backgroundColor: Color = MaterialTheme.colorScheme.surfaceContainerLow,
    titleChooseDirectory: UiText,
    titleChooseFile: UiText,
    iconFolder: Painter,
    editableListResources: EditableListResources,
    tooltipOk: UiText,
    visibilityIcon: Painter? = null,
    iconOk: Painter,
    tooltipCancel: UiText,
    iconCancel: Painter,
    scrollPosition: MutableMap<String, Pair<Int, Int?>>,
    scrollbarId: String,
    scrollbarStyle: PlatformScrollbarStyle = defaultScrollbarStyle(),
    fieldHeight: Dp = Dp.Unspecified,
    focusedBorderColor: Color = MaterialTheme.colorScheme.outline,
    unfocusedBorderColor: Color = MaterialTheme.colorScheme.onSurface,
    iconTint: Color = MaterialTheme.colorScheme.onSurface,
    buttonShape: Shape = MaterialTheme.shapes.extraSmall,
    buttonColor: Color = MaterialTheme.colorScheme.surfaceContainerLowest,
    containerShape: Shape = MaterialTheme.shapes.small,
    textStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    space: Dp = 8.dp,
    onValueChange: (KeyValue) -> Unit,
    onCancelClick: () -> Unit,
    onOkClick: () -> Unit,
    onCommonAction: (CommonAction) -> Unit,
    deleteAllowed: (AbstractFieldDescriptor<*,*,*,*>?, String) -> Boolean = { _,_ -> true }
) {
    PlatformVerticalScrollbarBox(
        modifier = modifier
            .fillMaxWidth(),
        backgroundColor = backgroundColor,
        scrollbarModifier = scrollbarModifier,
        scrollbarStyle = scrollbarStyle,
        scrollbarId = scrollbarId,
        scrollPosition = scrollPosition,
        onCommonAction = onCommonAction
    ) {
        listOf(
            Pair("fields", @Composable {
                FlowRow(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(space),
                    verticalArrangement = Arrangement.spacedBy(space)
                ) {
                    configuration
                        .fieldDescriptors
                        .filter { fieldDescriptor -> fieldDescriptor.visible }
                        .forEach { fieldDescriptor ->
                            @Suppress("UNCHECKED_CAST")
                            fieldDescriptor as AbstractFieldDescriptor<Any, Any, K, Any>
                            val currentValue = configuration.getUnsafe(fieldDescriptor.key)
                            key(fieldDescriptor.key) {
                                val fieldState = remember(currentValue, fieldDescriptor) {
                                    val currentOption = fieldDescriptor.currentOption(configuration)
                                    FieldState(
                                        fieldDescriptor = fieldDescriptor,
                                        options = fieldDescriptor.options(configuration),
                                        currentValue = currentValue,
                                        currentOption = currentOption,
                                        currentOptionUIText = currentOption?.second ?: UiText.DynamicString(
                                            currentOption?.first?.toString() ?: ""
                                        ),
                                        valid = fieldDescriptor.valid(configuration, currentValue)
                                    )
                                }

                                Box(
                                    modifier = Modifier
                                        .width(300.dp)
                                ) {
                                    EditableField(
                                        fieldState = fieldState,
                                        titleChooseDirectory = titleChooseDirectory,
                                        titleChooseFile = titleChooseFile,
                                        iconFolder = iconFolder,
                                        editableListResources = editableListResources,
                                        fieldHeight = fieldHeight,
                                        space = space,
                                        unfocusedBorderColor = unfocusedBorderColor,
                                        focusedBorderColor = focusedBorderColor,
                                        visibilityIcon = visibilityIcon,
                                        iconTint = iconTint,
                                        buttonColor = buttonColor,
                                        buttonShape = buttonShape,
                                        containerShape = containerShape,
                                        textStyle = textStyle,
                                        onValueChange = onValueChange,
                                        deleteAllowed = deleteAllowed
                                    )
                                }
                            }
                       }
                }
            }),
            Pair("spacer", @Composable {
                Spacer(Modifier.height(16.dp))
            }),
            Pair("buttons", @Composable {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(space),
                    modifier = Modifier
                        .wrapContentWidth(),
                ) {
                    Spacer(Modifier.weight(1f))

                    IndicatorButton(
                        toolTip = tooltipCancel.asString(),
                        width = 50.dp,
                        height = 50.dp,
                        buttonColor = buttonColor,
                        shape = buttonShape,
                        leadingIcon = iconCancel,
                        leadingIconTint = iconTint,
                        onClick = onCancelClick
                    )

                    IndicatorButton(
                        toolTip = tooltipOk.asString(),
                        width = 50.dp,
                        height = 50.dp,
                        buttonColor = buttonColor,
                        shape = buttonShape,
                        leadingIcon = iconOk,
                        leadingIconTint = iconTint,
                        onClick = onOkClick
                    )
                }
            })
        )
    }
}


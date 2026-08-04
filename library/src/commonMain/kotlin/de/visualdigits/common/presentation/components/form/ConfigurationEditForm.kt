package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.key
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.configuration.AbstractConfiguration
import de.visualdigits.common.domain.model.configuration.AbstractFieldDescriptor
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.form.LocalFormResources
import de.visualdigits.common.domain.model.platform.PlatformType
import de.visualdigits.common.domain.model.ui.KeyValue
import de.visualdigits.common.domain.model.ui.UiPlatform
import de.visualdigits.common.domain.model.ui.UiText
import de.visualdigits.common.presentation.components.PlatformVerticalScrollbarBox
import de.visualdigits.common.presentation.components.androidPlatform
import de.visualdigits.common.presentation.components.button.IndicatorButton
import de.visualdigits.common.presentation.components.container.OutlinedGroupBox
import de.visualdigits.common.presentation.model.CommonAction
import de.visualdigits.common.presentation.model.ScrollIntent


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> ConfigurationEditForm(
    modifier: Modifier = Modifier,
    platformType: PlatformType,
    configuration: AbstractConfiguration<*, K>,
    configurationRef: AbstractConfiguration<*, FK>? = null,
    scrollbarModifier: Modifier = Modifier,
    scrollPosition: MutableMap<String, Triple<Int, Int?, ScrollIntent>> = mutableMapOf(),
    scrollbarId: String? = null,
    colorPickerUseOnlySliders: Boolean = false,
    onValueChange: (KeyValue) -> Unit,
    onCancelClick: () -> Unit,
    onOkClick: () -> Unit,
    onCommonAction: ((CommonAction) -> Unit)? = null,
    deleteAllowed: (AbstractFieldDescriptor<*,*,*,*,*>?, String) -> Boolean = { _,_ -> true },
    headerContent: (@Composable () -> Unit)? = null
) {
    val formResources = LocalFormResources.current
    val androidPlatform = androidPlatform()
    val platform = Pair(platformType, androidPlatform)

    PlatformVerticalScrollbarBox(
        modifier = modifier
            .fillMaxWidth()
            .padding(end = 10.dp + formResources.space),
        platformType = platformType,
        backgroundColor = formResources.backgroundColor,
        scrollbarModifier = scrollbarModifier,
        scrollbarId = scrollbarId,
        scrollPosition = scrollPosition,
        onCommonAction = onCommonAction
    ) {
        listOf(
            Pair("header", @Composable {
                headerContent?.let { hc -> hc() }
            }),
            Pair("fields", @Composable {
                FlowRow(
                    modifier = Modifier
                        .fillMaxSize(),
                    horizontalArrangement = Arrangement.spacedBy(formResources.space),
                    verticalArrangement = Arrangement.spacedBy(formResources.space)
                ) {
                    configuration
                        .fieldDescriptors
                        .filter { fieldDescriptor ->
                            fieldDescriptor.visible &&
                                    fieldDescriptor.notValidForPlatforms.none { pair -> pair == platform }
                        }
                        .groupBy { fieldDescriptor -> fieldDescriptor.group?.asString() }
                        .forEach { (group, fieldDescriptors) ->
                            if (group != null && androidPlatform != UiPlatform.UI_MODE_TYPE_TELEVISION) {
                                OutlinedGroupBox(
                                    label = { Text(group) },
                                    space = formResources.space
                                ) {
                                    FlowRow(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        horizontalArrangement = Arrangement.spacedBy(formResources.space),
                                        verticalArrangement = Arrangement.spacedBy(
                                            space = formResources.space,
                                            alignment = Alignment.Bottom
                                        )
                                    ) {
                                        RenderFields(
                                            fieldDescriptors = fieldDescriptors,
                                            configuration = configuration,
                                            configurationRef = configurationRef,
                                            colorPickerUseOnlySliders = colorPickerUseOnlySliders,
                                            onValueChange = onValueChange,
                                            deleteAllowed = deleteAllowed
                                        )
                                    }
                                }
                            } else {
                                RenderFields(
                                    fieldDescriptors = fieldDescriptors,
                                    configuration = configuration,
                                    configurationRef = configurationRef,
                                    colorPickerUseOnlySliders = colorPickerUseOnlySliders,
                                    onValueChange = onValueChange,
                                    deleteAllowed = deleteAllowed
                                )
                            }
                        }
                }
            }),
            Pair("spacer", @Composable {
                Spacer(Modifier.height(16.dp))
            }),
            Pair("buttons", @Composable {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(formResources.space),
                    modifier = Modifier
                        .wrapContentWidth(),
                ) {
                    Spacer(Modifier.weight(1f))

                    IndicatorButton(
                        toolTip = formResources.tooltipCancel?.asString(),
                        width = 50.dp,
                        height = 50.dp,
                        buttonColor = formResources.buttonColor,
                        shape = formResources.buttonShape,
                        leadingIcon = formResources.iconCancel,
                        leadingIconTint = formResources.iconTint,
                        onClick = onCancelClick
                    )

                    IndicatorButton(
                        toolTip = formResources.tooltipOk?.asString(),
                        width = 50.dp,
                        height = 50.dp,
                        buttonColor = formResources.buttonColor,
                        shape = formResources.buttonShape,
                        leadingIcon = formResources.iconOk,
                        leadingIconTint = formResources.iconTint,
                        onClick = onOkClick
                    )
                }
            })
        )
    }
}

@Composable
private fun <FK : FieldKey<FK>, K : FieldKey<K>> RenderFields(
    fieldDescriptors: List<AbstractFieldDescriptor<*, *, K, *, *>>,
    configuration: AbstractConfiguration<*, K>,
    configurationRef: AbstractConfiguration<*, FK>?,
    colorPickerUseOnlySliders: Boolean = false,
    onValueChange: (KeyValue) -> Unit,
    deleteAllowed: (AbstractFieldDescriptor<*, *, *, *, *>?, String) -> Boolean
) {
    fieldDescriptors.forEach { fieldDescriptor ->
        @Suppress("UNCHECKED_CAST")
        fieldDescriptor as AbstractFieldDescriptor<Any, Any, K, FK, Any>
        val currentValue = configuration.getUnsafe(fieldDescriptor.key)
        key(fieldDescriptor.key) {
            val fieldState = remember(currentValue, fieldDescriptor) {
                val currentOption = fieldDescriptor.currentOption(configuration, configurationRef)
                FieldState(
                    configuration = configuration,
                    fieldDescriptor = fieldDescriptor,
                    options = fieldDescriptor.options(configuration, configurationRef),
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
                    colorPickerUseOnlySliders = colorPickerUseOnlySliders,
                    onValueChange = onValueChange,
                    deleteAllowed = deleteAllowed
                )
            }
        }
    }
}


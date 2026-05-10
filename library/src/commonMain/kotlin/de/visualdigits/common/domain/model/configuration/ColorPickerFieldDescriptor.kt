package de.visualdigits.common.domain.model.configuration

import androidx.compose.ui.graphics.Color
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.keyfactory.ColorKeyFactory

/**
 * Represents a field which is rendered as a text field in the UI.
 */
class ColorPickerFieldDescriptor<K : FieldKey<K>>(
    key: K,

    label: UiText,
    toolTip: UiText? = null,

    visible: Boolean = true,
    readOnly: Boolean = false,
): AbstractFieldDescriptor<Color, Color, K>(
    fieldClass = Color::class,
    key = key,
    label = label,
    toolTip = toolTip,
    visible = visible,
    readOnly = readOnly,
    options = { listOf() },
    keyFactory = ColorKeyFactory
)

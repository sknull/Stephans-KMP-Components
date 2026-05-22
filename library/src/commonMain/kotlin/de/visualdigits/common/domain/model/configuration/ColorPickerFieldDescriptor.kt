package de.visualdigits.common.domain.model.configuration

import androidx.compose.ui.graphics.Color
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.keyfactory.ColorKeyFactory

/**
 * Represents a field which is rendered as a text field in the UI.
 */
class ColorPickerFieldDescriptor<K : FieldKey<K>, FK : FieldKey<FK>>(
    group: UiText? = null,

    key: K,

    label: UiText,
    toolTip: UiText? = null,

    visible: Boolean = true,
    readOnly: Boolean = false,

    default: Color? = null,

    valid: (AbstractConfiguration<*, K>, Any?) -> Boolean = { _, _ -> true },

): AbstractFieldDescriptor<Color, Color, K, K, String>(
    fieldClass = Color::class,
    group = group,
    key = key,
    label = label,
    toolTip = toolTip,
    visible = visible,
    readOnly = readOnly,
    default = default,
    valid = valid,
    keyFactory = ColorKeyFactory
)

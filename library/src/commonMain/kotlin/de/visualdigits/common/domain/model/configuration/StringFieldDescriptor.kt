package de.visualdigits.common.domain.model.configuration

import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.keyfactory.StringKeyFactory
import org.jetbrains.compose.resources.DrawableResource

/**
 * Represents a field which is rendered as a text field in the UI.
 */
class StringFieldDescriptor<K : FieldKey<K>, FK : FieldKey<FK>>(
    key: K,

    label: UiText,
    toolTip: UiText? = null,

    visible: Boolean = true,
    readOnly: Boolean = false,

    default: String? = null,

    valid: (AbstractConfiguration<*, K>, Any?) -> Boolean = { _, _ -> true },
): AbstractFieldDescriptor<String, String, K, K, String>(
    fieldClass = String::class,
    key = key,
    label = label,
    toolTip = toolTip,
    visible = visible,
    readOnly = readOnly,
    default = default,
    valid = valid,
    keyFactory = StringKeyFactory
)

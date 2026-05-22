package de.visualdigits.common.domain.model.configuration

import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.keyfactory.IntKeyFactory

/**
 * Represents a field which is rendered as a text field in the UI.
 */
class IntFieldDescriptor<K : FieldKey<K>, FK : FieldKey<FK>>(
    group: UiText? = null,

    key: K,

    label: UiText,
    toolTip: UiText? = null,

    visible: Boolean = true,
    readOnly: Boolean = false,

    default: Int? = null,

    valid: (AbstractConfiguration<*, K>, Any?) -> Boolean = { _, _ -> true },
): AbstractFieldDescriptor<Int, Int, K, K, Int>(
    fieldClass = Int::class,
    group = group,
    key = key,
    label = label,
    toolTip = toolTip,
    visible = visible,
    readOnly = readOnly,
    default = default,
    valid = valid,
    keyFactory = IntKeyFactory,
)

package de.visualdigits.common.domain.model.configuration

import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.keyfactory.IntKeyFactory
import org.jetbrains.compose.resources.DrawableResource

/**
 * Represents a field which is rendered as a text field in the UI.
 */
class IntFieldDescriptor<K : FieldKey<K>, FK : FieldKey<FK>>(
    key: K,

    label: UiText,
    toolTip: UiText? = null,

    visible: Boolean = true,
    readOnly: Boolean = false,

    default: Int? = null,

    valid: (AbstractConfiguration<*, K>, Any?) -> Boolean = { _, _ -> true },
): AbstractFieldDescriptor<Int, Int, K, K, Int>(
    fieldClass = Int::class,
    key = key,
    label = label,
    toolTip = toolTip,
    visible = visible,
    default = default,
    readOnly = readOnly,
    valid = valid,
    keyFactory = IntKeyFactory,
)

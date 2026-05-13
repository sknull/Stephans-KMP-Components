package de.visualdigits.common.domain.model.configuration

import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.keyfactory.KeyFactory
import org.jetbrains.compose.resources.DrawableResource
import kotlin.reflect.KClass

/**
 * Represents a field which is rendered as a combobox in the UI.
 * Similar to EnumFieldDescriptor but has not an enum as value.
 * Instead it can provide options from elsewhere.
 */
class ReferenceListFieldDescriptor<V : Any, K : FieldKey<K>, FK : FieldKey<FK>>(
    fieldClass: KClass<V>,

    key: K,

    label: UiText,
    toolTip: UiText? = null,

    visible: Boolean = true,
    readOnly: Boolean = false,

    default: V? = null,

    valid: (AbstractConfiguration<*, K>, Any?) -> Boolean = { _, _ -> true },
    options: (AbstractConfiguration<*, K>, AbstractConfiguration<*, FK>?) -> List<Triple<V, UiText?, DrawableResource?>> = { _, _ -> listOf() },

    keyFactory: KeyFactory<V>
): AbstractFieldDescriptor<V, V, K, FK, V>(
    fieldClass = fieldClass,
    key = key,
    label = label,
    toolTip = toolTip,
    visible = visible,
    readOnly = readOnly,
    default = default,
    valid = valid,
    options = options,
    keyFactory = keyFactory,
)

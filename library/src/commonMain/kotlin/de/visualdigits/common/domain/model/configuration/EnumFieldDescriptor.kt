package de.visualdigits.common.domain.model.configuration

import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.keyfactory.KeyFactory
import org.jetbrains.compose.resources.DrawableResource
import kotlin.reflect.KClass

/**
 * Represents a field which is rendered as a combobox in the UI.
 */
class EnumFieldDescriptor<V : Any, K : FieldKey<K>>(
    fieldClass: KClass<V>,

    group: UiText? = null,

    key: K,

    label: UiText,
    toolTip: UiText? = null,

    visible: Boolean = true,
    readOnly: Boolean = false,

    default: V? = null,

    enabled: Boolean = true,

    enabledCondition: (AbstractConfiguration<*, K>, Any?) -> Boolean = { _, _ -> true },

    valid: (AbstractConfiguration<*, K>, Any?) -> Severity = { _, _ -> Severity.Info },
    options: (AbstractConfiguration<*, K>, AbstractConfiguration<*, K>?) -> List<Triple<V, UiText?, DrawableResource?>> = { _, _ -> listOf() },

    keyFactory: KeyFactory<V>
): AbstractFieldDescriptor<V, V, K, K, V>(
    fieldClass = fieldClass,
    group = group,
    key = key,
    label = label,
    toolTip = toolTip,
    visible = visible,
    readOnly = readOnly,
    default = default,
    enabled = enabled,
    enabledCondition = enabledCondition,
    valid = valid,
    options = options,
    keyFactory = keyFactory
)

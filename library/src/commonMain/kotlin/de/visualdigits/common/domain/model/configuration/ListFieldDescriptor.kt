package de.visualdigits.common.domain.model.configuration

import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.keyfactory.KeyFactory
import org.jetbrains.compose.resources.DrawableResource
import kotlin.reflect.KClass

/**
 * Represents a field which is rendered as an editable list in the UI.
 */
@Suppress("UNCHECKED_CAST")
open class ListFieldDescriptor<F : Any, K : FieldKey<K>>(
    fieldClass: KClass<F>,

    group: UiText? = null,

    key: K,

    label: UiText,
    toolTip: UiText? = null,

    visible: Boolean = true,
    readOnly: Boolean = false,

    default: F? = null,

    enabled: Boolean = true,

    enabledCondition: (AbstractConfiguration<*, K>, Any?) -> Boolean = { _, _ -> true },

    valid: (AbstractConfiguration<*, K>, Any?) -> Severity = { _, _ -> Severity.Info },
    options: (AbstractConfiguration<*, K>, AbstractConfiguration<*, K>?) -> List<Triple<F, UiText?, DrawableResource?>> = { _, _ -> listOf() },

    keyFactory: KeyFactory<MutableList<F>>
): AbstractFieldDescriptor<MutableList<F>, F, K, K, F>(
    fieldClass = MutableList::class as KClass<MutableList<F>>,
    itemClass = fieldClass,
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

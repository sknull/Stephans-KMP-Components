package de.visualdigits.common.domain.model.configuration

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

    key: K,

    label: UiText,
    toolTip: UiText? = null,

    visible: Boolean = true,
    readOnly: Boolean = false,

    default: F? = null,

    valid: (AbstractConfiguration<*, K>, Any?) -> Boolean = { _, _ -> true },
    options: (AbstractConfiguration<*, K>, AbstractConfiguration<*, K>?) -> List<Triple<F, UiText?, DrawableResource?>> = { _, _ -> listOf() },

    keyFactory: KeyFactory<MutableList<F>>
): AbstractFieldDescriptor<MutableList<F>, F, K, K, F>(
    fieldClass = MutableList::class as KClass<MutableList<F>>,
    itemClass = fieldClass,
    key = key,
    label = label,
    toolTip = toolTip,
    visible = visible,
    default = default,
    readOnly = readOnly,
    valid = valid,
    options = options,
    keyFactory = keyFactory
)

package de.visualdigits.common.domain.model.configuration

import de.visualdigits.common.domain.model.configuration.keyfactory.StringKeyFactory
import de.visualdigits.common.domain.model.ui.UiText


/**
 * Represents a field which should provide a file or directory picker.
 */
class SpacerFieldDescriptor<K : FieldKey<K>, FK : FieldKey<FK>>(
    group: UiText? = null,

    key: K,
): AbstractFieldDescriptor<String, String, K, K, String>(
    fieldClass = String::class,
    group = group,
    key = key,
    label = UiText.DynamicString(""),
    toolTip = UiText.DynamicString(""),
    readOnly = true,
    keyFactory = StringKeyFactory
)

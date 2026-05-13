package de.visualdigits.common.domain.model.configuration

import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.keyfactory.StringKeyFactory


/**
 * Represents a field which should provide a file or directory picker.
 */
class SpacerFieldDescriptor<K : FieldKey<K>, FK : FieldKey<FK>>(
    key: K,
): AbstractFieldDescriptor<String, String, K, K, String>(
    fieldClass = String::class,
    key = key,
    label = UiText.DynamicString(""),
    toolTip = UiText.DynamicString(""),
    visible = true,
    readOnly = true,
    keyFactory = StringKeyFactory
)

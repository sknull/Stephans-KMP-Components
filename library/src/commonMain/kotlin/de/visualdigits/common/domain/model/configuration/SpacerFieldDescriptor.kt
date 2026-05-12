package de.visualdigits.common.domain.model.configuration

import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.keyfactory.StringKeyFactory


/**
 * Represents a field which should provide a file or directory picker.
 */
class SpacerFieldDescriptor<K : FieldKey<K>>(
    key: K,
    label: UiText,
    toolTip: UiText? = null,
): AbstractFieldDescriptor<String, String, K, String>(
    fieldClass = String::class,
    key = key,
    label = label,
    toolTip = toolTip,
    visible = true,
    readOnly = true,
    keyFactory = StringKeyFactory
)

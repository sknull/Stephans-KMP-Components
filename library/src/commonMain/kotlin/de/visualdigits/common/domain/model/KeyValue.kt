package de.visualdigits.common.domain.model

import de.visualdigits.common.domain.model.configuration.AbstractFieldDescriptor

class KeyValue(
    val descriptor: AbstractFieldDescriptor<*,*,*,*,*>,
    val value: Any? = null,
    val previousValue: Any? = null,
    val newValue: Any? = null,
) {

    override fun toString(): String = "keyValueAction: $descriptor=$value"
}

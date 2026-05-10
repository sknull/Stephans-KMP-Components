package de.visualdigits.common.domain.model.configuration

interface FieldsInitializer<K : FieldKey<K>> {

    fun setupFields(): List<Field<*,*,K>>
}

package de.visualdigits.common.domain.model.configuration

/**
 * Base class for all configuration classes.
 */
abstract class AbstractConfiguration<T : AbstractConfiguration<T, K>, K : FieldKey<K>>(
    val values: Map<K, Any?> = mapOf(),
    val fieldDescriptors: List<AbstractFieldDescriptor<*, *, K, *, *>> = listOf(),
) {

    val lookupFieldDescriptors: Map<K, AbstractFieldDescriptor<*, *, K, *, *>> = fieldDescriptors.associateBy { descriptor -> descriptor.key }

    companion object {

        fun <K : FieldKey<K>, FK : FieldKey<FK>> valueMap(fieldDescriptors: List<AbstractFieldDescriptor<*, *, K, *, *>>, values: Map<K, Any?>): Map<K, Any?> {
            val lookupFieldDescriptors: Map<K, AbstractFieldDescriptor<*, *, K, *, *>> = fieldDescriptors.associateBy { descriptor -> descriptor.key }
            return lookupFieldDescriptors.map { (key, descriptor) ->
                Pair(key, values[key]?.let { value -> descriptor.keyFactory.fromValue(value) }?:descriptor.default)
            }.toMap()
        }
    }

    override fun toString(): String {
        return values.toList().joinToString(", ") { e -> "${e.first}=\"${e.second}\"" }
    }

    inline fun <reified V : Any> get(key: K): V? {
        return values[key] as? V
    }

    fun getUnsafe(key: FieldKey<*>?): Any? {
        return values[key]
    }

    /**
     * Determines if the actual value of the field is valid or not.
     * Returns a pair of <[true | false], description-resource-id>
     * When the [key] is not given it checks whether the entire configuration is valid or not.
     */
    open fun valid(key: K): Boolean? {
        return lookupFieldDescriptors.all { ( key, descriptor) -> descriptor.valid(this, values[key]) }
    }

    fun copy(values: Map<K, Any?>? = null): T {
        val newValues = values?.toMutableMap()?.apply { putAll(values) } ?: this.values

        return createInstance(newValues)
    }

    fun copy(key: K, value: Any? = null): T {
        val newValues = values.toMutableMap().apply { put(key, value) }

        return createInstance(newValues)
    }

    abstract fun createInstance(newValues: Map<K, Any?>): T
}

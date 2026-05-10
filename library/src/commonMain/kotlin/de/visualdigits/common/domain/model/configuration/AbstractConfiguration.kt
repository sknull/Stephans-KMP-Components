package de.visualdigits.common.domain.model.configuration

/**
 * Base class for all configuration classes.
 */
abstract class AbstractConfiguration<T : AbstractConfiguration<T, K>, K : FieldKey<K>>(
    val fields: List<Field<*,*,K>> = listOf()
) {

    val lookupMap = fields.associateBy { field -> field.descriptor.key }

    init {
        if (fields.isNotEmpty()) {
            fields.forEach { f ->
                f.configuration = this
            }
        }
    }

    override fun toString(): String {
        return fields.joinToString(", ") { e -> "${e.descriptor.key}=\"${e.value}\"" }
    }

    protected abstract fun createInstance(newFields: List<Field<*,*,K>>): T

    inline fun <reified V : Any> get(key: K): V? {
        val field = lookupMap[key]
        return field?.value as? V
    }

    fun getUnsafe(key: FieldKey<*>): Any? {
        val field = lookupMap[key]
        return field?.value
    }

    @Suppress("UNCHECKED_CAST")
    fun set(key: K, value: Any?) {
        val field = lookupMap[key]
        field?.setUnsafe(value)
    }

    /**
     * Determines if the actual value of the field is valid or not.
     * Returns a pair of <[true | false], description-resource-id>
     * When the [key] is not given it checks whether the entire configuration is valid or not.
     */
    open fun valid(key: K): Boolean? {
        return fields.all { field -> field.valid(field.value) }
    }

    fun copy(key: K? = null, value: String? = null): T {
        val newFields = fields.map { f ->
            if (f.descriptor.key == key) {
                f.copyUnsafe(f.fromString(value))
            } else {
                f.copy()
            }
        }

        val createInstance = createInstance(newFields)
        createInstance.fields.forEach { field -> field.configuration = createInstance }
        return createInstance
    }
}

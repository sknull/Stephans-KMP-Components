package de.visualdigits.common.domain.model.configuration

import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.configuration.keyfactory.KeyFactory
import de.visualdigits.common.domain.model.platform.PlatformType
import de.visualdigits.common.domain.model.ui.UiPlatform
import de.visualdigits.common.domain.model.ui.UiText
import org.jetbrains.compose.resources.DrawableResource
import kotlin.reflect.KClass

/**
 * Describes all aspects to render a configuration field.
 */
abstract class AbstractFieldDescriptor<V : Any, S : Any, K : FieldKey<K>, FK : FieldKey<FK>, O : Any>(

    /** The class of this field (can also be collection class). */
    val fieldClass: KClass<V>,

    /** Sometimes we need to know the item class of a collection as this is erased on runtime from the fieldclass. */
    val itemClass: KClass<S>? = null,

    /** An optional group for fields */
    val group: UiText? = null,

    /** The primary key of this field. */
    val key: K,

    /** A string resource id used to render the label in th UI. */
    val label: UiText,

    /** A string resource id used to render a tooltip in the UI (if any). */
    val toolTip: UiText? = null,

    /**
     * Determines whether the field should be rendered in the UI at all.
     * Sometimes we want to have a configuration which we need to treat separately from the other fields.
     */
    val visible: Boolean = true,

    /** Determines whether this field can be edited or not. */
    val readOnly: Boolean = false,

    var enabled: Boolean = true,

    val enabledCondition: (AbstractConfiguration<*, K>, Any?) -> Boolean = { _, _ -> true },

    val default: S? = null,

    val valid: (AbstractConfiguration<*, K>, Any?) -> Severity = { _, _ -> Severity.Info },

    /**
     *  For fields which are represented by a combobox or editable list this should generate the
     *  available values rendered in the UI.
     */
    var options: (AbstractConfiguration<*, K>, AbstractConfiguration<*, FK>?) -> List<Triple<O, UiText?, DrawableResource?>> = { _,_ -> listOf() },

    val notValidForPlatforms: List<Pair<PlatformType, UiPlatform?>> = listOf(),

    /** A factory class which handles conversion to and from string values. */
    val keyFactory: KeyFactory<V>

) {

    override fun toString(): String = "$key: $options"

    fun currentOption(configuration: AbstractConfiguration<*, K>, configurationRef: AbstractConfiguration<*, FK>? = null): Triple<O, UiText?, DrawableResource?>? {
        val currentValue = configuration.values[key]

        return this.options(configuration, configurationRef).find { o -> o.first == currentValue }
    }
}

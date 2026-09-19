package de.visualdigits.common.domain.model.configuration

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.configuration.keyfactory.KeyFactory
import de.visualdigits.common.domain.model.platform.PlatformType
import de.visualdigits.common.domain.model.ui.UiPlatform
import de.visualdigits.common.domain.model.ui.UiText
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
    width: Dp = 300.dp,
    readOnly: Boolean = false,

    default: F? = null,

    enabled: Boolean = true,

    enabledCondition: (AbstractConfiguration<*, K>, Any?) -> Boolean = { _, _ -> true },

    valid: (AbstractConfiguration<*, K>, Any?) -> Severity = { _, _ -> Severity.Info },
    options: (AbstractConfiguration<*, K>, AbstractConfiguration<*, K>?) -> List<Triple<F, UiText?, DrawableResource?>> = { _, _ -> listOf() },

    notValidForPlatforms: List<Pair<PlatformType, UiPlatform?>> = listOf(),

    keyFactory: KeyFactory<MutableList<F>>
): AbstractFieldDescriptor<MutableList<F>, F, K, K, F>(
    fieldClass = MutableList::class as KClass<MutableList<F>>,
    itemClass = fieldClass,
    group = group,
    key = key,
    label = label,
    toolTip = toolTip,
    visible = visible,
    width = width,
    readOnly = readOnly,
    enabled = enabled,
    enabledCondition = enabledCondition,
    default = default,
    valid = valid,
    options = options,
    notValidForPlatforms = notValidForPlatforms,
    keyFactory = keyFactory
)

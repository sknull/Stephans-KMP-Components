package de.visualdigits.common.domain.model.configuration

import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.configuration.keyfactory.LocalTimeKeyFactory
import de.visualdigits.common.domain.model.platform.PlatformType
import de.visualdigits.common.domain.model.ui.UiPlatform
import de.visualdigits.common.domain.model.ui.UiText
import kotlinx.datetime.LocalTime
import org.jetbrains.compose.resources.DrawableResource

/**
 * Represents a field which should provide a file or directory picker.
 */
class LocalTimeFieldDescriptor<K : FieldKey<K>, FK : FieldKey<FK>>(
    group: UiText? = null,

    key: K,

    label: UiText,
    toolTip: UiText? = null,

    visible: Boolean = true,
    readOnly: Boolean = false,

    default: LocalTime? = null,

    enabled: Boolean = true,

    enabledCondition: (AbstractConfiguration<*, K>, Any?) -> Boolean = { _, _ -> true },

    valid: (AbstractConfiguration<*, K>, Any?) -> Severity = { _, _ -> Severity.Info },
    options: (AbstractConfiguration<*, K>, AbstractConfiguration<*, K>?) -> List<Triple<String, UiText?, DrawableResource?>> = { _, _ -> listOf() },

    notValidForPlatforms: List<Pair<PlatformType, UiPlatform?>> = listOf(),
): AbstractFieldDescriptor<LocalTime, LocalTime, K, K, String>(
    fieldClass = LocalTime::class,
    group = group,
    key = key,
    label = label,
    toolTip = toolTip,
    visible = visible,
    readOnly = readOnly,
    default = default,
    enabled = enabled,
    enabledCondition = enabledCondition,
    valid = valid,
    options = options,
    notValidForPlatforms = notValidForPlatforms,
    keyFactory = LocalTimeKeyFactory
)

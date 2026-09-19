package de.visualdigits.common.domain.model.configuration

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.common.KmpOffsetDateTime
import de.visualdigits.common.domain.model.configuration.keyfactory.DateTimeKeyFactory
import de.visualdigits.common.domain.model.platform.PlatformType
import de.visualdigits.common.domain.model.ui.UiPlatform
import de.visualdigits.common.domain.model.ui.UiText
import org.jetbrains.compose.resources.DrawableResource

/**
 * Represents a field which should provide a file or directory picker.
 */
class DateTimeFieldDescriptor<K : FieldKey<K>, FK : FieldKey<FK>>(
    group: UiText? = null,

    key: K,

    label: UiText,
    toolTip: UiText? = null,

    visible: Boolean = true,
    width: Dp = 300.dp,
    readOnly: Boolean = false,

    default: KmpOffsetDateTime? = null,

    enabled: Boolean = true,

    enabledCondition: (AbstractConfiguration<*, K>, Any?) -> Boolean = { _, _ -> true },

    valid: (AbstractConfiguration<*, K>, Any?) -> Severity = { _, _ -> Severity.Info },
    options: (AbstractConfiguration<*, K>, AbstractConfiguration<*, K>?) -> List<Triple<String, UiText?, DrawableResource?>> = { _, _ -> listOf() },

    notValidForPlatforms: List<Pair<PlatformType, UiPlatform?>> = listOf(),
): AbstractFieldDescriptor<KmpOffsetDateTime, KmpOffsetDateTime, K, K, String>(
    fieldClass = KmpOffsetDateTime::class,
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
    keyFactory = DateTimeKeyFactory
)

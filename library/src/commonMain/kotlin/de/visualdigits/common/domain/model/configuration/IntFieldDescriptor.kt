package de.visualdigits.common.domain.model.configuration

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.configuration.keyfactory.IntKeyFactory
import de.visualdigits.common.domain.model.platform.PlatformType
import de.visualdigits.common.domain.model.ui.UiPlatform
import de.visualdigits.common.domain.model.ui.UiText

/**
 * Represents a field which is rendered as a text field in the UI.
 */
class IntFieldDescriptor<K : FieldKey<K>, FK : FieldKey<FK>>(
    group: UiText? = null,

    key: K,

    label: UiText,
    toolTip: UiText? = null,

    visible: Boolean = true,
    width: Dp = 300.dp,
    readOnly: Boolean = false,

    default: Int? = null,

    enabled: Boolean = true,

    enabledCondition: (AbstractConfiguration<*, K>, Any?) -> Boolean = { _, _ -> true },

    notValidForPlatforms: List<Pair<PlatformType, UiPlatform?>> = listOf(),

    valid: (AbstractConfiguration<*, K>, Any?) -> Severity = { _, _ -> Severity.Info },
): AbstractFieldDescriptor<Int, Int, K, K, Int>(
    fieldClass = Int::class,
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
    notValidForPlatforms = notValidForPlatforms,
    keyFactory = IntKeyFactory,
)

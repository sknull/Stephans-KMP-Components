package de.visualdigits.common.domain.model.configuration

import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.color.HsvColor
import de.visualdigits.common.domain.model.configuration.keyfactory.HsvColorKeyFactory
import de.visualdigits.common.domain.model.platform.PlatformType
import de.visualdigits.common.domain.model.ui.UiPlatform
import de.visualdigits.common.domain.model.ui.UiText

/**
 * Represents a field which is rendered as a text field in the UI.
 */
class ColorPickerFieldDescriptor<K : FieldKey<K>, FK : FieldKey<FK>>(
    group: UiText? = null,

    key: K,

    label: UiText,
    toolTip: UiText? = null,

    visible: Boolean = true,
    width: Dp = 300.dp,
    readOnly: Boolean = false,

    default: HsvColor? = null,

    enabled: Boolean = true,

    enabledCondition: (AbstractConfiguration<*, K>, Any?) -> Boolean = { _, _ -> true },

    notValidForPlatforms: List<Pair<PlatformType, UiPlatform?>> = listOf(),

    valid: (AbstractConfiguration<*, K>, Any?) -> Severity = { _, _ -> Severity.Info },
): AbstractFieldDescriptor<HsvColor, HsvColor, K, K, String>(
    fieldClass = HsvColor::class,
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
    keyFactory = HsvColorKeyFactory
)

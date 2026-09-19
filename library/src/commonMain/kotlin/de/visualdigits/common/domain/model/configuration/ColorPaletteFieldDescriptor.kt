package de.visualdigits.common.domain.model.configuration

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.color.ColorPalette
import de.visualdigits.common.domain.model.configuration.keyfactory.ColorKeyFactory
import de.visualdigits.common.domain.model.platform.PlatformType
import de.visualdigits.common.domain.model.ui.UiPlatform
import de.visualdigits.common.domain.model.ui.UiText

/**
 * Represents a field which is rendered as a text field in the UI.
 */
class ColorPaletteFieldDescriptor<K : FieldKey<K>, FK : FieldKey<FK>>(
    group: UiText? = null,

    key: K,

    label: UiText,
    toolTip: UiText? = null,

    visible: Boolean = true,
    width: Dp = 300.dp,
    readOnly: Boolean = false,

    default: Color? = null,

    enabled: Boolean = true,

    val colorPalette: ColorPalette,

    enabledCondition: (AbstractConfiguration<*, K>, Any?) -> Boolean = { _, _ -> true },

    notValidForPlatforms: List<Pair<PlatformType, UiPlatform?>> = listOf(),

    valid: (AbstractConfiguration<*, K>, Any?) -> Severity = { _, _ -> Severity.Info },
): AbstractFieldDescriptor<Color, Color, K, K, String>(
    fieldClass = Color::class,
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
    keyFactory = ColorKeyFactory
)

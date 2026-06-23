package de.visualdigits.common.domain.model.configuration

import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.configuration.keyfactory.FileKeyFactory
import de.visualdigits.common.domain.model.ui.FileMode
import de.visualdigits.common.domain.model.ui.UiText
import kotlinx.io.files.Path
import org.jetbrains.compose.resources.DrawableResource

/**
 * Represents a field which should provide a file or directory picker.
 */
class FileFieldDescriptor<K : FieldKey<K>, FK : FieldKey<FK>>(
    group: UiText? = null,

    key: K,

    label: UiText,
    toolTip: UiText? = null,

    visible: Boolean = true,
    readOnly: Boolean = false,

    default: Path? = null,

    enabled: Boolean = true,

    enabledCondition: (AbstractConfiguration<*, K>, Any?) -> Boolean = { _, _ -> true },

    valid: (AbstractConfiguration<*, K>, Any?) -> Severity = { _, _ -> Severity.Info },
    options: (AbstractConfiguration<*, K>, AbstractConfiguration<*, K>?) -> List<Triple<String, UiText?, DrawableResource?>> = { _, _ -> listOf() },

    val fileMode: FileMode,
    var startDirectory: (AbstractConfiguration<*, *>) -> Path = {
        Path(System.getProperty("user.home"))
    },
): AbstractFieldDescriptor<Path, Path, K, K, String>(
    fieldClass = Path::class,
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
    keyFactory = FileKeyFactory
)

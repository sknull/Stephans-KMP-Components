package de.visualdigits.common.domain.model.form

import de.visualdigits.common.domain.model.ui.UiText
import org.jetbrains.compose.resources.DrawableResource

data class ConfigurationPanelResources(
    val labelEdit: UiText,
    val iconEdit: DrawableResource? = null,

    val labelDelete: UiText,
    val iconDelete: DrawableResource? = null,

    val placeholderFieldUnset: UiText,
    val iconInfo: DrawableResource? = null,
    val tooltipReadonly: UiText,
    val iconFolder: DrawableResource? = null,
    val tooltipOpenInExplorer: UiText,

    val labelOk: UiText,
    val iconOk: DrawableResource? = null,

    val iconWarning: DrawableResource? = null,
    val warningDelete: UiText,
    val iconSave: DrawableResource? = null,

    val labelCancel: UiText,
    val iconCancel: DrawableResource? = null,
) {
    companion object {
        val DEFAULT_RESOURCES = ConfigurationPanelResources(
            labelEdit = UiText.DynamicString("Edit"),
            labelDelete = UiText.DynamicString("Delete"),
            placeholderFieldUnset = UiText.DynamicString("UNSET"),
            tooltipReadonly = UiText.DynamicString("Readonly"),
            tooltipOpenInExplorer = UiText.DynamicString("Open In Explorer"),
            labelOk = UiText.DynamicString("Ok"),
            warningDelete = UiText.DynamicString("DELETE ?"),
            labelCancel = UiText.DynamicString("Cancel"),
        )
    }
}

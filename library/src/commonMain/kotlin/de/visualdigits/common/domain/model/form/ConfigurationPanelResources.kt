package de.visualdigits.common.domain.model.form

import de.visualdigits.common.domain.model.ui.UiText
import org.jetbrains.compose.resources.DrawableResource

data class ConfigurationPanelResources(
    val label_edit: UiText,
    val icon_edit: DrawableResource? = null,

    val label_delete: UiText,
    val icon_delete: DrawableResource? = null,

    val placeholder_field_unset: UiText,
    val icon_info: DrawableResource? = null,
    val tooltip_readonly: UiText,
    val icon_folder: DrawableResource? = null,
    val tooltip_open_in_explorer: UiText,

    val label_ok: UiText,
    val icon_ok: DrawableResource? = null,

    val icon_warning: DrawableResource? = null,
    val warning_delete: UiText,
    val icon_save: DrawableResource? = null,

    val label_cancel: UiText,
    val icon_cancel: DrawableResource? = null,
) {
    companion object {
        val DEFAULT_RESOURCES = ConfigurationPanelResources(
            label_edit = UiText.DynamicString("Edit"),
            label_delete = UiText.DynamicString("Delete"),
            placeholder_field_unset = UiText.DynamicString("UNSET"),
            tooltip_readonly = UiText.DynamicString("Readonly"),
            tooltip_open_in_explorer = UiText.DynamicString("Open In Explorer"),
            label_ok = UiText.DynamicString("Ok"),
            warning_delete = UiText.DynamicString("DELETE ?"),
            label_cancel = UiText.DynamicString("Cancel"),
        )
    }
}

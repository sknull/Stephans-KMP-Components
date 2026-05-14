package de.visualdigits.common.domain.model.form

import androidx.compose.runtime.Immutable
import de.visualdigits.common.domain.model.UiText
import org.jetbrains.compose.resources.DrawableResource

@Immutable
data class EditableListResources(
    val titleAdd: UiText,
    val titleEdit: UiText,

    val tooltipAdd: UiText,
    val iconAdd: DrawableResource? = null,

    val toolTipEdit: UiText,
    val iconEdit: DrawableResource? = null,

    val toolTipDelete: UiText,
    val iconDelete: DrawableResource? = null,

    val labelOk: UiText,
    val iconOk: DrawableResource? = null,

    val labelCancel: UiText,
    val iconCancel: DrawableResource? = null,
) {
    companion object {
        val DEFAULT_RESOURCES = EditableListResources(
            tooltipAdd = UiText.DynamicString("Add..."),
            titleAdd = UiText.DynamicString("Add"),
            titleEdit = UiText.DynamicString("Edit"),
            toolTipDelete = UiText.DynamicString("Delete"),
            toolTipEdit = UiText.DynamicString("Edit"),
            labelOk = UiText.DynamicString("Ok"),
            labelCancel = UiText.DynamicString("Cancel")
        )
    }

}

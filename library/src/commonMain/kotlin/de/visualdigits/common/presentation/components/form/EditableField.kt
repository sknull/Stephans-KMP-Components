package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.KeyValue
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.color
import de.visualdigits.common.domain.model.configuration.AbstractConfiguration
import de.visualdigits.common.domain.model.configuration.AbstractFieldDescriptor
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.ListFieldDescriptor
import de.visualdigits.common.domain.model.configuration.SpacerFieldDescriptor
import de.visualdigits.common.domain.model.form.EditableListResources
import kotlin.collections.get

@Composable
fun EditableField(
    configuration: AbstractConfiguration<*,*>,
    titleChooseDirectory: UiText,
    titleChooseFile: UiText,
    iconFolder: Painter,
    editableListResources: EditableListResources,
    fieldKey: FieldKey<*>,
    fieldHeight: Dp,
    space: Dp,
    unfocusedBorderColor: Color,
    focusedBorderColor: Color,
    iconTint: Color,
    buttonColor: Color,
    buttonShape: Shape,
    containerShape: Shape,
    textStyle: TextStyle,
    onValueChange: (KeyValue) -> Unit,
    deleteAllowed: (AbstractFieldDescriptor<*,*,*>?, String) -> Boolean
) {
    val field = configuration.lookupMap[fieldKey]
    val isEditable = !(field?.descriptor?.readOnly?:false)
    if (field?.valid(field.value) == true) Color.Unspecified else Severity.Error.color()

    when(field?.descriptor) {
        is ListFieldDescriptor -> {
            EditableList(
                titleChooseDirectory = titleChooseDirectory,
                titleChooseFile = titleChooseFile,
                iconFolder = iconFolder,
                resources = editableListResources,
                configuration = configuration,
                fieldKey = fieldKey,
                fieldHeight = fieldHeight,
                space = space,
                focusedBorderColor = focusedBorderColor,
                unfocusedBorderColor = unfocusedBorderColor,
                iconTint = iconTint,
                buttonShape = buttonShape,
                containerShape = containerShape,
                buttonColor = buttonColor,
                textStyle = textStyle,
                onValueChange = onValueChange,
                deleteAllowed = deleteAllowed
            )
        }

        is SpacerFieldDescriptor ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            )

        else -> {
            TypeAwareEditableField(
                modifier = Modifier
                    .fillMaxWidth(),
                titleChooseDirectory = titleChooseDirectory,
                titleChooseFile = titleChooseFile,
                iconFolder = iconFolder,
                configuration = configuration,
                fieldKey = fieldKey,
                fieldHeight = fieldHeight,
                focusedBorderColor = focusedBorderColor,
                unfocusedBorderColor = unfocusedBorderColor,
                textStyle = textStyle,
                iconTint = iconTint,
                buttonShape = buttonShape,
                buttonColor = buttonColor,
                enabled = isEditable,
                onValueChange = onValueChange
            )
        }
    }
}

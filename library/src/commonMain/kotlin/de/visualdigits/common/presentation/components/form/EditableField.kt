package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.SwitchColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import de.visualdigits.common.domain.model.KeyValue
import de.visualdigits.common.domain.model.UiText
import de.visualdigits.common.domain.model.configuration.AbstractFieldDescriptor
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.configuration.ListFieldDescriptor
import de.visualdigits.common.domain.model.configuration.SpacerFieldDescriptor
import de.visualdigits.common.domain.model.form.EditableListResources
import de.visualdigits.common.presentation.components.util.switchBoxColors

@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>>  EditableField(
    fieldState: FieldState<K, FK>,
    titleChooseDirectory: UiText,
    titleChooseFile: UiText,
    iconFolder: Painter,
    editableListResources: EditableListResources,
    fieldHeight: Dp,
    space: Dp,
    switchColors: SwitchColors = switchBoxColors(),
    unfocusedBorderColor: Color,
    focusedBorderColor: Color,
    visibilityIcon: Painter? = null,
    iconTint: Color,
    buttonColor: Color,
    buttonShape: Shape,
    containerShape: Shape,
    textStyle: TextStyle,
    colorPickerUseOnlySliders: Boolean = false,
    onValueChange: (KeyValue) -> Unit,
    deleteAllowed: (AbstractFieldDescriptor<*,*,*,*,*>?, String) -> Boolean
) {
    when(fieldState.fieldDescriptor) {
        is ListFieldDescriptor<*,*> -> {
            EditableList(
                fieldState = fieldState,
                titleChooseDirectory = titleChooseDirectory,
                titleChooseFile = titleChooseFile,
                iconFolder = iconFolder,
                resources = editableListResources,
                fieldHeight = fieldHeight,
                space = space,
                switchColors = switchColors,
                focusedBorderColor = focusedBorderColor,
                unfocusedBorderColor = unfocusedBorderColor,
                iconTint = iconTint,
                buttonShape = buttonShape,
                containerShape = containerShape,
                buttonColor = buttonColor,
                visibilityIcon = visibilityIcon,
                textStyle = textStyle,
                colorPickerUseOnlySliders = colorPickerUseOnlySliders,
                onValueChange = onValueChange,
                deleteAllowed = deleteAllowed
            )
        }

        is SpacerFieldDescriptor<*,*> ->
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            )

        else -> {
            TypeAwareEditableField(
                modifier = Modifier
                    .fillMaxWidth(),
                fieldState = fieldState,
                titleChooseDirectory = titleChooseDirectory,
                titleChooseFile = titleChooseFile,
                iconFolder = iconFolder,
                fieldHeight = fieldHeight,
                switchColors = switchColors,
                focusedBorderColor = focusedBorderColor,
                unfocusedBorderColor = unfocusedBorderColor,
                textStyle = textStyle,
                visibilityIcon = visibilityIcon,
                iconTint = iconTint,
                buttonShape = buttonShape,
                buttonColor = buttonColor,
                colorPickerUseOnlySliders = colorPickerUseOnlySliders,
                onValueChange = onValueChange
            )
        }
    }
}

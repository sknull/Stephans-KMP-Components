package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.visualdigits.common.domain.model.configuration.AbstractFieldDescriptor
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.configuration.ListFieldDescriptor
import de.visualdigits.common.domain.model.configuration.SpacerFieldDescriptor
import de.visualdigits.common.domain.model.ui.KeyValue

@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>>  EditableField(
    fieldState: FieldState<K, FK>,
    colorPickerUseOnlySliders: Boolean = false,
    onValueChange: (KeyValue) -> Unit,
    deleteAllowed: (AbstractFieldDescriptor<*,*,*,*,*>?, String) -> Boolean
) {
    when(fieldState.fieldDescriptor) {
        is ListFieldDescriptor<*,*> -> {
            EditableList(
                fieldState = fieldState,
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
                colorPickerUseOnlySliders = colorPickerUseOnlySliders,
                onValueChange = onValueChange
            )
        }
    }
}

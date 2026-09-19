package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import de.visualdigits.common.domain.model.configuration.ColorPaletteFieldDescriptor
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.form.LocalFormFieldResources
import de.visualdigits.common.presentation.components.ColorPalettePicker
import de.visualdigits.common.presentation.components.util.conditional
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight
import de.visualdigits.common.presentation.components.util.outlinedTextFieldColors

@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> ColorPaletteBox(
    modifier: Modifier = Modifier,
    fieldState: FieldState<K, FK>,
    alignForForm: Boolean = true,
    onValueChange: (Color?) -> Unit,
) {
    val formFieldResources = LocalFormFieldResources.current
    val textFieldState = rememberTextFieldState(" ")
    val halfHeight = minimizedLabelHalfHeight(formFieldResources.textStyle)

    Column(
        modifier = modifier
            .conditional(!alignForForm) { offset(y = halfHeight * -1.0f) }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = formFieldResources.textStyle,
            label = {
                Text(
                    text = fieldState.fieldDescriptor.label.asString(),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            enabled = fieldState.fieldDescriptor.enabled,
            shape = formFieldResources.shape,
            readOnly = true,
            state = textFieldState,
            leadingIcon = {
                val paletteColors = (fieldState.fieldDescriptor as ColorPaletteFieldDescriptor<*, *>).colorPalette.colors
                ColorPalettePicker(
                    modifier = Modifier,
                    initialColor = fieldState.currentValue as? Color,
                    paletteColors = paletteColors,
                ) { color ->
                    onValueChange(color)
                }
            },
            colors = outlinedTextFieldColors(
                formFieldResources.focusedBorderColor,
                formFieldResources.unfocusedBorderColor,
                formFieldResources.focusedContainerColor,
                formFieldResources.unfocusedContainerColor
            )
        )
    }
}

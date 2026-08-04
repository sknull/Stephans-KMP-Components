package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import de.visualdigits.common.domain.model.color.HsvColor
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.form.LocalFormFieldResources
import de.visualdigits.common.domain.model.form.LocalFormResources
import de.visualdigits.common.presentation.components.ColorPicker
import de.visualdigits.common.presentation.components.util.conditional
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight
import de.visualdigits.common.presentation.components.util.outlinedTextFieldColors

@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> ColorPickerBox(
    modifier: Modifier = Modifier,
    fieldState: FieldState<K, FK>,
    label: String,
    alignForForm: Boolean = true,
    slidersOnly: Boolean = false,
    onValueChange: (HsvColor) -> Unit,
) {
    val formResources = LocalFormResources.current
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
                    text = label,
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
                ColorPicker(
                    modifier = Modifier
                        .padding(start = formResources.space * 3, top = formResources.space, end = formResources.space, bottom = formResources.space),
                    initialColor = fieldState.currentValue as? HsvColor,
                    size = formFieldResources.fieldHeight * 3,
                    space = formResources.space,
                    slidersOnly = slidersOnly,
                    hasSwatch = true
                ) { hsvColor ->
                    onValueChange(hsvColor)
                }
            },
            colors = outlinedTextFieldColors(formFieldResources.focusedBorderColor, formFieldResources.unfocusedBorderColor)
        )
    }
}

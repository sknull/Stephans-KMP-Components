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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.HsvColor
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.presentation.components.ColorPicker
import de.visualdigits.common.presentation.components.util.conditional
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight
import de.visualdigits.common.presentation.components.util.outlinedTextFieldColors

@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> ColorPickerBox(
    modifier: Modifier = Modifier,
    fieldState: FieldState<K, FK>,
    space: Dp = 8.dp,
    label: String,
    fieldHeight: Dp,
    focusedBorderColor: Color,
    unfocusedBorderColor: Color,
    buttonShape: Shape,
    textStyle: TextStyle,
    alignForForm: Boolean = true,
    onValueChange: (HsvColor) -> Unit,
) {
    val textFieldState = rememberTextFieldState(" ")
    val halfHeight = minimizedLabelHalfHeight(textStyle)

    Column(
        modifier = modifier
            .conditional(!alignForForm) { offset(y = halfHeight * -1.0f) }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            textStyle = textStyle,
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            enabled = fieldState.fieldDescriptor.enabled,
            shape = buttonShape,
            readOnly = true,
            state = textFieldState,
            leadingIcon = {
                ColorPicker(
                    modifier = Modifier
                        .padding(start = space * 3, top = space, end = space, bottom = space),
                    initialColor = fieldState.currentValue as? HsvColor,
                    size = fieldHeight * 3,
                    space = space,
                    hasSwatch = true
                ) { hsvColor ->
                    onValueChange(hsvColor)
                }
            },
            colors = outlinedTextFieldColors(focusedBorderColor, unfocusedBorderColor)
        )
    }
}

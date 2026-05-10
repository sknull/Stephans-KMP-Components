package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import de.visualdigits.common.domain.model.configuration.Field
import de.visualdigits.common.presentation.components.PlatformToolTip
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun InnerTextField(
    modifier: Modifier,
    space: Dp,
    toolTipBackgroundColor: Color,
    toolTipShape: Shape,
    fieldHeight: Dp,
    textStyle: TextStyle,
    field: Field<*,*,*>,
    enabled: Boolean = true,
    buttonShape: Shape,
    textFieldState: TextFieldState,
    expanded: Boolean,
    focusedBorderColor: Color,
    unfocusedBorderColor: Color
) {
    val halfHeight = minimizedLabelHalfHeight(textStyle)

    val toolTip = field.descriptor.toolTip?.let { t -> t.asString() }
    PlatformToolTip(
        text = toolTip,
        space = space,
        backgroundColor = toolTipBackgroundColor,
        shape = toolTipShape
    ) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .height(fieldHeight + halfHeight),
            textStyle = textStyle.copy(fontSize = textStyle.fontSize * 0.8f),
            label = {
                Text(
                    text = field.descriptor.label.asString(),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            enabled = enabled,
            shape = buttonShape,
            readOnly = true,
            state = textFieldState,
            trailingIcon = if (enabled) {
                {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded,
                        modifier = Modifier
                            .pointerHoverIcon(PointerIcon.Hand)
                    )
                }
            } else null,
            colors = OutlinedTextFieldDefaults.colors(
                unfocusedBorderColor = unfocusedBorderColor,
                focusedBorderColor = focusedBorderColor,
                unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
                focusedTextColor = MaterialTheme.colorScheme.onPrimary,
                cursorColor = MaterialTheme.colorScheme.onSurface,
                unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,
                focusedLabelColor = focusedBorderColor
            )
        )
    }
}

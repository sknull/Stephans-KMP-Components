package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.presentation.components.PlatformToolTip
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight
import de.visualdigits.common.presentation.components.util.outlinedTextFieldColors

@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> TextBox(
    modifier: Modifier,
    fieldState: FieldState<K, FK>,
    currentValue: Any?,
    space: Dp,
    toolTipBackgroundColor: Color,
    toolTipShape: Shape,
    fieldHeight: Dp,
    textStyle: TextStyle,
    buttonShape: Shape,
    finalUnfocusedBorderColor: Color,
    focusedBorderColor: Color,
    onValueChange: (String) -> Unit
) {
    val text = currentValue?.toString() ?: fieldState.currentValue?.toString() ?: ""
    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = text)) }

    LaunchedEffect(text) {
        if (text != textFieldValue.text) {
            textFieldValue = textFieldValue.copy(text = text)
        }
    }
    PlatformToolTip(
        text = fieldState.fieldDescriptor.toolTip?.asString(),
        space = space,
        backgroundColor = toolTipBackgroundColor,
        shape = toolTipShape
    ) {
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .height(fieldHeight + minimizedLabelHalfHeight(textStyle)),
            textStyle = textStyle,
            enabled = fieldState.fieldDescriptor.enabled && fieldState.fieldDescriptor.enabledCondition(fieldState.configuration, null),
            label = {
                Text(
                    text = fieldState.fieldDescriptor.label.asString(),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            value = textFieldValue,
            shape = buttonShape,
            onValueChange = { value ->
                textFieldValue = value
                onValueChange(value.text)
            },
            singleLine = true,
            colors = outlinedTextFieldColors(focusedBorderColor, finalUnfocusedBorderColor)
        )
    }
}

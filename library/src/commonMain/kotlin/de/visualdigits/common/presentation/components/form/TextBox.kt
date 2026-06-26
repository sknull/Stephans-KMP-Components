package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
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
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.presentation.components.PlatformToolTip
import de.visualdigits.common.presentation.components.util.conditional
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight
import de.visualdigits.common.presentation.components.util.outlinedTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
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
    alignForForm: Boolean = true,
    buttonShape: Shape,
    finalUnfocusedBorderColor: Color,
    focusedBorderColor: Color,
    onValueChange: (String) -> Unit
) {
    val text = currentValue?.toString() ?: fieldState.currentValue?.toString() ?: ""

    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = text)) }
    val interactionSource = remember { MutableInteractionSource() }

    val primaryColor = MaterialTheme.colorScheme.primary
    val textSelectionColors = remember {
        TextSelectionColors(
            handleColor = primaryColor,
            backgroundColor = primaryColor.copy(alpha = 0.4f)
        )
    }
    val halfHeight = minimizedLabelHalfHeight(textStyle)

    LaunchedEffect(text) {
        if (text != textFieldValue.text) {
            textFieldValue = textFieldValue.copy(text = text)
        }
    }

    PlatformToolTip(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = halfHeight * 3 / 4)
            .conditional(!alignForForm) { offset(y = halfHeight * -1.0f) },
        text = fieldState.fieldDescriptor.toolTip?.asString(),
        space = space,
        backgroundColor = toolTipBackgroundColor,
        shape = toolTipShape,
        content = {
            CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
                BasicTextField(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(fieldHeight + halfHeight / 4),
                    value = textFieldValue,
                    onValueChange = { value ->
                        textFieldValue = value
                        onValueChange(value.text)
                    },
                    textStyle = textStyle,
                    singleLine = true,
                    enabled = fieldState.fieldDescriptor.enabled && fieldState.fieldDescriptor.enabledCondition(fieldState.configuration, null),
                    interactionSource = interactionSource,
                    // Der Cursor lässt sich hier ebenfalls passend färben
                    cursorBrush = androidx.compose.ui.graphics.SolidColor(primaryColor),
                    decorationBox = { innerTextField ->
                        // 2. Die DecorationBox stellt das exakte Material-Design-Gewand bereit
                        OutlinedTextFieldDefaults.DecorationBox(
                            value = textFieldValue.text,
                            innerTextField = innerTextField,
                            enabled = fieldState.fieldDescriptor.enabled,
                            singleLine = true,
                            visualTransformation = VisualTransformation.None,
                            interactionSource = interactionSource,
                            label = {
                                Text(
                                    text = fieldState.fieldDescriptor.label.asString(),
                                    style = MaterialTheme.typography.bodySmall,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            colors = outlinedTextFieldColors(
                                focusedBorderColor = focusedBorderColor,
                                unfocusedBorderColor = finalUnfocusedBorderColor
                            ),
                            container = {
                                // Zeichnet den eigentlichen Rahmen (Outline) mit deiner Form
                                OutlinedTextFieldDefaults.ContainerBox(
                                    enabled = fieldState.fieldDescriptor.enabled,
                                    isError = false,
                                    interactionSource = interactionSource,
                                    colors = outlinedTextFieldColors(focusedBorderColor, finalUnfocusedBorderColor),
                                    shape = buttonShape
                                )
                            }
                        )
                    }
                )
            }
        }
    )
}

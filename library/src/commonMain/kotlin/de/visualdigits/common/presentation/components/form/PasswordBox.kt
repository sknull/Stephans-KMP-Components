package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.presentation.components.PlatformToolTip
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight
import de.visualdigits.common.presentation.components.util.outlinedTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> PasswordBox(
    modifier: Modifier,
    fieldState: FieldState<K, FK>,
    currentValue: Any?,
    space: Dp,
    toolTipBackgroundColor: Color,
    toolTipShape: Shape,
    fieldHeight: Dp,
    textStyle: TextStyle,
    buttonShape: Shape,
    visibilityIcon: Painter?,
    finalUnfocusedBorderColor: Color,
    focusedBorderColor: Color,
    onValueChange: (String) -> Unit
) {
    val text = currentValue?.toString() ?: fieldState.currentValue?.toString() ?: ""

    var textFieldValue by remember { mutableStateOf(TextFieldValue(text = text)) }
    val interactionSource = remember { MutableInteractionSource() }
    var passwordVisible by remember { mutableStateOf(false) }

    val visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation()
    val primaryColor = MaterialTheme.colorScheme.primary
    val textSelectionColors = remember {
        TextSelectionColors(
            handleColor = primaryColor,
            backgroundColor = primaryColor.copy(alpha = 0.4f)
        )
    }

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
        CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
            BasicTextField(
                value = textFieldValue,
                onValueChange = { value ->
                    textFieldValue = value
                    onValueChange(value.text)
                },
                modifier = modifier
                    .fillMaxWidth()
                    .height(fieldHeight + minimizedLabelHalfHeight(textStyle)),
                textStyle = textStyle,
                singleLine = true,
                enabled = fieldState.fieldDescriptor.enabled && fieldState.fieldDescriptor.enabledCondition(fieldState.configuration, null),
                interactionSource = interactionSource,
                // Wichtig für Passwort-Felder: Maskierung und Keyboard-Typ an das Basis-Feld übergeben
                cursorBrush = androidx.compose.ui.graphics.SolidColor(primaryColor),
                visualTransformation = visualTransformation,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                decorationBox = { innerTextField ->
                    OutlinedTextFieldDefaults.DecorationBox(
                        value = textFieldValue.text,
                        innerTextField = innerTextField,
                        enabled = fieldState.fieldDescriptor.enabled,
                        singleLine = true,
                        // Transformation muss auch an die DecorationBox für die korrekte Label-Animation
                        visualTransformation = visualTransformation,
                        interactionSource = interactionSource,
                        label = {
                            Text(
                                text = fieldState.fieldDescriptor.label.asString(),
                                style = MaterialTheme.typography.bodySmall,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                        },
                        // Hier wird dein funktionierendes TrailingIcon für die Sichtbarkeit platziert
                        trailingIcon = {
                            visibilityIcon?.let { vi ->
                                Icon(
                                    modifier = Modifier
                                        .pointerHoverIcon(PointerIcon.Hand)
                                        .hoverable(interactionSource)
                                        .clickable {
                                            passwordVisible = !passwordVisible
                                        },
                                    painter = vi,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        },
                        colors = outlinedTextFieldColors(
                            focusedBorderColor = focusedBorderColor,
                            unfocusedBorderColor = finalUnfocusedBorderColor
                        ),
                        container = {
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
}

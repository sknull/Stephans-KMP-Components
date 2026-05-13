package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.KeyValue
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import org.jetbrains.compose.resources.painterResource


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun <K : FieldKey<K>, FK : FieldKey<FK>> ComboBox(
    modifier: Modifier = Modifier,
    fieldState: FieldState<K, FK>,
    space: Dp,
    toolTipBackgroundColor: Color,
    toolTipShape: Shape,
    textStyle: TextStyle,
    fieldHeight: Dp = Dp.Unspecified,
    focusedBorderColor: Color = MaterialTheme.colorScheme.outline,
    unfocusedBorderColor: Color = MaterialTheme.colorScheme.onSurface,
    buttonShape: Shape,
    onValueChange: (Any?) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val textFieldState = rememberTextFieldState(fieldState.currentOptionUIText.asString())
    val text = fieldState.currentOptionUIText.asString()
    LaunchedEffect(text) {
        textFieldState.edit {
            replace(0, length, text)
        }
    }
    if (fieldState.fieldDescriptor.enabled) {
        ExposedDropdownMenuBox(
            modifier = Modifier
                .fillMaxWidth(),
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            InnerTextField(
                modifier = modifier
                    .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                    .exposedDropdownSize(),
                fieldState = fieldState,
                space = space,
                toolTipBackgroundColor = toolTipBackgroundColor,
                toolTipShape = toolTipShape,
                textStyle = textStyle,
                fieldHeight = fieldHeight,
                buttonShape = buttonShape,
                textFieldState = textFieldState,
                expanded = expanded,
                unfocusedBorderColor = unfocusedBorderColor,
                focusedBorderColor = focusedBorderColor
            )
            ExposedDropdownMenu(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.secondary),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                fieldState.options.forEach { option ->
                    // todo this is probably incorrect due to changes in field descriptor
                    val text = option.second?.asString() ?:""
                    DropdownMenuItem(
                        modifier = Modifier
                            .height(30.dp)
                            .pointerHoverIcon(PointerIcon.Hand),
                        onClick = {
                            onValueChange(option.first)
                            textFieldState.setTextAndPlaceCursorAtEnd(text)
                            expanded = false
                        },
                        leadingIcon = {
                            option.third?.let { icon ->
                                Image(
                                    painter = painterResource(icon),
                                    contentDescription = option.second?.asString()?:option.first?.toString(),
                                    modifier = Modifier
                                        .height(30.dp)
                                )
                            }
                        },
                        text = {
                            val text1 = option.second?.asString() ?: option.first.toString()
                            option.second?.let { t ->
                                Text(
                                    modifier = Modifier
                                        .height(30.dp),
                                    text = t.asString(),
                                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSecondary)
                                )
                            } ?: Text(
                                modifier = Modifier
                                    .height(30.dp),
                                text = text1,
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onSecondary)
                            )
                        }
                    )
                }
            }
        }
    } else {
        InnerTextField(
            modifier = modifier,
            fieldState = fieldState,
            enabled = false,
            space = space,
            toolTipBackgroundColor = toolTipBackgroundColor,
            toolTipShape = toolTipShape,
            textStyle = textStyle,
            fieldHeight = fieldHeight,
            buttonShape = buttonShape,
            textFieldState = textFieldState,
            expanded = expanded,
            focusedBorderColor = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColor
        )
    }
}

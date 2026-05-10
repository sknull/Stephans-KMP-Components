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
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.KeyValue
import de.visualdigits.common.domain.model.configuration.AbstractConfiguration
import de.visualdigits.common.domain.model.configuration.Field
import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.painterResource


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ComboBox(
    modifier: Modifier = Modifier,
    space: Dp,
    toolTipBackgroundColor: Color,
    toolTipShape: Shape,
    textStyle: TextStyle,
    configuration: AbstractConfiguration<*,*>,
    field: Field<*,*,*>,
    fieldHeight: Dp = Dp.Unspecified,
    enabled: Boolean = true,
    focusedBorderColor: Color = MaterialTheme.colorScheme.outline,
    unfocusedBorderColor: Color = MaterialTheme.colorScheme.onSurface,
    buttonShape: Shape,
    options: List<Triple<String, StringResource?, DrawableResource?>>? = null,
    onValueChange: (KeyValue) -> Unit,
) {
    var expanded by remember { mutableStateOf(false) }
    val currentOption = field.currentOption(configuration)?.second?.asString()?:field.currentOption(configuration)?.first?:""
    val textFieldState = rememberTextFieldState(currentOption)
    LaunchedEffect(currentOption) {
        textFieldState.edit {
            // Ersetzt den aktuellen Text durch den neuen Wert aus dem Model
            replace(0, length, currentOption)
        }
    }
    if (enabled) {
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
                space = space,
                toolTipBackgroundColor = toolTipBackgroundColor,
                toolTipShape = toolTipShape,
                textStyle = textStyle,
                fieldHeight = fieldHeight,
                field = field,
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
                options?: field.descriptor.options(configuration).forEach { option ->
                    val text = option.second?.asString() ?:""
                    DropdownMenuItem(
                        modifier = Modifier
                            .height(30.dp)
                            .pointerHoverIcon(PointerIcon.Hand),
                        onClick = {
                            onValueChange(KeyValue(field.descriptor, option.first))
                            textFieldState.setTextAndPlaceCursorAtEnd(text)
                            expanded = false
                        },
                        leadingIcon = {
                            option.third?.let { icon ->
                                Image(
                                    painter = painterResource(icon),
                                    contentDescription = option.first,
                                    modifier = Modifier
                                        .height(30.dp)
                                )
                            }
                        },
                        text = {
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
                                text = option.first,
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
            space = space,
            toolTipBackgroundColor = toolTipBackgroundColor,
            toolTipShape = toolTipShape,
            textStyle = textStyle,
            fieldHeight = fieldHeight,
            field = field,
            enabled = false,
            buttonShape = buttonShape,
            textFieldState = textFieldState,
            expanded = expanded,
            focusedBorderColor = focusedBorderColor,
            unfocusedBorderColor = unfocusedBorderColor
        )
    }
}

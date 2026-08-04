package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
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
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.form.LocalFormFieldResources
import de.visualdigits.common.presentation.components.util.conditional
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight
import de.visualdigits.common.presentation.components.util.outlinedTextFieldColors
import org.jetbrains.compose.resources.painterResource


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun <K : FieldKey<K>, FK : FieldKey<FK>> ComboBox(
    modifier: Modifier = Modifier,
    fieldState: FieldState<K, FK>,
    alignForForm: Boolean = true,
    onValueChange: (Any?) -> Unit,
) {
    val formFieldResources = LocalFormFieldResources.current
    var expanded by remember { mutableStateOf(false) }
    val text = fieldState.currentOptionUIText.asString()
    val textFieldState = rememberTextFieldState(text)
    val halfHeight = minimizedLabelHalfHeight(formFieldResources.textStyle)
    LaunchedEffect(text) {
        textFieldState.edit {
            replace(0, length, text)
        }
    }
    if (fieldState.fieldDescriptor.enabled && fieldState.fieldDescriptor.enabledCondition(fieldState.configuration, null)) {
        ExposedDropdownMenuBox(
            modifier = Modifier
                .fillMaxWidth()
                .conditional(!alignForForm) { offset(y = halfHeight * -1.0f) },
            expanded = expanded,
            onExpandedChange = { expanded = !expanded },
        ) {
            val halfHeight = minimizedLabelHalfHeight(textStyle = formFieldResources.textStyle)
            OutlinedTextField(
                modifier = modifier
                            .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                            .exposedDropdownSize()
                    .fillMaxWidth()
                    .height(formFieldResources.fieldHeight + halfHeight),
                textStyle = formFieldResources.textStyle.copy(fontSize = formFieldResources.textStyle.fontSize * 0.8f),
                label = {
                    Text(
                        text = fieldState.fieldDescriptor.label.asString(),
                        style = MaterialTheme.typography.bodySmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                },
                enabled = true,
                shape = formFieldResources.shape,
                readOnly = true,
                state = textFieldState,
                trailingIcon = if (true) {
                    {
                        ExposedDropdownMenuDefaults.TrailingIcon(
                            expanded = expanded,
                            modifier = Modifier
                                .pointerHoverIcon(PointerIcon.Hand)
                        )
                    }
                } else null,
                colors = outlinedTextFieldColors(
                    focusedBorderColor = formFieldResources.focusedBorderColor,
                    unfocusedBorderColor = formFieldResources.unfocusedBorderColor
                )
            )

            ExposedDropdownMenu(
                modifier = Modifier
                    .background(MaterialTheme.colorScheme.background),
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                fieldState.options.forEach { option ->
                    val text = option.second?.asString() ?: ""
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
                                    contentDescription = option.second?.asString() ?: option.first?.toString(),
                                    modifier = Modifier
                                        .width(30.dp)
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
                                    style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground)
                                )
                            } ?: Text(
                                modifier = Modifier
                                    .height(30.dp),
                                text = text1,
                                style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground)
                            )
                        }
                    )
                }
            }
        }
    } else {
        val halfHeight = minimizedLabelHalfHeight(textStyle = formFieldResources.textStyle)
        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .height(formFieldResources.fieldHeight + halfHeight),
            textStyle = formFieldResources.textStyle.copy(fontSize = formFieldResources.textStyle.fontSize * 0.8f),
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
            trailingIcon = if (fieldState.fieldDescriptor.enabled) {
                {
                    ExposedDropdownMenuDefaults.TrailingIcon(
                        expanded = expanded,
                        modifier = Modifier
                            .pointerHoverIcon(PointerIcon.Hand)
                    )
                }
            } else null,
            colors = outlinedTextFieldColors(
                focusedBorderColor = formFieldResources.focusedBorderColor,
                unfocusedBorderColor = formFieldResources.unfocusedBorderColor
            )
        )
    }
}

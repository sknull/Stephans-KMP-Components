package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.form.LocalFormFieldResources
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight
import de.visualdigits.common.presentation.components.util.outlinedTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> TextBox(
    modifier: Modifier,
    fieldState: FieldState<K, FK>,
    currentValue: Any?,
    unfocusedBorderColor: Color,
    onValueChange: (String) -> Unit
) {
    val formFieldResources = LocalFormFieldResources.current
    val text = currentValue?.toString() ?: fieldState.currentValue?.toString() ?: ""
    val textFieldState = rememberTextFieldState(text)
    LaunchedEffect(text) {
        if (text != textFieldState.text.toString()) {
            textFieldState.edit { replace (0, length, text) }
        }
    }
    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text }
            .collect { charSequence ->
                onValueChange(charSequence.toString())
            }
    }

    val primaryColor = MaterialTheme.colorScheme.primary
    val textSelectionColors = remember {
        TextSelectionColors(
            handleColor = primaryColor,
            backgroundColor = primaryColor.copy(alpha = 0.4f)
        )
    }

    CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
        val halfHeight = minimizedLabelHalfHeight(formFieldResources.textStyle)
        OutlinedTextField(
            state = textFieldState,
            modifier = modifier
                .fillMaxWidth()
                .height(formFieldResources.fieldHeight + halfHeight),
            textStyle = formFieldResources.textStyle,
            lineLimits = TextFieldLineLimits.SingleLine,
            label = {
                Text(
                    text = fieldState.fieldDescriptor.label.asString(),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            enabled = fieldState.fieldDescriptor.enabled && fieldState.fieldDescriptor.enabledCondition(fieldState.configuration, null),
            shape = formFieldResources.shape,
            colors = outlinedTextFieldColors(formFieldResources.focusedBorderColor, unfocusedBorderColor)
        )
    }
}

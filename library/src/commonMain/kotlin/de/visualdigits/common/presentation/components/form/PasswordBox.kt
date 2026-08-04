package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.clickable
import androidx.compose.foundation.hoverable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.TextObfuscationMode
import androidx.compose.foundation.text.selection.LocalTextSelectionColors
import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedSecureTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.text.style.TextOverflow
import de.visualdigits.common.domain.model.configuration.FieldKey
import de.visualdigits.common.domain.model.configuration.FieldState
import de.visualdigits.common.domain.model.form.LocalFormFieldResources
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight
import de.visualdigits.common.presentation.components.util.outlinedTextFieldColors

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <K : FieldKey<K>, FK : FieldKey<FK>> PasswordBox(
    modifier: Modifier,
    fieldState: FieldState<K, FK>,
    currentValue: Any?,
    unfocusedBorderColor: Color,
    onValueChange: (String) -> Unit
) {
    val formFieldResources = LocalFormFieldResources.current
    val text = currentValue?.toString() ?: fieldState.currentValue?.toString() ?: ""

    val textFieldState = remember { TextFieldState(initialText = text) }
    val interactionSource = remember { MutableInteractionSource() }
    var passwordVisible by remember { mutableStateOf(false) }

    val primaryColor = MaterialTheme.colorScheme.primary
    val textSelectionColors = remember {
        TextSelectionColors(
            handleColor = primaryColor,
            backgroundColor = primaryColor.copy(alpha = 0.4f)
        )
    }

    LaunchedEffect(text) {
        if (text != textFieldState.text.toString()) {
            textFieldState.edit { replace(0, length, text) }
        }
    }

    LaunchedEffect(textFieldState) {
        snapshotFlow { textFieldState.text }
            .collect { charSequence -> onValueChange(charSequence.toString()) }
    }

    CompositionLocalProvider(LocalTextSelectionColors provides textSelectionColors) {
        val halfHeight = minimizedLabelHalfHeight(formFieldResources.textStyle)

        // V2 Passwort-Komponente nutzen
        OutlinedSecureTextField(
            state = textFieldState,
            modifier = modifier
                .fillMaxWidth()
                .height(formFieldResources.fieldHeight + halfHeight),
            textStyle = formFieldResources.textStyle,
            // Steuert die Sichtbarkeit in der V2-API
            textObfuscationMode = if (passwordVisible) {
                TextObfuscationMode.Visible
            } else {
                TextObfuscationMode.Hidden
            },
            // keyboardOptions(keyboardType = Password) ist bei SecureTextField bereits voreingestellt
            label = {
                Text(
                    text = fieldState.fieldDescriptor.label.asString(),
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            trailingIcon = {
                formFieldResources.visibilityIcon?.let { vi ->
                    Icon(
                        modifier = Modifier
                            .pointerHoverIcon(PointerIcon.Hand)
                            .hoverable(interactionSource)
                            .clickable { passwordVisible = !passwordVisible },
                        painter = vi,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            },
            enabled = fieldState.fieldDescriptor.enabled && fieldState.fieldDescriptor.enabledCondition(fieldState.configuration, null),
            shape = formFieldResources.shape,
            colors = outlinedTextFieldColors(formFieldResources.focusedBorderColor, unfocusedBorderColor)
        )
    }
}

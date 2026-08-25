package de.visualdigits.common.presentation.components.button

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun TextButton(
    modifier: Modifier = Modifier,
    paddingValues: PaddingValues = PaddingValues(0.dp),
    text: String,
    textModifier: Modifier = Modifier,
    textColorDisabled: Color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
    textColor: Color = MaterialTheme.colorScheme.onSurface,
    textStyle: TextStyle = MaterialTheme.typography.labelMedium,
    textAlign: TextAlign = TextAlign.Center,
    buttonColor: Color = MaterialTheme.colorScheme.surface,
    shape: Shape = MaterialTheme.shapes.extraSmall,
    enabled: Boolean = true,
    onClick: (() -> Unit),
) {
    val interactionSource = remember { MutableInteractionSource() }

    Button(
        modifier = modifier
            .background(buttonColor),
        enabled = enabled,
        shape = shape,
        colors = ButtonDefaults.buttonColors().copy(
            containerColor = buttonColor
        ),
        contentPadding = paddingValues,
        interactionSource = interactionSource,
        onClick = onClick,
    ) {
        Text(
            modifier = textModifier,
            text = text,
            color = if (enabled) textColor else textColorDisabled,
            style = textStyle,
            textAlign = textAlign
        )
    }
}

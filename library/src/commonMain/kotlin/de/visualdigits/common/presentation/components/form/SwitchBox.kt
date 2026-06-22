package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.configuration.keyfactory.BooleanEnum
import de.visualdigits.common.presentation.components.util.conditional
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight
import de.visualdigits.common.presentation.components.util.outlinedTextFieldColors
import de.visualdigits.common.presentation.components.util.switchBoxColors

@Composable
fun SwitchBox(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    currentValue: Any? = false,
    switchColors: SwitchColors = switchBoxColors(),
    space: Dp = 8.dp,
    label: String,
    fieldHeight: Dp = Dp.Unspecified,
    focusedBorderColor: Color = MaterialTheme.colorScheme.outline,
    unfocusedBorderColor: Color = MaterialTheme.colorScheme.onSurface,
    buttonShape: Shape = MaterialTheme.shapes.extraSmall,
    textStyle: TextStyle,
    alignForForm: Boolean = true,
    onValueChange: (BooleanEnum) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val booleanValue = when (currentValue) {
        is BooleanEnum -> currentValue.booleanValue
        is Boolean -> currentValue
        is String -> currentValue.toBoolean()
        else -> false
    }
    var checked by remember { mutableStateOf(booleanValue) }
    val textFieldState = rememberTextFieldState(" ")
    val halfHeight = minimizedLabelHalfHeight(textStyle)
    Column(
        modifier = modifier
            .fillMaxWidth()
            .conditional(!alignForForm) { offset(y = halfHeight * -1.0f) }
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .height(fieldHeight + halfHeight),
            textStyle = textStyle,
            label = {
                Text(
                    text = label,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            },
            enabled = enabled,
            shape = buttonShape,
            readOnly = true,
            state = textFieldState,
            leadingIcon = {
                Row {
                    Spacer(Modifier.width(space * 2))
                    Switch(
                        checked = checked,
                        onCheckedChange = { v ->
                            checked = v
                            onValueChange(BooleanEnum.fromValue(v)!!)
                        },
                        interactionSource = interactionSource,
                        colors = switchColors
                    )
                }
            },
            colors = outlinedTextFieldColors(focusedBorderColor, unfocusedBorderColor)
        )
    }
}

package de.visualdigits.common.presentation.components.form

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import de.visualdigits.common.domain.model.configuration.keyfactory.BooleanEnum
import de.visualdigits.common.domain.model.form.LocalFormFieldResources
import de.visualdigits.common.domain.model.form.LocalFormResources
import de.visualdigits.common.presentation.components.util.LocalSwitchColors
import de.visualdigits.common.presentation.components.util.minimizedLabelHalfHeight
import de.visualdigits.common.presentation.components.util.outlinedTextFieldColors

@Composable
fun SwitchBox(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    currentValue: Any? = false,
    label: String,
    onValueChange: (BooleanEnum) -> Unit
) {
    val formResources = LocalFormResources.current
    val formFieldResources = LocalFormFieldResources.current
    val interactionSource = remember { MutableInteractionSource() }
    val booleanValue = when (currentValue) {
        is BooleanEnum -> currentValue.booleanValue
        is Boolean -> currentValue
        is String -> currentValue.toBoolean()
        else -> false
    }
    var checked by remember { mutableStateOf(booleanValue) }
    val textFieldState = rememberTextFieldState(" ")
    val halfHeight = minimizedLabelHalfHeight(formFieldResources.textStyle)
    OutlinedTextField(
        modifier = modifier
            .fillMaxWidth()
            .height(formFieldResources.fieldHeight + halfHeight),
        textStyle = formFieldResources.textStyle,
        label = {
            Text(
                text = label,
                style = MaterialTheme.typography.bodySmall,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        enabled = enabled,
        shape = formFieldResources.shape,
        readOnly = true,
        state = textFieldState,
        leadingIcon = {
            Row {
                Spacer(Modifier.width(formResources.space * 2))
                Switch(
                    checked = checked,
                    onCheckedChange = { v ->
                        checked = v
                        onValueChange(BooleanEnum.fromValue(v)!!)
                    },
                    interactionSource = interactionSource,
                    colors = LocalSwitchColors.current
                )
            }
        },
        colors = outlinedTextFieldColors(formFieldResources.focusedBorderColor, formFieldResources.unfocusedBorderColor)
    )
}

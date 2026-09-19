package de.visualdigits.common.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.color.PaletteColor
import de.visualdigits.common.domain.model.form.LocalFormFieldResources
import de.visualdigits.common.domain.model.form.LocalFormResources
import de.visualdigits.common.presentation.components.button.IndicatorButton
import de.visualdigits.common.presentation.components.util.conditional
import org.jetbrains.compose.resources.painterResource


@Composable
fun ColorPalettePicker(
    modifier: Modifier = Modifier,
    initialColor: Color?,
    paletteColors: List<PaletteColor>,
    onColorChanged: (color: Color?) -> Unit = {}
) {
    val formResources = LocalFormResources.current
    val formFieldResources = LocalFormFieldResources.current

    var currentColor by remember { mutableStateOf(initialColor) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                start = formResources.space,
                end = formResources.space,
                top = formResources.space * 2,
                bottom = formResources.space
            ),
        verticalArrangement = Arrangement.spacedBy(formResources.space)
    ) {
        FlowRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(formResources.space),
            verticalArrangement = Arrangement.spacedBy(formResources.space)
        ) {
            paletteColors.forEach { color ->
                IndicatorButton(
                    modifier = Modifier
                        .clip(MaterialTheme.shapes.extraSmall)
                        .border(1.dp, MaterialTheme.colorScheme.onBackground, MaterialTheme.shapes.extraSmall),
                    textColor = Color.White,
                    padding = 0.dp,
                    buttonColor = color.color ?: Color.White,
                    width = 50.dp,
                    height = 50.dp,
                    shape = RoundedCornerShape(4.dp),
                    content = {
                        if (color.color == null && formFieldResources.colorPaletteNoColorIcon != null) {
                            Icon(
                                modifier = Modifier
                                    .width(100.dp)
                                    .height(100.dp),
                                painter = painterResource(formFieldResources.colorPaletteNoColorIcon),
                                contentDescription = null,
                                tint = Color.Black
                            )
                        }
                    }
                ) {
                    currentColor = color.color
                    onColorChanged(color.color)
                }
            }
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = formResources.space),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.extraSmall)
                    .border(2.dp, MaterialTheme.colorScheme.onBackground)
                    .conditional(currentColor == null) { background(Color.White) }
                    .conditional(currentColor != null) { background(currentColor!!) }
                    .width(50.dp)
                    .height(50.dp)
                    .padding(0.dp),
                contentAlignment = Alignment.Center
            ) {
                if (currentColor == null && formFieldResources.colorPaletteNoColorIcon != null) {
                    Icon(
                        modifier = Modifier
                            .width(300.dp)
                            .height(100.dp),
                        painter = painterResource(formFieldResources.colorPaletteNoColorIcon),
                        contentDescription = null,
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

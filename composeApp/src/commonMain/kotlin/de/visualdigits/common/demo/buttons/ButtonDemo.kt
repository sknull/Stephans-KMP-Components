package de.visualdigits.common.demo.buttons

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.visualdigits.common.presentation.components.button.IndicatorButton

@Composable
fun ButtonDemo() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        IndicatorButton(
            text = "Hello",
            textColor = Color.White,
            buttonColor = Color(0xff333333),
            flatLook = false,
            toolTip = "Hello",
            width = 220.dp,
            height = 80.dp,
            indicatorPosition = Alignment.Center,
            indicatorColor = Color.Blue,
            shape = RoundedCornerShape(4.dp),
            selected = true,
        ) {
        }
    }
}

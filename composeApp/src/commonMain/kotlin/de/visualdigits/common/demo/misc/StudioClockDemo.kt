package de.visualdigits.common.demo.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import de.visualdigits.common.presentation.components.StudioClock
import de.visualdigits.common.presentation.components.StudioClockColors
import de.visualdigits.compose.resources.Res
import de.visualdigits.compose.resources.digital_dream_skew_fat
import org.jetbrains.compose.resources.Font

@Composable
fun StudioClockDemo() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF000000)),
        contentAlignment = Alignment.Center
    ) {
        StudioClock(
            modifier = Modifier
                .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                .padding(10.dp)
                .fillMaxSize(),
            fontFamily = FontFamily(Font(Res.font.digital_dream_skew_fat)),
            colors = StudioClockColors(
                colorHours = Color(0xFFFF0040),
                colorMinutes = Color(0xFFFB7C9C),
                colorSeconds = Color(0xFFFF0040),
                colorTime = Color(0xFFFF0040),
                colorDate = Color(0xFF9E4F62),
                colorBackground = Color(0xdd000000),
            ),
            showSeconds = true,
            showDate = true,
            showYear = true
        )
    }
}

package de.visualdigits.common.demo.misc

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import de.visualdigits.common.domain.model.HsvColor
import de.visualdigits.common.presentation.components.StudioClock
import de.visualdigits.common.presentation.components.StudioClockColors
import org.jetbrains.compose.resources.Font
import stephans_kmp_components.composeapp.generated.resources.Res
import stephans_kmp_components.composeapp.generated.resources.digital_dream_skew_fat

@Composable
fun StudioClockDemo() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        StudioClock(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxSize(),
            fontFamily = FontFamily(Font(Res.font.digital_dream_skew_fat)),
            showSeconds = false,
            showDate = true,
            showYear = true,
            showFrames = false,
            framesPerSecond = 60
        )
    }
}

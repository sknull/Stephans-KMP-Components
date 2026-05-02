package de.visualdigits.common

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import de.visualdigits.common.demo.MainPage
import de.visualdigits.common.domain.model.platform.PlatformType

@Composable
fun App(platformType: PlatformType) {

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF353535))
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        MainPage()
    }
}

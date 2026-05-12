package de.visualdigits.common

import androidx.compose.runtime.Composable
import de.visualdigits.common.demo.MainPage
import de.visualdigits.common.domain.model.platform.PlatformType

@Composable
fun App(platformType: PlatformType) {

    MainPage()
}

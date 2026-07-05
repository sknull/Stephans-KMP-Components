package de.visualdigits.common.presentation.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import de.visualdigits.common.domain.model.ui.UiPlatform
import java.util.Locale

@Composable
actual fun androidPlatform(): UiPlatform = UiPlatform.NONE

@Composable
actual fun Modifier.platformFocus(onClick: (() -> Unit)?): Modifier = this

actual fun applyAppLanguage(languageTag: String) {
    val javaLocale = Locale.forLanguageTag(languageTag)
    Locale.setDefault(javaLocale)
}

actual fun currentLanguageTag(): String {
    return Locale.getDefault().language
}

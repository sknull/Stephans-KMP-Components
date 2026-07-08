package de.visualdigits.common.presentation.util

import androidx.compose.ui.platform.UriHandler
import co.touchlab.kermit.Logger

/**
 * Safely opens a URI without crashing the app if no handler is installed.
 */
fun UriHandler.openUriSafely(uri: String) {
    try {
        this.openUri(uri)
    } catch (_: IllegalArgumentException) {
        // Catches the "Can't open mailto..." error if no email app exists
        Logger.w("Could not open URI. No matching app installed: $uri")
    } catch (e: Exception) {
        Logger.e("Unexpected error while opening URI: $uri", e)
    }
}

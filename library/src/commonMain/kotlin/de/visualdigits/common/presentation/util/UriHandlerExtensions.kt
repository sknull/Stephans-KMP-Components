package de.visualdigits.common.presentation.util

import androidx.compose.ui.platform.UriHandler
import co.touchlab.kermit.Severity
import de.visualdigits.common.domain.model.errorhandling.LogMessage.Companion.log

/**
 * Safely opens a URI without crashing the app if no handler is installed.
 */
fun UriHandler.openUriSafely(uri: String) {
    try {
        this.openUri(uri)
    } catch (_: IllegalArgumentException) {
        // Catches the "Can't open mailto..." error if no email app exists
        log(Severity.Warn, "Could not open URI. No matching app installed: $uri", withTag = "UI")
    } catch (e: Exception) {
        log(Severity.Error, "Unexpected error while opening URI: $uri", e, withTag = "UI")
    }
}

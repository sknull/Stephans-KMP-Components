package de.visualdigits.common.domain.model.platform

import androidx.compose.ui.graphics.Color

enum class ConnectivityMode(
    val isFreeOfCharge: Boolean,
    val color: Color
) {

    wifi(true, Color(0xFF55FF00)),
    cellular(false, Color(0xFFFFBF00)),
    ethernet(true, Color(0xFF55FF00)),
    disconnected(true, Color(0xFFFF3300))
}

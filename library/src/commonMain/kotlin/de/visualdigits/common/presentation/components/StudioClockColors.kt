package de.visualdigits.common.presentation.components

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.takeOrElse
import de.visualdigits.common.domain.util.copy

class StudioClockColors(
    val colorHours: Color = Color(0xFFFF0040),
    val colorMinutes: Color = Color(0xFFFB7C9C),
    val colorSeconds: Color = Color(0xFFFF0040),
    val colorTime: Color = Color(0xFFFF0040),
    val colorDate: Color = Color(0xFF9E4F62),
    val colorBackground: Color = Color(0xdd000000),
) {
    companion object {
        val STUDIO_CLOCK_COLOR_DEFAULT: Color = Color(0xFFFF0040)
    }

    fun copy(
        colorHours: Color = this.colorHours,
        colorMinutes: Color = this.colorMinutes,
        colorSeconds: Color = this.colorSeconds,
        colorTime: Color = this.colorTime,
        colorDate: Color = this.colorDate,
        colorBackground: Color = this.colorBackground,
    ) = StudioClockColors(
        colorHours.takeOrElse { this.colorHours },
        colorMinutes.takeOrElse { this.colorMinutes },
        colorSeconds.takeOrElse { this.colorSeconds },
        colorTime.takeOrElse { this.colorTime },
        colorDate.takeOrElse { this.colorDate },
        colorBackground.takeOrElse { this.colorBackground }
    )
}

val defaultStudioClockColors: StudioClockColors
    get() = studioClockColors(spotColor = StudioClockColors.STUDIO_CLOCK_COLOR_DEFAULT)

fun studioClockColors(spotColor: Color): StudioClockColors = StudioClockColors(
    colorHours = spotColor,
    colorMinutes = spotColor,
    colorSeconds = spotColor,
    colorTime = spotColor,
    colorDate = spotColor.copy(saturation = 0.25f, value = 0.75f),
    colorBackground = Color(0xdd000000),
)

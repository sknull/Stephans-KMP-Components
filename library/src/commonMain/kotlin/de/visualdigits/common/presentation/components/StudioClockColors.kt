package de.visualdigits.common.presentation.components

import androidx.compose.ui.graphics.Color
import de.visualdigits.common.domain.model.HsvColor

class StudioClockColors(
    val colorHours: HsvColor = HsvColor.fromComposeColor(Color(0xFFFF0040)),
    val colorMinutes: HsvColor = HsvColor.fromComposeColor(Color(0xFFFB7C9C)),
    val colorSeconds: HsvColor = HsvColor.fromComposeColor(Color(0xFFFF0040)),
    val colorTime: HsvColor = HsvColor.fromComposeColor(Color(0xFFFF0040)),
    val colorDate: HsvColor = HsvColor.fromComposeColor(Color(0xFF9E4F62)),
    val colorBackground: Color = Color(0xdd000000),
) {
    companion object {
        val STUDIO_CLOCK_COLOR_DEFAULT: HsvColor = HsvColor.fromComposeColor(Color(0xFFFF0040))
    }

    fun copy(
        colorHours: HsvColor = this.colorHours,
        colorMinutes: HsvColor = this.colorMinutes,
        colorSeconds: HsvColor = this.colorSeconds,
        colorTime: HsvColor = this.colorTime,
        colorDate: HsvColor = this.colorDate,
        colorBackground: Color = this.colorBackground,
    ) = StudioClockColors(
        colorHours,
        colorMinutes,
        colorSeconds,
        colorTime,
        colorDate,
        colorBackground
    )
}

val defaultStudioClockColors: StudioClockColors
    get() = studioClockColors(spotColor = StudioClockColors.STUDIO_CLOCK_COLOR_DEFAULT)

fun studioClockColors(spotColor: HsvColor): StudioClockColors {
    return StudioClockColors(
        colorHours = spotColor,
        colorMinutes = spotColor,
        colorSeconds = spotColor,
        colorTime = spotColor,
        colorDate = spotColor,
        colorBackground = Color(0xdd000000),
    )
}

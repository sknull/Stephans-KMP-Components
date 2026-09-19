package de.visualdigits.common.domain.model.color

import androidx.compose.runtime.Immutable

@Immutable
data class ColorPalette(
    val colors: List<PaletteColor>
) {

    val colorLookup = colors.associateBy { it.color }
}

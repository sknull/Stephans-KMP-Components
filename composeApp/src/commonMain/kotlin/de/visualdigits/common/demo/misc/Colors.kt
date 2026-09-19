package de.visualdigits.common.demo.misc

import androidx.compose.ui.graphics.Color
import de.visualdigits.common.domain.model.color.ColorPalette
import de.visualdigits.common.domain.model.color.PaletteColor

val COLOR_PALETTE_BASIC = ColorPalette(
    colors = listOf(
        PaletteColor("red", Color.Red),
        PaletteColor("green", Color.Green),
        PaletteColor("blue", Color.Blue),
        PaletteColor("yellow", Color.Yellow),
        PaletteColor("cyan", Color.Cyan),
        PaletteColor("magenta", Color.Magenta),
        PaletteColor("black", Color.Black),
        PaletteColor("white", Color.White),
        PaletteColor("none", null),
    )
)

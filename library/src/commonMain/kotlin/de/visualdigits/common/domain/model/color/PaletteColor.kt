package de.visualdigits.common.domain.model.color

import androidx.compose.runtime.Immutable
import androidx.compose.ui.graphics.Color

@Immutable
data class PaletteColor(
    val name: String,
    val color: Color?
)

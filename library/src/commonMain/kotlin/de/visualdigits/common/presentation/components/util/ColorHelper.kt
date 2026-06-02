package de.visualdigits.common.presentation.components.util

import androidx.compose.foundation.text.selection.TextSelectionColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.SwitchColors
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@Composable
fun outlinedTextFieldColors(
    focusedBorderColor: Color,
    unfocusedBorderColor: Color
): TextFieldColors = OutlinedTextFieldDefaults.colors(
    unfocusedBorderColor = unfocusedBorderColor,
    unfocusedTextColor = MaterialTheme.colorScheme.onSurface,
    unfocusedLabelColor = MaterialTheme.colorScheme.onSurface,

    focusedBorderColor = focusedBorderColor,
    focusedTextColor = MaterialTheme.colorScheme.onBackground,
    focusedLabelColor = focusedBorderColor,

    cursorColor = MaterialTheme.colorScheme.onSurface,
    selectionColors = TextSelectionColors(
        handleColor = MaterialTheme.colorScheme.onSurface,
        backgroundColor = MaterialTheme.colorScheme.background,
    )
)

@Composable
fun switchBoxColors(): SwitchColors = SwitchDefaults.colors().copy(
    uncheckedTrackColor = MaterialTheme.colorScheme.secondary,
    uncheckedThumbColor = MaterialTheme.colorScheme.onSecondary,
    uncheckedBorderColor = MaterialTheme.colorScheme.onSecondary,

    checkedTrackColor = MaterialTheme.colorScheme.onSurface,
    checkedThumbColor = MaterialTheme.colorScheme.onBackground,
    checkedBorderColor = MaterialTheme.colorScheme.onSurface
)

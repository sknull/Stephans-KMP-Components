package de.visualdigits.common.presentation.components.util

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import de.visualdigits.common.domain.model.HsvColor
import de.visualdigits.common.presentation.components.StudioClockColors
import kotlin.collections.get


private val digitMatrix = listOf(
    //                 0      1      2      3      4      5      6
    booleanArrayOf( true,  true,  true, false,  true,  true,  true), // 0
    booleanArrayOf(false, false,  true, false, false,  true, false), // 1           0
    booleanArrayOf( true, false,  true,  true,  true, false,  true), // 2         1   2
    booleanArrayOf( true, false,  true,  true, false,  true,  true), // 3           3
    booleanArrayOf(false,  true,  true,  true, false,  true, false), // 4         4   5
    booleanArrayOf( true,  true, false,  true, false,  true,  true), // 5           6
    booleanArrayOf(false,  true, false,  true,  true,  true,  true), // 6
    booleanArrayOf( true, false,  true, false, false,  true, false), // 7
    booleanArrayOf( true,  true,  true,  true,  true,  true,  true), // 8
    booleanArrayOf( true,  true,  true,  true, false,  true, false), // 9
)

fun timeHeight(
    width: Double,
    showSeconds: Boolean
): Double {
    val rows = if (showSeconds) 15 * 6 + 7.5 * 2 + 6 else 15 * 4 + 7.5 + 4
    val radius = width / rows
    return 28 * radius
}

fun ContentDrawScope.drawTime(
    offsetX: Double,
    offsetY: Double,
    width: Double,
    colors: StudioClockColors,
    showSeconds: Boolean,
    timeString: String
) {
    val rows = if (showSeconds) 15 * 6 + 7.5 * 2 + 6 else 15 * 4 + 7.5 + 4
    val radius = width / rows
    val digitWidth = 15 * radius

    val left = offsetX - (rows * radius) / 2.0
    val colorHighlighted = colors.colorTime
    val colorDimmed = colors.colorTime.copy(value = 0.1, saturation = 0.5)

    drawLedDigit(
        left,
        offsetY,
        digitMatrix[timeString[0].toString().toInt()],
        radius,
        colorHighlighted,
        colorDimmed
    )
    drawLedDigit(
        left + digitWidth + 2 * radius,
        offsetY,
        digitMatrix[timeString[1].toString().toInt()],
        radius,
        colorHighlighted,
        colorDimmed
    )

    drawDoubleColon(left + 2 * digitWidth + 2 * radius, offsetY, radius, colorHighlighted)

    drawLedDigit(
        left + 2.5 * digitWidth + 2 * radius,
        offsetY,
        digitMatrix[timeString[2].toString().toInt()],
        radius,
        colorHighlighted,
        colorDimmed
    )
    drawLedDigit(
        left + 3.5 * digitWidth + 4 * radius,
        offsetY,
        digitMatrix[timeString[3].toString().toInt()],
        radius,
        colorHighlighted,
        colorDimmed
    )

    if (showSeconds) {
        drawDoubleColon(left + 4.5 * digitWidth + 4 * radius, offsetY, radius, colorHighlighted)

        drawLedDigit(
            left + 5 * digitWidth + 4 * radius,
            offsetY,
            digitMatrix[timeString[4].toString().toInt()],
            radius,
            colorHighlighted,
            colorDimmed
        )
        drawLedDigit(
            left + 6 * digitWidth + 6 * radius,
            offsetY,
            digitMatrix[timeString[5].toString().toInt()],
            radius,
            colorHighlighted,
            colorDimmed
        )
    }
}

private fun frameHeight(
    width: Double
): Double {
    val rows = 15 * 2 + 2
    val radius = width / rows
    return 28 * radius
}

fun ContentDrawScope.drawFrame(
    offsetX: Double,
    offsetY: Double,
    width: Double,
    colors: StudioClockColors,
    frameString: String
) {
    val rows = 15 * 2 + 2
    val radius = width / rows
    val digitWidth = 15 * radius

    val left = offsetX - (rows * radius) / 2.0
    val colorHighlighted = colors.colorTime
    val colorDimmed = colors.colorTime.copy(value = 0.1, saturation = 0.5)

    drawLedDigit(
        left,
        offsetY,
        digitMatrix[frameString[0].toString().toInt()],
        radius,
        colorHighlighted,
        colorDimmed
    )
    drawLedDigit(
        left + digitWidth + 2 * radius,
        offsetY,
        digitMatrix[frameString[1].toString().toInt()],
        radius,
        colorHighlighted,
        colorDimmed
    )
}

fun dateHeight(
    width: Double,
    showYear: Boolean,
): Double {
    val rows = if (showYear) 15 * 8 + 7.5 * 2 + 10 else 15 * 4 + 7.5 * 2 + 4
    val radius = width / rows
    return 28 * radius
}

fun ContentDrawScope.drawDate(
    offsetX: Double,
    offsetY: Double,
    width: Double,
    showYear: Boolean,
    colors: StudioClockColors,
    dateString: String
) {
    val rows = if (showYear) 15 * 8 + 7.5 * 2 + 10 else 15 * 4 + 7.5 * 2 + 4
    val radius = width / rows
    val digitWidth = 15 * radius

    val left = offsetX - (rows * radius) / 2.0
    val colorHighlighted = colors.colorDate
    val colorDimmed = colors.colorDate.copy(value = 0.1, saturation = 0.5)

    drawLedDigit(
        left,
        offsetY,
        digitMatrix[dateString[0].toString().toInt()],
        radius,
        colorHighlighted,
        colorDimmed
    )
    drawLedDigit(
        left + digitWidth + 2 * radius,
        offsetY,
        digitMatrix[dateString[1].toString().toInt()],
        radius,
        colorHighlighted,
        colorDimmed
    )

    drawColon(left + 2 * digitWidth + 2 * radius, offsetY, radius, colorHighlighted)

    drawLedDigit(
        left + 2.5 * digitWidth + 2 * radius,
        offsetY,
        digitMatrix[dateString[2].toString().toInt()],
        radius,
        colorHighlighted,
        colorDimmed
    )
    drawLedDigit(
        left + 3.5 * digitWidth + 4 * radius,
        offsetY,
        digitMatrix[dateString[3].toString().toInt()],
        radius,
        colorHighlighted,
        colorDimmed
    )

    drawColon(left + 4.5 * digitWidth + 4 * radius, offsetY, radius, colorHighlighted)

    if (showYear) {
        drawLedDigit(
            left + 5 * digitWidth + 4 * radius,
            offsetY,
            digitMatrix[dateString[4].toString().toInt()],
            radius,
            colorHighlighted,
            colorDimmed
        )
        drawLedDigit(
            left + 6 * digitWidth + 6 * radius,
            offsetY,
            digitMatrix[dateString[5].toString().toInt()],
            radius,
            colorHighlighted,
            colorDimmed
        )
        drawLedDigit(
            left + 7 * digitWidth + 8 * radius,
            offsetY,
            digitMatrix[dateString[6].toString().toInt()],
            radius,
            colorHighlighted,
            colorDimmed
        )
        drawLedDigit(
            left + 8 * digitWidth + 10 * radius,
            offsetY,
            digitMatrix[dateString[7].toString().toInt()],
            radius,
            colorHighlighted,
            colorDimmed
        )
    }
}

fun ContentDrawScope.drawDoubleColon(
    left: Double,
    top: Double,
    ledRadius: Double,
    colorHighlighted: HsvColor,
) {
    val glowColor = colorHighlighted.toComposeColor().copy(alpha = 0.5f)
    val baseColor = colorHighlighted
    val glossColor = Color.White.copy(alpha = 0.8f)
    drawLed(
        x = (left + 4.0 * ledRadius),
        y = (top + 7.0 * ledRadius),
        radius = ledRadius,
        glowColor = glowColor,
        baseColor = baseColor,
        glossColor = glossColor
    )
    drawLed(
        x = (left + 4.0 * ledRadius),
        y = (top + 20.0 * ledRadius),
        radius = ledRadius,
        glowColor = glowColor,
        baseColor = baseColor,
        glossColor = glossColor
    )
}

fun ContentDrawScope.drawColon(
    left: Double,
    top: Double,
    ledRadius: Double,
    colorHighlighted: HsvColor,
) {
    val glowColor = colorHighlighted.toComposeColor().copy(alpha = 0.5f)
    val baseColor = colorHighlighted
    val glossColor = Color.White.copy(alpha = 0.8f)
    drawLed(
        x = (left + 4.0 * ledRadius),
        y = (top + 27.0 * ledRadius),
        radius = ledRadius,
        glowColor = glowColor,
        baseColor = baseColor,
        glossColor = glossColor
    )
}

fun ContentDrawScope.drawLedDigit(
    left: Double,
    top: Double,
    digit: BooleanArray,
    ledRadius: Double,
    colorHighlighted: HsvColor,
    colorDimmed: HsvColor
) {
    // top [0]
    drawSegmentH(
        x = (left + 3.0 * ledRadius),
        y = top + ledRadius,
        radius = ledRadius,
        glowColor = if (digit[0]) colorHighlighted.toComposeColor().copy(alpha = 0.5f) else Color.Transparent,
        baseColor = if (digit[0]) colorHighlighted else colorDimmed,
        glossColor = Color.White.copy(alpha = if (digit[0]) 0.8f else 0.2f)
    )

    // top left [1]
    drawSegmentV(
        x = (left + ledRadius),
        y = (top + 3.0 * ledRadius),
        radius = ledRadius,
        glowColor = if (digit[1]) colorHighlighted.toComposeColor().copy(alpha = 0.5f) else Color.Transparent,
        baseColor = if (digit[1]) colorHighlighted else colorDimmed,
        glossColor = Color.White.copy(alpha = if (digit[1]) 0.8f else 0.2f)
    )

    // top right [2]
    drawSegmentV(
        x = (left + 14.0 * ledRadius),
        y = (top + 3.0 * ledRadius),
        radius = ledRadius,
        glowColor = if (digit[2]) colorHighlighted.toComposeColor().copy(alpha = 0.5f) else Color.Transparent,
        baseColor = if (digit[2]) colorHighlighted else colorDimmed,
        glossColor = Color.White.copy(alpha = if (digit[2]) 0.8f else 0.2f)
    )

    // mid [3]
    drawSegmentH(
        x = (left + 3.0 * ledRadius),
        y = (top + 14.0 * ledRadius),
        radius = ledRadius,
        glowColor = if (digit[3]) colorHighlighted.toComposeColor().copy(alpha = 0.5f) else Color.Transparent,
        baseColor = if (digit[3]) colorHighlighted else colorDimmed,
        glossColor = Color.White.copy(alpha = if (digit[3]) 0.8f else 0.2f)
    )

    // bottom left [4]
    drawSegmentV(
        x = (left + ledRadius),
        y = (top + 16.0 * ledRadius),
        radius = ledRadius,
        glowColor = if (digit[4]) colorHighlighted.toComposeColor().copy(alpha = 0.5f) else Color.Transparent,
        baseColor = if (digit[4]) colorHighlighted else colorDimmed,
        glossColor = Color.White.copy(alpha = if (digit[4]) 0.8f else 0.2f)
    )

    // bottom right [5]
    drawSegmentV(
        x = (left + 14.0 * ledRadius),
        y = (top + 16.0 * ledRadius),
        radius = ledRadius,
        glowColor = if (digit[5]) colorHighlighted.toComposeColor().copy(alpha = 0.5f) else Color.Transparent,
        baseColor = if (digit[5]) colorHighlighted else colorDimmed,
        glossColor = Color.White.copy(alpha = if (digit[5]) 0.8f else 0.2f)
    )

    // bottom [6]
    drawSegmentH(
        x = (left + 3.0 * ledRadius),
        y = (top + 27 * ledRadius),
        radius = ledRadius,
        glowColor = if (digit[6]) colorHighlighted.toComposeColor().copy(alpha = 0.5f) else Color.Transparent,
        baseColor = if (digit[6]) colorHighlighted else colorDimmed,
        glossColor = Color.White.copy(alpha = if (digit[6]) 0.8f else 0.2f)
    )
}

private fun ContentDrawScope.drawSegmentH(
    x: Double,
    y: Double,
    radius: Double,
    glowColor: Color,
    baseColor: HsvColor,
    glossColor: Color
) {
    (0 until 4 ).forEach { i ->
        drawLed(
            x = x + i * 3 * radius,
            y = y,
            radius = radius,
            glowColor = glowColor,
            baseColor = baseColor,
            glossColor = glossColor
        )
    }
}

private fun ContentDrawScope.drawSegmentV(
    x: Double,
    y: Double,
    radius: Double,
    glowColor: Color,
    baseColor: HsvColor,
    glossColor: Color
) {
    (0 until 4 ).forEach { i ->
        drawLed(
            x = x,
            y = y + i * 3 * radius,
            radius = radius,
            glowColor = glowColor,
            baseColor = baseColor,
            glossColor = glossColor
        )
    }
}

fun ContentDrawScope.drawLed(
    x: Double,
    y: Double,
    radius: Double,
    glowColor: Color,
    baseColor: HsvColor,
    glossColor: Color
) {
    val drawCenter = Offset(x = x.toFloat(), y = y.toFloat())
    // glow
    drawCircle(
        brush = Brush.radialGradient(
            colorStops = arrayOf(
                0.5f to Color.Transparent,
                0.51f to glowColor,
                1.0f to Color.Transparent,
            ),
            center = drawCenter,
            radius = (radius * 1.5).toFloat()
        ),
        radius = (radius * 1.5).toFloat(),
        center = drawCenter,
        blendMode = BlendMode.Screen
    )

    // base color
    drawCircle(
        brush = Brush.radialGradient(
            colorStops = arrayOf(
                0.0f to baseColor.toComposeColor(),
                1.0f to baseColor.copy(value = baseColor.value * 0.2f).toComposeColor(),
            ),
            center = drawCenter,
            radius = radius.toFloat()
        ),
        radius = radius.toFloat(),
        center = drawCenter,
    )

    // gloss
    drawCircle(
        brush = Brush.radialGradient(
            colorStops = arrayOf(
                0.0f to glossColor,
                1.0f to Color.Transparent,
            ),
            center = drawCenter - Offset((radius * 0.25f).toFloat(), (radius * 0.25f).toFloat()),
            radius = (radius * 0.5f).toFloat()
        ),
        radius = (radius * 0.5f).toFloat(),
        center = drawCenter - Offset((radius * 0.25f).toFloat(), (radius * 0.25f).toFloat()),
    )
}

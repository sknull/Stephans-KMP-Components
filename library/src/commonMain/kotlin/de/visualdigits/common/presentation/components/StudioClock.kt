package de.visualdigits.common.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.text.TextMeasurer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import de.visualdigits.common.domain.model.color.HsvColor
import de.visualdigits.common.domain.model.common.KmpOffsetDateTime
import de.visualdigits.common.presentation.components.util.dateHeight
import de.visualdigits.common.presentation.components.util.drawDate
import de.visualdigits.common.presentation.components.util.drawFrame
import de.visualdigits.common.presentation.components.util.drawLed
import de.visualdigits.common.presentation.components.util.drawTime
import de.visualdigits.common.presentation.components.util.timeHeight
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDateTime
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.roundToLong
import kotlin.math.sin
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.Instant

@Composable
fun StudioClock(
    modifier: Modifier = Modifier,
    fontFamily: FontFamily? = null,
    colors: StudioClockColors = defaultStudioClockColors,
    showSeconds: Boolean = true,
    showDate: Boolean = true,
    showYear: Boolean = false,
    showFrames: Boolean = false,
    dimOtherLeds: Boolean = false, // dims all leds except the current one
    framesPerSecond: Int = 24
) {
    val patternWithSeconds = if (fontFamily != null) "HH:mm:ss" else "HHmmss"
    val patternWithoutSeconds = if (fontFamily != null) "HH:mm" else "HHmm"
    val patternDate = if (fontFamily != null) "dd.MM." else "ddMM"
    val patternDateWithYear = if (fontFamily != null) "dd.MM.yyyy" else "ddMMyyyy"

    var currentTime by remember { mutableLongStateOf(System.currentTimeMillis()) }
    var currentFrame by remember { mutableIntStateOf(0) }
    val textMeasurer = rememberTextMeasurer()

    val millisPerFrame = (1000.0f / framesPerSecond).roundToLong()
    val frameDigits = framesPerSecond.toString().length

    Box(
        modifier = modifier
    ) {
        LaunchedEffect(Unit) {
            while (true) {
                currentTime = System.currentTimeMillis()

                val millisUntilNextChange = if (showSeconds) {
                    val progress = System.currentTimeMillis() % 1000L
                    if (showFrames) {
                        currentFrame = (progress / millisPerFrame).toInt()
                        millisPerFrame - System.currentTimeMillis() % millisPerFrame
                    } else {
                        1000L - progress
                    }
                } else {
                    60000L - (System.currentTimeMillis() % 60000L)
                }
                delay(millisUntilNextChange.milliseconds)
            }
        }

        val currentDateTime = KmpOffsetDateTime(
            Instant.fromEpochMilliseconds(currentTime), KmpOffsetDateTime.OFFSET_SYSTEM_DEFAULT
        )

        val currentTimeString = if (showSeconds) {
            currentDateTime.format(patternWithSeconds)
        } else {
            currentDateTime.format(patternWithoutSeconds)
        }
        val currentDateString = if (showYear) {
            currentDateTime.format(patternDateWithYear)
        } else {
            currentDateTime.format(patternDate)
        }
        val currentFrameString = (currentFrame + 1).toString().padStart(frameDigits, '0')

        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithCache {
                    val radius = min(size.width, size.height) / 2.0
                    val unit = radius / 85.0
                    val availableWidth = (radius * 2.0 - 45 * unit)

                    val offsetX = size.width / 2.0
                    val offsetY = size.height / 2.0

                    onDrawWithContent {
                        drawClockFace(
                            offsetX = offsetX,
                            offsetY = offsetY,
                            radius = radius,
                            unit = unit,
                            colors = colors,
                            showSeconds = showSeconds,
                            currentDateTime = currentDateTime.toLocalDateTime(),
                            dimOtherLeds = dimOtherLeds
                        )

                        if (fontFamily != null) {
                            drawTimeLcd(
                                availableWidth = availableWidth,
                                offsetX = offsetX,
                                offsetY = offsetY,
                                unit = unit,
                                fontFamily = fontFamily,
                                colors = colors,
                                textMeasurer = textMeasurer,
                                showSeconds = showSeconds,
                                showDate = showDate,
                                showYear = showYear,
                                showFrames = showFrames,
                                currentTimeString = currentTimeString,
                                currentDateString = currentDateString,
                                currentFrameString = currentFrameString
                            )
                        } else {
                            drawTimeLedMatrix(
                                availableWidth = availableWidth,
                                offsetX = offsetX,
                                offsetY = offsetY,
                                unit = unit,
                                colors = colors,
                                showSeconds = showSeconds,
                                showYear = showYear,
                                showDate = showDate,
                                showFrames = showFrames,
                                currentTimeString = currentTimeString,
                                currentDateString = currentDateString,
                                currentFrameString = currentFrameString
                            )
                        }

                        drawContent()
                    }
                },
            contentAlignment = Alignment.Center
        ) {}
    }
}

private fun ContentDrawScope.drawClockFace(
    offsetX: Double,
    offsetY: Double,
    radius: Double,
    unit: Double,
    colors: StudioClockColors,
    showSeconds: Boolean,
    currentDateTime: LocalDateTime,
    dimOtherLeds: Boolean,
) {
    drawCircle(
        color = colors.colorBackground
    )
    // hours
    drawDots(
        offsetX = offsetX,
        offsetY = offsetY,
        radius = (radius - (if (showSeconds) 18 else 12) * unit),
        ledRadius = (3.0 * unit),
        numberOfDots = 12,
        highlightedDots = currentDateTime.hour % 12,
        colorHighlighted = colors.colorHours,
        colorDimmed = colors.colorHours.copy(value = 0.1, saturation = 0.5),
        dimOtherLeds = dimOtherLeds
    )

    // minutes
    drawDots(
        offsetX = offsetX,
        offsetY = offsetY,
        radius = (radius - (if (showSeconds) 10 else 4) * unit),
        ledRadius = (2.0 * unit),
        numberOfDots = 60,
        highlightedDots = currentDateTime.minute,
        colorHighlighted = colors.colorMinutes,
        colorDimmed = colors.colorMinutes.copy(value = 0.1, saturation = 0.5),
        dimOtherLeds = dimOtherLeds
    )

    // seconds
    if (showSeconds) {
        drawDots(
            offsetX = offsetX,
            offsetY = offsetY,
            radius = (radius - 4 * unit),
            ledRadius = (1.5f * unit),
            numberOfDots = 60,
            highlightedDots = currentDateTime.second,
            colorHighlighted = colors.colorSeconds,
            colorDimmed = colors.colorSeconds.copy(value = 0.1, saturation = 0.5),
            dimOtherLeds = dimOtherLeds
        )
    }
}

private fun ContentDrawScope.drawTimeLcd(
    availableWidth: Double,
    offsetX: Double,
    offsetY: Double,
    unit: Double,
    fontFamily: FontFamily,
    colors: StudioClockColors,
    textMeasurer: TextMeasurer,
    showSeconds: Boolean,
    showDate: Boolean,
    showYear: Boolean,
    showFrames: Boolean,
    currentTimeString: String,
    currentDateString: String,
    currentFrameString: String
) {
    // probe dpi
    val testFontSize = 100f
    val testResult = textMeasurer.measure(
        text = if (showSeconds) "88:88:88" else "88:88", // use widest text possible in this context
        style = TextStyle(
            fontSize = testFontSize.sp,
            fontFamily = fontFamily
        )
    )

    val scaleFactor = availableWidth / testResult.size.width
    val finalFontSize = (testFontSize * scaleFactor * 0.9f).sp

    val timeLayoutResult = textMeasurer.measure(
        text = currentTimeString,
        maxLines = 1,
        style = TextStyle(
            fontFamily = fontFamily,
            fontSize = finalFontSize,
            shadow = Shadow(
                color = colors.colorTime.toComposeColor().copy(alpha = 0.7f),
                offset = Offset(0f, 0f),
                blurRadius = unit.toFloat()
            )
        )
    )

    val dateLayoutResult = textMeasurer.measure(
        text = currentDateString,
        maxLines = 1,
        style = TextStyle(
            fontFamily = fontFamily,
            fontSize = if (showYear) finalFontSize * 0.5 else finalFontSize,
            shadow = Shadow(
                color = colors.colorDate.toComposeColor().copy(alpha = 0.7f),
                offset = Offset(0f, 0f),
                blurRadius = unit.toFloat()
            )
        )
    )

    val frameLayoutResult = textMeasurer.measure(
        text = currentFrameString,
        maxLines = 1,
        style = TextStyle(
            fontFamily = fontFamily,
            fontSize = finalFontSize,
            shadow = Shadow(
                color = colors.colorTime.toComposeColor().copy(alpha = 0.7f),
                offset = Offset(0f, 0f),
                blurRadius = unit.toFloat()
            )
        )
    )

    val heightTime = timeLayoutResult.size.height
    val heightDate = dateLayoutResult.size.height
    val offset = if (showDate) {
        heightTime / 2.0 + heightDate / 2.0
    } else {
        heightTime / 2.0
    }

    drawText(
        textLayoutResult = timeLayoutResult,
        topLeft = Offset(
            x = (offsetX - timeLayoutResult.size.width / 2.0).toFloat(),
            y = (offsetY - offset).toFloat()
        ),
        color = colors.colorTime.copy(value = colors.colorDate.value * 0.8f).toComposeColor()
    )

    if (showDate && showFrames) {
        drawText(
            textLayoutResult = dateLayoutResult,
            topLeft = Offset(
                x = (offsetX - dateLayoutResult.size.width / 2.0).toFloat(),
                y = (offsetY - offset + heightTime + unit).toFloat()
            ),
            color = colors.colorDate.copy(value = colors.colorDate.value * 0.5f).toComposeColor()
        )
        drawText(
            textLayoutResult = frameLayoutResult,
            topLeft = Offset(
                x = (offsetX - frameLayoutResult.size.width / 2.0).toFloat(),
                y = (offsetY - offset + heightTime + heightDate + 2 * unit).toFloat()
            ),
            color = colors.colorTime.copy(value = colors.colorDate.value * 0.8f).toComposeColor()
        )
    } else if (showDate) {
        drawText(
            textLayoutResult = dateLayoutResult,
            topLeft = Offset(
                x = (offsetX - dateLayoutResult.size.width / 2.0).toFloat(),
                y = (offsetY - offset + heightTime + unit).toFloat()
            ),
            color = colors.colorDate.copy(value = colors.colorDate.value * 0.5f).toComposeColor()
        )
    } else if (showFrames) {
        drawText(
            textLayoutResult = frameLayoutResult,
            topLeft = Offset(
                x = (offsetX - frameLayoutResult.size.width / 2.0).toFloat(),
                y = (offsetY - offset + heightTime + unit).toFloat()
            ),
            color = colors.colorTime.copy(value = colors.colorDate.value * 0.8f).toComposeColor()
        )
    }
}

private fun ContentDrawScope.drawTimeLedMatrix(
    availableWidth: Double,
    offsetX: Double,
    offsetY: Double,
    unit: Double,
    colors: StudioClockColors,
    showSeconds: Boolean,
    showYear: Boolean,
    showDate: Boolean,
    showFrames: Boolean,
    currentTimeString: String,
    currentDateString: String,
    currentFrameString: String
) {
    val heightTime = timeHeight(availableWidth * 0.9, showSeconds)
    val heightDate = dateHeight(availableWidth * 0.9, showYear)
    val offset = if (showDate) {
        heightTime / 2.0 + heightDate / 2.0
    } else {
        heightTime / 2.0
    }

    drawTime(offsetX, offsetY - offset, availableWidth * 0.9, colors, showSeconds, currentTimeString)

    if (showDate && showFrames) {
        drawDate(
            offsetX,
            offsetY - offset + heightTime + unit,
            availableWidth * 0.9,
            showYear,
            colors,
            currentDateString
        )
        drawFrame(
            offsetX,
            offsetY - offset + heightTime + heightDate + 2 * unit,
            availableWidth * 0.2,
            colors,
            currentFrameString
        )
    } else if (showDate) {
        drawDate(
            offsetX,
            offsetY - offset + heightTime + unit,
            availableWidth * 0.9,
            showYear,
            colors,
            currentDateString
        )
    } else if (showFrames) {
        drawFrame(offsetX, offsetY - offset + heightTime + unit, availableWidth * 0.2, colors, currentFrameString)
    }
}

private fun ContentDrawScope.drawDots(
    offsetX: Double,
    offsetY: Double,
    radius: Double,
    ledRadius: Double,
    numberOfDots: Int,
    highlightedDots: Int,
    colorHighlighted: HsvColor,
    colorDimmed: HsvColor,
    dimOtherLeds: Boolean
) {
    var a = 0.0f
    val step = 360.0f / numberOfDots
    while (a < 360.0f) {
        val ar = ((a - 90.0) * PI / 180.0).toFloat()
        val x = offsetX + radius * cos(ar)
        val y = offsetY + radius * sin(ar)

        val (baseColor, glowColor, glossColor) = if(dimOtherLeds) {
            val highlightDimOffset = step * (highlightedDots)
            if (a < highlightDimOffset) {
                Triple(colorHighlighted.copy(value = 0.5), colorHighlighted.copy(value = 0.5).toComposeColor().copy(alpha = 0.5f), Color.White.copy(alpha = 0.2f))
            } else if (a == highlightDimOffset) {
                Triple(colorHighlighted, colorHighlighted.toComposeColor().copy(alpha = 0.5f), Color.White.copy(alpha = 0.8f))
            } else {
                Triple(colorDimmed, colorDimmed.toComposeColor().copy(alpha = 0.5f), Color.White.copy(alpha = 0.2f))
            }
        } else {
            val highlightOffset = step * (highlightedDots + 1)
            if (a < highlightOffset) {
                Triple(colorHighlighted, colorHighlighted.toComposeColor().copy(alpha = 0.5f), Color.White.copy(alpha = 0.8f))
            } else {
                Triple(colorDimmed, colorDimmed.toComposeColor().copy(alpha = 0.5f), Color.White.copy(alpha = 0.2f))
            }
        }

        drawLed(x, y, ledRadius, glowColor, baseColor, glossColor)

        a += step
    }
}

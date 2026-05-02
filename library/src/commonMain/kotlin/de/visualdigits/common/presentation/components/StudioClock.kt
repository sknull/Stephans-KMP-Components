package de.visualdigits.common.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.drawscope.ContentDrawScope
import androidx.compose.ui.graphics.drawscope.clipPath
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.sp
import de.visualdigits.common.domain.util.copy
import de.visualdigits.common.domain.util.copyFactor
import kotlinx.coroutines.delay
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

private val formatterWithSeconds = DateTimeFormatter.ofPattern("HH:mm:ss")
private val formatterWithoutSeconds = DateTimeFormatter.ofPattern("HH:mm")
private val formatterDate = DateTimeFormatter.ofPattern("dd.MM.")
private val formatterDateWithYear = DateTimeFormatter.ofPattern("dd.MM.yyyy")

/*
 */
@Composable
fun StudioClock(
    modifier: Modifier = Modifier,
    fontFamily: FontFamily,
    colors: StudioClockColors = defaultStudioClockColors,
    showSeconds: Boolean = true,
    showDate: Boolean = true,
    showYear: Boolean = false
) {

    var currentTime by remember { mutableStateOf(System.currentTimeMillis()) }
    val textMeasurer = rememberTextMeasurer()

    Box(
        modifier = modifier
    ) {
        LaunchedEffect(Unit) {
            while (true) {
                currentTime = System.currentTimeMillis()

                val millisUntilNextChange = if (showSeconds) {
                    1000L - (System.currentTimeMillis() % 1000L)
                } else {
                    60000L - (System.currentTimeMillis() % 60000L)
                }
                delay(millisUntilNextChange)
            }
        }

        val currentDateTime = LocalDateTime.ofInstant(
            Instant.ofEpochMilli(currentTime), ZoneId.systemDefault()
        )
        val currentTimeString = if (showSeconds) formatterWithSeconds.format(currentDateTime) else formatterWithoutSeconds.format(currentDateTime)
        val currentDateString = if (showYear) formatterDateWithYear.format(currentDateTime) else formatterDate.format(currentDateTime)

        // probe dpi
        val testFontSize = 100f
        val testResult = textMeasurer.measure(
            text = if (showSeconds) "88:88:88" else "88:88", // use widest text possible in this context
            style = TextStyle(
                fontSize = testFontSize.sp,
                fontFamily = fontFamily
            )
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawWithCache {
                    val radius = min(size.width, size.height) / 2.0
                    val unit = radius / 85.0
                    val availableWidth = (radius * 2.0 - 45 * unit).toFloat()
                    val scaleFactor = availableWidth / testResult.size.width
                    val finalFontSize = (testFontSize * scaleFactor * 0.9f).sp

                    val offsetX = size.width / 2.0f
                    val offsetY = size.height / 2.0f

                    val timeLayoutResult = textMeasurer.measure(
                        text = currentTimeString,
                        maxLines = 1,
                        style = TextStyle(
                            fontFamily = fontFamily,
                            fontSize = finalFontSize,
                            shadow = Shadow(
                                color = colors.colorTime.copy(alpha = 0.7f),
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
                            fontSize = finalFontSize * 0.5f,
                            shadow = Shadow(
                                color = colors.colorDate.copy(alpha = 0.7f),
                                offset = Offset(0f, 0f),
                                blurRadius = unit.toFloat()
                            )
                        )
                    )

                    onDrawWithContent {
                        drawCircle(
                            color = colors.colorBackground
                        )
                        // hours
                        drawDots(
                            offsetX = offsetX,
                            offsetY = offsetY,
                            angle = (radius - 18 * unit).toFloat(),
                            size = (3.0 * unit).toFloat(),
                            numberOfDots = 12,
                            highlightedDots = currentDateTime.hour % 12,
                            colorHighlighted = colors.colorHours,
                            colorDimmed = colors.colorHours.copy(value = 0.3f)
                        )

                        // minutes
                        drawDots(
                            offsetX = offsetX,
                            offsetY = offsetY,
                            angle = (radius - 10 * unit).toFloat(),
                            size = (2.0 * unit).toFloat(),
                            numberOfDots = 60,
                            highlightedDots = currentDateTime.minute,
                            colorHighlighted = colors.colorMinutes,
                            colorDimmed = colors.colorMinutes.copy(value = 0.3f)
                        )

                        // seconds
                        if (showSeconds) {
                            drawDots(
                                offsetX = offsetX,
                                offsetY = offsetY,
                                angle = (radius - 4 * unit).toFloat(),
                                size = (1.5f * unit).toFloat(),
                                numberOfDots = 60,
                                highlightedDots = currentDateTime.second,
                                colorHighlighted = colors.colorSeconds,
                                colorDimmed = colors.colorSeconds.copy(value = 0.3f)
                            )
                        }

                        drawText(
                            textLayoutResult = timeLayoutResult,
                            topLeft = Offset(
                                x = offsetX - timeLayoutResult.size.width / 2.0f,
                                y = offsetY - timeLayoutResult.size.height / 2.0f - (if(showDate) 3.0f else 0.0f)
                            ),
//                            brush = Brush.linearGradient(
//                                colorStops = arrayOf(
//                                    0.5f to colors.colorTime,
//                                    1.0f to colors.colorTime.copyFactor(valueFactor = 0.2f),
//                                ),
//                                start = Offset(0f, 0f),
//                                end = Offset(0f, timeLayoutResult.size.height.toFloat())
//                            )
                            color = colors.colorTime
                        )

                        if (showDate) {
                            drawText(
                                textLayoutResult = dateLayoutResult,
                                topLeft = Offset(
                                    x = offsetX - dateLayoutResult.size.width / 2.0f,
                                    y = offsetY - dateLayoutResult.size.height / 2.0f + timeLayoutResult.size.height * 0.75f
                                ),
//                                brush = Brush.linearGradient(
//                                    colorStops = arrayOf(
//                                        0.5f to colors.colorDate,
//                                        1.0f to colors.colorDate.copyFactor(valueFactor = 0.2f),
//                                    ),
//                                    start = Offset(0f, 0f),
//                                    end = Offset(0f, dateLayoutResult.size.height.toFloat())
//                                )
                                color = colors.colorDate
                            )
                        }

                        drawContent()
                    }
                },
            contentAlignment = Alignment.Center
        ) {}
    }
}

private fun ContentDrawScope.drawDots(
    offsetX: Float,
    offsetY: Float,
    angle: Float,
    size: Float,
    numberOfDots: Int,
    highlightedDots: Int,
    colorHighlighted: Color,
    colorDimmed: Color
) {
    var a = 0.0f
    val highlightOffset = 360.0f / numberOfDots * (highlightedDots + 1)
    while (a < 360.0f) {
        val ar = ((a - 90.0) * PI / 180.0).toFloat()
        val x = offsetX + angle * cos(ar)
        val y = offsetY + angle * sin(ar)
        val drawCenter = Offset(x = x, y = y)
        val baseColor = if (a < highlightOffset) colorHighlighted else colorDimmed

//        clipPath(
//            Path().apply {
//                addOval(Rect(center = drawCenter, radius = size * 1.5f))
//            }
//        ) {
//            drawCircle(
//                color = Color.Red,
//                radius = size * 1.5f,
//                center = drawCenter + Offset(size, size),
//            )
//        }
        // base color
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.5f to baseColor,
                    1.0f to baseColor.copyFactor(valueFactor = 0.2f),
                ),
                center = drawCenter,
                radius = size
            ),
            radius = size,
            center = drawCenter,
        )

        // gloss
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.0f to Color.White.copy(alpha = 0.5f),
                    1.0f to Color.Transparent,
                ),
                center = drawCenter - Offset(size * 0.25f, size * 0.25f),
                radius = size * 0.5f
            ),
            radius = size * 0.5f,
            center = drawCenter - Offset(size * 0.25f, size * 0.25f),
        )

        // glow
        drawCircle(
            brush = Brush.radialGradient(
                colorStops = arrayOf(
                    0.5f to Color.Transparent,
                    0.75f to baseColor.copy(alpha = 0.3f),
                    1.0f to Color.Transparent,
                ),
                center = drawCenter,
                radius = size * 1.5f
            ),
            radius = size * 1.5f,
            center = drawCenter,
            blendMode = BlendMode.Screen
        )
        a += 360.0f / numberOfDots
    }
}

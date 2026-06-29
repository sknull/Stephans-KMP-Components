package de.visualdigits.common.demo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import androidx.compose.ui.unit.sp
import de.visualdigits.common.demo.buttons.ButtonDemo
import de.visualdigits.common.demo.form.FormDemo
import de.visualdigits.common.demo.misc.StudioClockDemo
import de.visualdigits.common.domain.model.ui.UiText
import de.visualdigits.common.presentation.components.button.IndicatorButton
import de.visualdigits.common.presentation.components.button.TabButtonRow
import de.visualdigits.common.presentation.components.container.ErrorCard
import org.jetbrains.compose.resources.Font
import stephans_kmp_components.composeapp.generated.resources.Res
import stephans_kmp_components.composeapp.generated.resources.Roboto_Black
import stephans_kmp_components.composeapp.generated.resources.Roboto_Bold
import stephans_kmp_components.composeapp.generated.resources.Roboto_Regular

@Composable
fun MainPage(
) {

    var state by remember { mutableStateOf(DemoState()) }

    val items = linkedMapOf<Pair<String, UiText>, @Composable (() -> Unit)>(
        Pair("Studio Clock", UiText.DynamicString("Studio Clock")) to {
            StudioClockDemo()
        },
        Pair("Button Demo", UiText.DynamicString("Button Demo")) to {
            ButtonDemo()
        },
        Pair("Form Demo", UiText.DynamicString("Form Demo")) to {
            FormDemo(
                state = state,
                setState = { newState ->
                    state = newState
                }
            )
        },
    )

    var selectedTabIndex by remember { mutableStateOf(0) }
    val fontFamilyRegular = FontFamily(Font(Res.font.Roboto_Regular))
    val fontFamilyBold = FontFamily(Font(Res.font.Roboto_Bold))
    val fontFamilyBlack = FontFamily(Font(Res.font.Roboto_Black))
    val textColor = Color(0xffffffff)
    val sizeFactor = 1.0f

    MaterialTheme(
        typography = Typography(
            headlineSmall = TextStyle(
                fontFamily = fontFamilyBlack,
                fontWeight = FontWeight.Black,
                fontSize = 18.sp * sizeFactor,
                lineHeight = 1.5.em,
                letterSpacing = 0.2.sp,
                color = textColor
            ),
            headlineMedium = TextStyle(
                fontFamily = fontFamilyBlack,
                fontWeight = FontWeight.Black,
                fontSize = 24.sp * sizeFactor,
                lineHeight = 1.5.em,
                letterSpacing = 0.2.sp,
                color = textColor
            ),
            headlineLarge = TextStyle(
                fontFamily = fontFamilyBlack,
                fontWeight = FontWeight.Black,
                fontSize = 30.sp * sizeFactor,
                lineHeight = 1.5.em,
                letterSpacing = 0.2.sp,
                color = textColor
            ),

            titleSmall = TextStyle(
                fontFamily = fontFamilyBold,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp * sizeFactor,
                lineHeight = 1.2.em,
                letterSpacing = 0.2.sp,
                color = textColor
            ),
            titleMedium = TextStyle(
                fontFamily = fontFamilyBold,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp * sizeFactor,
                lineHeight = 1.2.em,
                letterSpacing = 0.2.sp,
                color = textColor
            ),
            titleLarge = TextStyle(
                fontFamily = fontFamilyBold,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp * sizeFactor,
                lineHeight = 1.2.em,
                letterSpacing = 0.2.sp,
                color = textColor
            ),

            bodySmall = TextStyle(
                fontFamily = fontFamilyRegular,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp * sizeFactor,
                lineHeight = 1.2.em,
                letterSpacing = 0.2.sp,
                color = textColor
            ),
            bodyMedium = TextStyle(
                fontFamily = fontFamilyRegular,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp * sizeFactor,
                lineHeight = 1.2.em,
                letterSpacing = 0.2.sp,
                color = textColor
            ),
            bodyLarge = TextStyle(
                fontFamily = fontFamilyRegular,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp * sizeFactor,
                lineHeight = 1.2.em,
                letterSpacing = 0.2.sp,
                color = textColor
            ),

            displaySmall = TextStyle(
                fontFamily = fontFamilyRegular,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp * sizeFactor,
                lineHeight = 1.2.em,
                letterSpacing = 0.2.sp,
                color = textColor
            ),
            displayMedium = TextStyle(
                fontFamily = fontFamilyRegular,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp * sizeFactor,
                lineHeight = 1.2.em,
                letterSpacing = 0.2.sp,
                color = textColor
            ),
            displayLarge = TextStyle(
                fontFamily = fontFamilyRegular,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp * sizeFactor,
                lineHeight = 1.2.em,
                letterSpacing = 0.2.sp,
                color = textColor
            ),

            labelSmall = TextStyle(
                fontFamily = fontFamilyBold,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp * sizeFactor,
                lineHeight = 1.2.em,
                letterSpacing = 0.2.sp,
                color = textColor
            ),
            labelMedium = TextStyle(
                fontFamily = fontFamilyBold,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp * sizeFactor,
                lineHeight = 1.2.em,
                letterSpacing = 0.2.sp,
                color = textColor
            ),
            labelLarge = TextStyle(
                fontFamily = fontFamilyBold,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp * sizeFactor,
                lineHeight = 1.2.em,
                letterSpacing = 0.2.sp,
                color = textColor
            )
        ),
        colorScheme = darkColorScheme(
            secondary = Color(0xFF313030),
            onSecondary = Color(0xFFFFFFFF),

            background = Color(0xFF000000),
            onBackground = Color(0xFFFFFFFF),

            surface = Color.Transparent,
            onSurface = Color(0xFF439DDE), // deco color

            surfaceContainer = Color(0xFFFFFFFF),
            surfaceContainerHigh = Color.Transparent,
            surfaceContainerLow = Color.Transparent,
            surfaceContainerLowest = Color(0xFF373737),

            errorContainer = Color(0xffff002a),
            onErrorContainer = Color(0xFFFFFFFF),

            outline = Color(0xFFFFFFFF),

            primaryFixed = Color(0xAA000000),
        ),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background)
        ) {
            ErrorCard(
                errorMessage = state.uiMessage,
                severity = state.uiMessageSeverity,
                shapeContainer = MaterialTheme.shapes.small
            )

            TabButtonRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        val strokeWidth = 2.dp.toPx()
                        drawLine(
                            color = Color(0xaa111111),
                            start = Offset(0f, size.height - strokeWidth / 2),
                            end = Offset(size.width, size.height - strokeWidth / 2),
                            strokeWidth = strokeWidth
                        )
                    },
                horizontalArrangement = Arrangement.spacedBy(0.dp),
                selectedTab = { selectedTabIndex },
                items = items
            ) { label, index ->
                IndicatorButton(
                    flatLook = false,
                    buttonColor = Color.Black,
                    textColor = Color.White,
                    text = label.asString(),
                    indicatorPosition = Alignment.BottomCenter,
                    indicatorColor = Color(0xFFFF1B55),
                    shape = RoundedCornerShape(
                        topStart = 6.dp,
                        topEnd = 6.dp,
                        bottomStart = 0.dp,
                        bottomEnd = 0.dp
                    ),
                    selected = selectedTabIndex == index,
                ) {
                    selectedTabIndex = index
                    state = state.copy(
                        editedConfiguration = if (index == 2) state.configuration else null,
                        uiMessage = null,
                        uiMessageSeverity = null
                    )
                }
            }
        }
    }
}

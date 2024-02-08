package ru.aidar.lingolearn.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    background = LlColorBlack,
    onBackground = LlColorBlackAlpha,
    primary = LlColorWhite,
    secondary = LlColorLight,
    tertiary = LlColorDark,
)

private val LightColorScheme = lightColorScheme(
    background = LlColorWhite,
    onBackground = LlColorWhiteAlpha,
    primary = LlColorBlack,
    secondary = LlColorDark,
    tertiary = LlColorLight,
)

//@Immutable
//data class GradientColors(
//    val top: Color = Color.Unspecified,
//    val bottom: Color = Color.Unspecified,
//    val container: Color = Color.Unspecified,
//)

//val LocalGradientColors = staticCompositionLocalOf { GradientColors() }
//val LightGradientColors = GradientColors(container = ColorWhite)
//val DarkGradientColors = GradientColors(container = ColorBlack)

@Immutable
data class LlColors(
    val background: Color = Color.Unspecified,
    val primary: Color = Color.Unspecified,
    val onBackground: Color = Color.Unspecified,
    val additionalColor: Color = Color.Unspecified,
    val inverseColor: Color = Color.Unspecified,
)

val LocalLLColors = staticCompositionLocalOf { LlColors() }

val LightLLColors = LlColors(
    background = LlColorWhite,
    primary = LlColorBlack,
    onBackground = LlColorWhiteAlpha,
    additionalColor = LlColorDark,
    inverseColor = LlColorLight
)
val DarkLLColors = LlColors(
    background = LlColorBlack ,
    primary = LlColorWhite,
    onBackground = LlColorBlackAlpha,
    additionalColor = LlColorLight,
    inverseColor = LlColorDark
)

@Composable
fun LingoLearnTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if(darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }


    val llColors = when(darkTheme) {
        true ->
            DarkLLColors

        else ->
            LightLLColors

    }

    /*    val view = LocalView.current
    if(!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }*/

    CompositionLocalProvider(
        LocalLLColors provides llColors
    ) {
        MaterialTheme(
            colorScheme = colorScheme,
            typography = Typography,
            content = content
        )
    }
}
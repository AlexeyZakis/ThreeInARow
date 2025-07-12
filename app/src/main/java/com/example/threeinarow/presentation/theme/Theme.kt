package com.example.threeinarow.presentation.theme

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle

abstract class Theme {
    /// Colors
    // Light
    abstract val lightLabelPrimary: Color
    abstract val lightLabelSecondary: Color
    abstract val lightLabelTertiary: Color
    abstract val lightLabelDisable: Color

    abstract val lightBackPrimary: Color
    abstract val lightBackSecondary: Color
    abstract val lightBackTertiary: Color
    abstract val lightBackDisable: Color

    abstract val lightRed: Color
    abstract val lightGreen: Color
    abstract val lightBlue: Color
    abstract val lightGray: Color
    abstract val lightGrayLight: Color
    abstract val lightWhite: Color
    abstract val lightYellow: Color
    abstract val lightViolet: Color

    // Dark
    abstract val darkLabelPrimary: Color
    abstract val darkLabelSecondary: Color
    abstract val darkLabelTertiary: Color
    abstract val darkLabelDisable: Color

    abstract val darkBackPrimary: Color
    abstract val darkBackSecondary: Color
    abstract val darkBackTertiary: Color
    abstract val darkBackDisable: Color

    abstract val darkRed: Color
    abstract val darkGreen: Color
    abstract val darkBlue: Color
    abstract val darkGray: Color
    abstract val darkGrayLight: Color
    abstract val darkWhite: Color
    abstract val darkYellow: Color
    abstract val darkViolet: Color

    /// Typography
    abstract val labelPrimary: TextStyle
    abstract val labelSecondary: TextStyle
    abstract val labelTertiary: TextStyle
    abstract val labelDebug: TextStyle
}

data class ColorNames(
    val labelPrimary: Color,
    val labelSecondary: Color,
    val labelTertiary: Color,
    val labelDisable: Color,

    val backPrimary: Color,
    val backSecondary: Color,
    val backTertiary: Color,
    val backDisable: Color,

    val red: Color,
    val green: Color,
    val blue: Color,
    val gray: Color,
    val greyLight: Color,
    val white: Color,
    val yellow: Color,
    val violet: Color
)

val LocalColorNames = staticCompositionLocalOf {
    ColorNames(
        labelPrimary = Color.Unspecified,
        labelSecondary = Color.Unspecified,
        labelTertiary = Color.Unspecified,
        labelDisable = Color.Unspecified,

        backPrimary = Color.Unspecified,
        backSecondary = Color.Unspecified,
        backTertiary = Color.Unspecified,
        backDisable = Color.Unspecified,

        red = Color.Unspecified,
        green = Color.Unspecified,
        blue = Color.Unspecified,
        gray = Color.Unspecified,
        greyLight = Color.Unspecified,
        white = Color.Unspecified,
        yellow = Color.Unspecified,
        violet = Color.Unspecified,
    )
}

data class TypographyNames(
    val labelPrimary: TextStyle,
    val labelSecondary: TextStyle,
    val labelTertiary: TextStyle,
    val labelDebug: TextStyle,
)

val LocalTypographyNames = staticCompositionLocalOf {
    TypographyNames(
        labelPrimary = TextStyle.Default,
        labelSecondary = TextStyle.Default,
        labelTertiary = TextStyle.Default,
        labelDebug = TextStyle.Default,
    )
}

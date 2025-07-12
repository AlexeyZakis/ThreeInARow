package com.example.threeinarow.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import com.example.threeinarow.presentation.theme.themes.MainTheme

@Composable
fun AppTheme(
    theme: Theme = MainTheme,
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val themeColors = if (darkTheme) {
        ColorNames(
            labelPrimary = theme.darkLabelPrimary,
            labelSecondary = theme.darkLabelSecondary,
            labelTertiary = theme.darkLabelTertiary,
            labelDisable = theme.darkLabelDisable,

            backPrimary = theme.darkBackPrimary,
            backSecondary = theme.darkBackSecondary,
            backTertiary = theme.darkBackTertiary,
            backDisable = theme.darkBackDisable,

            red = theme.darkRed,
            green = theme.darkGreen,
            blue = theme.darkBlue,
            gray = theme.darkGray,
            greyLight = theme.darkGrayLight,
            white = theme.darkWhite,
            yellow = theme.darkYellow,
            violet = theme.darkViolet,
        )
    } else {
        ColorNames(
            labelPrimary = theme.lightLabelPrimary,
            labelSecondary = theme.lightLabelSecondary,
            labelTertiary = theme.lightLabelTertiary,
            labelDisable = theme.lightLabelDisable,

            backPrimary = theme.lightBackPrimary,
            backSecondary = theme.lightBackSecondary,
            backTertiary = theme.lightBackTertiary,
            backDisable = theme.lightBackDisable,

            red = theme.lightRed,
            green = theme.lightGreen,
            blue = theme.lightBlue,
            gray = theme.lightGray,
            greyLight = theme.lightGrayLight,
            white = theme.lightWhite,
            yellow = theme.lightYellow,
            violet = theme.lightViolet,
        )
    }
    val typography = TypographyNames(
        labelPrimary = theme.labelPrimary.copy(
            color = themeColors.labelPrimary,
        ),
        labelSecondary = theme.labelSecondary.copy(
            color = themeColors.labelSecondary,
        ),
        labelTertiary = theme.labelTertiary.copy(
            color = themeColors.labelTertiary,
        ),
        labelDebug = theme.labelDebug.copy(
            color = themeColors.labelPrimary,
        ),
    )
    CompositionLocalProvider(
        LocalTypographyNames provides typography,
        LocalColorNames provides themeColors,
    ) {
        MaterialTheme(
            content = content
        )
    }
}

val themeTypography: TypographyNames
    @Composable
    get() = LocalTypographyNames.current

val themeColors: ColorNames
    @Composable
    get() = LocalColorNames.current

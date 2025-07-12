package com.example.threeinarow.presentation.theme.themes

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.threeinarow.presentation.theme.Theme

object MainTheme : Theme() {
    /// Colors
    // Light
    override val lightLabelPrimary = Color(0xFF000000)
    override val lightLabelSecondary = Color(0x99000000)
    override val lightLabelTertiary = Color(0x4D000000)
    override val lightLabelDisable = Color(0x26000000)

    override val lightBackPrimary = Color(0xFFF7F6F2)
    override val lightBackSecondary = Color(0xFEAAAAA5)
    override val lightBackTertiary = Color(0xFE81817A)
    override val lightBackDisable = Color(0x66B2B0B0)

    override val lightRed = Color(0xFFFF3B30)
    override val lightGreen = Color(0xFF34C759)
    override val lightBlue = Color(0xFF007AFF)
    override val lightGray = Color(0xFF8E8E93)
    override val lightGrayLight = Color(0xFFD1D1D6)
    override val lightWhite = Color(0xFFFFFFFF)
    override val lightYellow = Color(0xFFF3C95C)
    override val lightViolet = Color(0xFFBF30D9)

    // Dark
    override val darkLabelPrimary = Color(0xFFFFFFFF)
    override val darkLabelSecondary = Color(0x99FFFFFF)
    override val darkLabelTertiary = Color(0x66FFFFFF)
    override val darkLabelDisable = Color(0x26FFFFFF)

    override val darkBackPrimary = Color(0xFF161618)
    override val darkBackSecondary = Color(0xFF252528)
    override val darkBackTertiary = Color(0x66FFFFFF)
    override val darkBackDisable = Color(0x66050505)

    override val darkRed = Color(0xFFFF453A)
    override val darkGreen = Color(0xFF28A63C)
    override val darkBlue = Color(0xFF0A84FF)
    override val darkGray = Color(0xFF8E8E93)
    override val darkGrayLight = Color(0xFF48484A)
    override val darkWhite = Color(0xFFFFFFFF)
    override val darkYellow = Color(0xFFD7B45B)
    override val darkViolet = Color(0xFF9928AD)


    /// Typography
    override val labelPrimary = TextStyle.Default.copy(
        fontSize = 14.sp,
    )
    override val labelSecondary = TextStyle.Default.copy(
        fontSize = 10.sp
    )
    override val labelTertiary = TextStyle.Default.copy(
        fontSize = 6.sp
    )
    override val labelDebug = TextStyle.Default.copy(
        fontSize = 20.sp
    )
}

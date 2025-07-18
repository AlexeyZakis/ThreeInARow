package com.example.threeinarow.presentation.screens.gameScreen.components.board

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import com.example.threeinarow.R
import com.example.threeinarow.data.Config
import com.example.threeinarow.domain.models.ExplosionPatterns
import com.example.threeinarow.presentation.screens.gameScreen.blockSizeDp
import com.example.threeinarow.presentation.theme.themeColors

@Composable
fun ExplosionPatternIcon(pattern: ExplosionPatterns) {
    val icon = when (pattern) {
        ExplosionPatterns.Bomb3x3 -> R.drawable.explosion_3x3
        ExplosionPatterns.Bomb5x5 -> R.drawable.explosion_5x5
        ExplosionPatterns.VerticalLine -> R.drawable.explosion_vertical
        ExplosionPatterns.HorizontalLine -> R.drawable.explosion_horizontal
        ExplosionPatterns.Cross -> R.drawable.explosion_cross
        ExplosionPatterns.DiagonalLeft -> R.drawable.explosion_diagonal_left
        ExplosionPatterns.DiagonalRight -> R.drawable.explosion_diagonal_right
        ExplosionPatterns.X -> R.drawable.explosion_x
        ExplosionPatterns.CrossX -> R.drawable.explosion_cross_x
        ExplosionPatterns.All -> R.drawable.explosion_all
    }

    Image(
        painter = painterResource(id = icon),
        contentDescription = null,
        colorFilter = ColorFilter.tint(themeColors.greyLight),
        modifier = Modifier.size(blockSizeDp * Config.BLOCK_ICON_RELATIVE_SIZE)
    )
}

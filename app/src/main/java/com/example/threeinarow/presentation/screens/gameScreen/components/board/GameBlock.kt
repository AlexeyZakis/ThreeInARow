package com.example.threeinarow.presentation.screens.gameScreen.components.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import com.example.threeinarow.data.Config
import com.example.threeinarow.data.fieldElements.Block
import com.example.threeinarow.domain.models.BlockTypes
import com.example.threeinarow.domain.models.ExplosionPatterns
import com.example.threeinarow.presentation.screens.gameScreen.blockSizeDp
import com.example.threeinarow.presentation.theme.AppTheme
import com.example.threeinarow.presentation.theme.themeColors

@Composable
fun GameBlock(
    block: Block,
    isActive: Boolean,
    onClick: () -> Unit,
    clickable: Boolean,
    explosionPattern: ExplosionPatterns?,
    modifier: Modifier = Modifier,
) {
    val color = block.type.toColor()
    val borderColor = if (isActive) {
        themeColors.gray
    } else {
        color.setBrightness(Config.BLOCK_BORDER_BRIGHTNESS)
    }
    val shape = CutCornerShape(blockSizeDp * Config.BLOCK_CORNER_CUT_RELATIVE_WIDTH)

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(blockSizeDp)
            .border(blockSizeDp * Config.BLOCK_BORDER_RELATIVE_WIDTH, borderColor, shape)
            .background(color, shape)
            .clickable(clickable) { onClick() }
    ) {
        if (explosionPattern != null) {
            ExplosionPatternIcon(pattern = explosionPattern)
        }
    }
}


@Composable
fun Color.setBrightness(percent: Float): Color {
    val red = (red * percent).coerceIn(0f, 1f)
    val green = (green * percent).coerceIn(0f, 1f)
    val blue = (blue * percent).coerceIn(0f, 1f)
    val newColor = this.copy(
        red = red,
        green = green,
        blue = blue,
    )
    return newColor
}

@Composable
private fun BlockTypes.toColor(): Color {
    return when (this) {
        BlockTypes.RED -> themeColors.red
        BlockTypes.GREEN -> themeColors.green
        BlockTypes.BLUE -> themeColors.blue
        BlockTypes.YELLOW -> themeColors.yellow
        BlockTypes.VIOLET -> themeColors.violet
    }
}

@Preview
@Composable
private fun GameBlockPreview() {
    AppTheme {
        val block = Block.exampleBlock
        GameBlock(
            block = block,
            isActive = false,
            explosionPattern = null,
            clickable = true,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun GameBlockIsActivePreview() {
    AppTheme {
        val block = Block.exampleBlock
        GameBlock(
            block = block,
            isActive = true,
            explosionPattern = null,
            clickable = true,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun GameBlockBomb3x3Preview() {
    AppTheme {
        val block = Block.exampleBlock
        GameBlock(
            block = block,
            isActive = false,
            onClick = {},
            clickable = true,
            explosionPattern = ExplosionPatterns.Bomb3x3,
        )
    }
}

@Preview
@Composable
private fun GameBlockBomb5x5Preview() {
    AppTheme {
        val block = Block.exampleBlock
        GameBlock(
            block = block,
            isActive = false,
            onClick = {},
            clickable = true,
            explosionPattern = ExplosionPatterns.Bomb5x5,
        )
    }
}

@Preview
@Composable
private fun GameBlockBombHorizontalPreview() {
    AppTheme {
        val block = Block.exampleBlock
        GameBlock(
            block = block,
            isActive = false,
            onClick = {},
            clickable = true,
            explosionPattern = ExplosionPatterns.HorizontalLine,
        )
    }
}

@Preview
@Composable
private fun GameBlockBombVerticalPreview() {
    AppTheme {
        val block = Block.exampleBlock
        GameBlock(
            block = block,
            isActive = false,
            onClick = {},
            clickable = true,
            explosionPattern = ExplosionPatterns.VerticalLine,
        )
    }
}

@Preview
@Composable
private fun GameBlockBombCrossPreview() {
    AppTheme {
        val block = Block.exampleBlock
        GameBlock(
            block = block,
            isActive = false,
            onClick = {},
            clickable = true,
            explosionPattern = ExplosionPatterns.Cross,
        )
    }
}

@Preview
@Composable
private fun GameBlockBombDiagonalLeftPreview() {
    AppTheme {
        val block = Block.exampleBlock
        GameBlock(
            block = block,
            isActive = false,
            onClick = {},
            clickable = true,
            explosionPattern = ExplosionPatterns.DiagonalLeft,
        )
    }
}

@Preview
@Composable
private fun GameBlockBombDiagonalRightPreview() {
    AppTheme {
        val block = Block.exampleBlock
        GameBlock(
            block = block,
            isActive = false,
            onClick = {},
            clickable = true,
            explosionPattern = ExplosionPatterns.DiagonalRight,
        )
    }
}

@Preview
@Composable
private fun GameBlockBombXPreview() {
    AppTheme {
        val block = Block.exampleBlock
        GameBlock(
            block = block,
            isActive = false,
            onClick = {},
            clickable = true,
            explosionPattern = ExplosionPatterns.X,
        )
    }
}

@Preview
@Composable
private fun GameBlockBombCrossXPreview() {
    AppTheme {
        val block = Block.exampleBlock
        GameBlock(
            block = block,
            isActive = false,
            onClick = {},
            clickable = true,
            explosionPattern = ExplosionPatterns.CrossX,
        )
    }
}

@Preview
@Composable
private fun GameBlockBombAllPreview() {
    AppTheme {
        val block = Block.exampleBlock
        GameBlock(
            block = block,
            isActive = false,
            onClick = {},
            clickable = true,
            explosionPattern = ExplosionPatterns.All,
        )
    }
}

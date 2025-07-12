package com.example.threeinarow.presentation.screens.gameScreen.components.board

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.threeinarow.data.fieldElements.Block
import com.example.threeinarow.domain.BlockTypes
import com.example.threeinarow.presentation.theme.AppTheme
import com.example.threeinarow.presentation.theme.themeColors

@Composable
fun GameBlock(
    block: Block,
    isActive: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val color = block.type.toColor()
    val borderColor = if (isActive) {
        themeColors.gray
    } else {
        color.setBrightness(0.7f)
    }
    val shape = CutCornerShape(5.dp)
    Box(
        modifier = modifier
            .size(100.dp)
            .border(
                width = 5.dp,
                color = borderColor,
                shape = shape,
            )
            .background(
                color = color,
                shape = shape,
            )
            .clickable { onClick() }
    )
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
        val block = Block()
        GameBlock(
            block = block,
            isActive = false,
            onClick = {},
        )
    }
}

@Preview
@Composable
private fun GameBlockIsActivePreview() {
    AppTheme {
        val block = Block()
        GameBlock(
            block = block,
            isActive = true,
            onClick = {},
        )
    }
}

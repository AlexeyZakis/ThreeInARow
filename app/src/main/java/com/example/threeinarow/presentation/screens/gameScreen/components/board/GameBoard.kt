package com.example.threeinarow.presentation.screens.gameScreen.components.board

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.threeinarow.data.Config
import com.example.threeinarow.data.GameBoard
import com.example.threeinarow.data.GameBoardManagerImpl
import com.example.threeinarow.data.RandomManagerImpl
import com.example.threeinarow.data.fieldElements.Block
import com.example.threeinarow.data.fieldElements.Bomb
import com.example.threeinarow.domain.models.Coord
import com.example.threeinarow.presentation.screens.gameScreen.blockSizeDp
import com.example.threeinarow.presentation.theme.AppTheme

@Composable
fun GameBoard(
    gameBoard: GameBoard,
    onObjectClick: (Coord) -> Unit,
    modifier: Modifier = Modifier,
    activeObjectPosition: Coord? = null,
) {
    val height = gameBoard.height
    val width = gameBoard.width
    val spaceBetweenObjects = blockSizeDp * Config.SPACE_BETWEEN_BLOCK_RELATIVE

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spaceBetweenObjects)
    ) {
        repeat(height) { y ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(spaceBetweenObjects)
            ) {
                repeat(width) { x ->
                    val coord = Coord(x, y)
                    val obj = gameBoard[coord]
                    val isActive =
                        x == activeObjectPosition?.x && y == activeObjectPosition.y
                    when (obj) {
                        is Block -> {
                            val explosionPattern = if (obj is Bomb) {
                                obj.explosionPattern
                            } else {
                                null
                            }
                            GameBlock(
                                block = obj,
                                isActive = isActive,
                                explosionPattern = explosionPattern,
                                onClick = { onObjectClick(Coord(x, y)) }
                            )
                        }

                        else -> {
                            EmptyBlock()
                        }
                    }
                }
            }
        }
    }
}


@Preview(
    widthDp = 1000,
    heightDp = 800,
)
@Composable
@SuppressLint("StateFlowValueCalledInComposition")
private fun GameBoardPreview() {
    AppTheme {
        val gameBoardManager = GameBoardManagerImpl(
            width = 10,
            height = 8,
            randomManager = RandomManagerImpl(1428),
        )
        GameBoard(
            gameBoard = gameBoardManager.gameBoard.value,
            onObjectClick = {},
        )
    }
}

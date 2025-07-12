package com.example.threeinarow.presentation.screens.gameScreen.components.board

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.threeinarow.data.GameBoardManagerImpl
import com.example.threeinarow.data.RandomManagerImpl
import com.example.threeinarow.data.fieldElements.Block
import com.example.threeinarow.domain.gameObjects.GameBoardObject
import com.example.threeinarow.presentation.theme.AppTheme

@Composable
fun GameBoard(
    gameBoard: List<List<GameBoardObject>>,
    onObjectClick: (Int, Int) -> Unit,
    modifier: Modifier = Modifier,
    activeObjectPosition: Pair<Int, Int>? = null,
) {
    val height = gameBoard.size
    val width = gameBoard.firstOrNull()?.size ?: return
    val spaceBetweenObjects = 5.dp

    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(spaceBetweenObjects)
    ) {
        repeat(height) { y ->
            Row(
                horizontalArrangement = Arrangement.spacedBy(spaceBetweenObjects)
            ) {
                repeat(width) { x ->
                    val obj = gameBoard[y][x]
                    val isActive = x == activeObjectPosition?.first && y == activeObjectPosition.second
                    when (obj) {
                        is Block -> {
                            GameBlock(
                                block = obj,
                                isActive = isActive,
                                onClick = { onObjectClick(x, y) }
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
        GameBoard(gameBoardManager.gameBoard.value, onObjectClick = { x, y -> {}})
    }
}

package com.example.threeinarow.presentation.screens.gameScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.threeinarow.data.GameBoardManagerImpl
import com.example.threeinarow.data.RandomManagerImpl
import com.example.threeinarow.presentation.screens.gameScreen.components.board.GameBoard
import com.example.threeinarow.presentation.theme.AppTheme
import com.example.threeinarow.presentation.theme.themeColors

@Composable
fun GameScreen(
    screenState: GameScreenState,
    screenAction: (GameScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .fillMaxSize()
            .background(themeColors.backPrimary)
    ) {
        GameBoard(
            gameBoard = screenState.gameBoard,
            activeObjectPosition = screenState.activeObjectPosition,
            onObjectClick = { x, y -> screenAction(GameScreenAction.OnBoardObjectClick(x, y)) },
            modifier = Modifier.padding(24.dp)
        )
    }
}

@SuppressLint("StateFlowValueCalledInComposition")
@Preview(
    widthDp = 1050,
    heightDp = 1650,
)
@Composable
private fun GameScreenPreview() {
    AppTheme {
        val gameBoardManager = GameBoardManagerImpl(
            width = 10,
            height = 8,
            randomManager = RandomManagerImpl(1428),
        )
        GameBoard(gameBoardManager.gameBoard.value, onObjectClick = { x, y -> {}})
    }
}

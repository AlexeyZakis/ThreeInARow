package com.example.threeinarow.presentation.screens.gameScreen

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.coerceAtLeast
import androidx.compose.ui.unit.dp
import com.example.threeinarow.data.Config
import com.example.threeinarow.data.GameBoardManagerImpl
import com.example.threeinarow.data.IdManagerImpl
import com.example.threeinarow.data.RandomManagerImpl
import com.example.threeinarow.presentation.screens.gameScreen.components.board.GameBoard
import com.example.threeinarow.presentation.theme.AppTheme
import com.example.threeinarow.presentation.theme.themeColors
import kotlin.math.min

@Composable
fun GameScreen(
    screenState: GameScreenState,
    screenAction: (GameScreenAction) -> Unit,
    modifier: Modifier = Modifier,
) {
    val screenPadding = 24

    var size by remember { mutableStateOf(IntSize.Zero) }

    val blockSizeDp = calculateBlockSize(size, screenPadding)

    CompositionLocalProvider(
        LocalBlockSize provides blockSizeDp,
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = modifier
                .onGloballyPositioned { coordinates ->
                    size = coordinates.size
                }
                .fillMaxSize()
                .background(themeColors.backPrimary)
        ) {
            GameBoard(
                gameBoard = screenState.gameBoard,
                activeObjectPosition = screenState.selectedObjectCoord,
                onObjectClick = { screenAction(GameScreenAction.OnBoardObjectClick(it)) },
                animationEvent = screenState.animationEvent,
                onAnimationFinished = { screenAction(GameScreenAction.OnAnimationFinished) },
                modifier = Modifier.padding(screenPadding.dp)
            )
        }
    }
}

@Composable
fun calculateBlockSize(size: IntSize, screePadding: Int): Dp {
    val density = LocalDensity.current
    val space = Config.SPACE_BETWEEN_BLOCK_RELATIVE
    val safeFactor = 0.9f

    val availableWidthPx = (size.width - 2 * screePadding).toFloat()
    val availableHeightPx = (size.height - 2 * screePadding).toFloat()

    val blockSizeX = availableWidthPx / (Config.BOARD_WIDTH + (Config.BOARD_WIDTH - 1) * space)
    val blockSizeY = availableHeightPx / (Config.BOARD_HEIGHT + (Config.BOARD_HEIGHT - 1) * space)

    val blockSizePx = min(blockSizeX, blockSizeY) * safeFactor
    val blockSizeDp = with(density) { blockSizePx.toDp().coerceAtLeast(0.dp) }
    return blockSizeDp
}

val LocalBlockSize = staticCompositionLocalOf<Dp> { 100.dp }

val blockSizeDp: Dp
    @Composable
    get() = LocalBlockSize.current

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
            idManager = IdManagerImpl(),
        )
        GameBoard(
            gameBoard = gameBoardManager.gameBoard.value,
            onObjectClick = {},
            onAnimationFinished = {},
        )
    }
}

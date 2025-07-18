package com.example.threeinarow.presentation.screens.gameScreen.components.board

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.threeinarow.data.AnimationTracker
import com.example.threeinarow.data.Config
import com.example.threeinarow.data.GameBoard
import com.example.threeinarow.data.GameBoardManagerImpl
import com.example.threeinarow.data.IdManagerImpl
import com.example.threeinarow.data.RandomManagerImpl
import com.example.threeinarow.data.fieldElements.Block
import com.example.threeinarow.data.fieldElements.BombBlock
import com.example.threeinarow.domain.models.AnimationData
import com.example.threeinarow.domain.models.AnimationEvent
import com.example.threeinarow.domain.models.Coord
import com.example.threeinarow.presentation.screens.gameScreen.blockSizeDp
import com.example.threeinarow.presentation.screens.gameScreen.components.board.animation.AnimatedGameBlock
import com.example.threeinarow.presentation.theme.AppTheme

@Composable
fun GameBoard(
    gameBoard: GameBoard,
    onObjectClick: (Coord) -> Unit,
    onAnimationFinished: () -> Unit,
    modifier: Modifier = Modifier,
    activeObjectPosition: Coord? = null,
    animationEvent: AnimationEvent? = null,
) {
    val height = gameBoard.height
    val width = gameBoard.width
    val spaceBetweenObjects = blockSizeDp * Config.SPACE_BETWEEN_BLOCK_RELATIVE

    var destroyingObjects = emptySet<Coord>()
    var fallingObjects = emptyMap<Coord, Int>()
    var spawningObjects = emptyMap<Coord, Int>()

    var numOfAnimations = 0

    when (animationEvent) {
        is AnimationEvent.Destroy -> {
            destroyingObjects = animationEvent.coords
            numOfAnimations = destroyingObjects.size
        }

        is AnimationEvent.Fall -> {
            fallingObjects = animationEvent.fallDistances
            numOfAnimations = fallingObjects.size
        }

        is AnimationEvent.Spawn -> {
            spawningObjects = animationEvent.spawnDistances
            numOfAnimations = spawningObjects.size
        }

        null -> {}
    }

    val animationTracker = remember(animationEvent, numOfAnimations) {
        AnimationTracker(
            total = numOfAnimations,
            onAllFinished = { onAnimationFinished() }
        )
    }

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

                    val animationData = when {
                        destroyingObjects.contains(coord) -> AnimationData.Destroy
                        fallingObjects.contains(coord) -> AnimationData.Fall(
                            fallingObjects.getValue(
                                coord
                            )
                        )

                        spawningObjects.contains(coord) -> AnimationData.Spawn(
                            spawningObjects.getValue(
                                coord
                            )
                        )

                        else -> AnimationData.Nothing
                    }
                    when (obj) {
                        is Block -> {
                            val explosionPattern = if (obj is BombBlock) {
                                obj.explosionPattern
                            } else {
                                null
                            }
                            AnimatedGameBlock(
                                block = obj,
                                isActive = isActive,
                                explosionPattern = explosionPattern,
                                onClick = { onObjectClick(Coord(x, y)) },
                                clickable = animationData is AnimationData.Nothing,
                                animationData = animationData,
                                onAnimationFinished = { animationTracker.notifyFinished() },
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
    widthDp = 1100,
    heightDp = 900,
)
@Composable
@SuppressLint("StateFlowValueCalledInComposition")
private fun GameBoardPreview() {
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

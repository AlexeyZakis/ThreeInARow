package com.example.threeinarow.data

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.threeinarow.domain.ExplosionPatterns
import com.example.threeinarow.domain.behavioral.Selectable
import com.example.threeinarow.domain.managers.GameBoardManager
import com.example.threeinarow.domain.managers.RandomManager
import com.example.threeinarow.domain.models.Coord
import com.example.threeinarow.presentation.screens.gameScreen.components.board.GameBoard
import com.example.threeinarow.presentation.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameBoardManagerImpl(
    override val width: Int,
    override val height: Int,
    val randomManager: RandomManager,
    val explosionEffectSpawnProbability: Int = Config.EXPLOSION_EFFECT_SPAWN_PROBABILITY,
    val explosionPatternsToProbabilityWeight: (ExplosionPatterns) -> Int =
        { Config.explosionPatternsToProbabilityWeight(it) },
    initGameBoard: GameBoard = GameBoard(
        width = width,
        height = height,
        explosionEffectSpawnProbability = explosionEffectSpawnProbability,
        explosionPatternsToProbabilityWeight = explosionPatternsToProbabilityWeight,
        randomManager = randomManager,
    )
) : GameBoardManager {
    private var _gameBoard: MutableStateFlow<GameBoard> =
        MutableStateFlow(initGameBoard)
    override val gameBoard = _gameBoard.asStateFlow()

    private var _selectedObjectCoord: MutableStateFlow<Coord?> = MutableStateFlow(null)
    override val selectedObjectCoord = _selectedObjectCoord.asStateFlow()

    init {
        spawnObjects()
        onBoardChange()
    }

    override fun onSelectObject(coord: Coord): Boolean {
        val gameBoard = _gameBoard.value
        val obj = gameBoard[coord]
        val isSelectable = obj is Selectable
        if (isSelectable) {
            onObjectSelected(newCoord = coord)
        }
        return isSelectable
    }

    private fun onObjectSelected(newCoord: Coord) {
        val oldSelectedObjectCoord = selectedObjectCoord.value
        val manhattanDistance = oldSelectedObjectCoord?.let { oldCoord ->
            manhattanDistance(
                coord1 = newCoord,
                coord2 = oldCoord,
            )
        }
        val newSelectedObjectCoord = when (manhattanDistance) {
            1 -> {
                val gameBoard = _gameBoard.value
                val successSwap = gameBoard.swapObjects(
                    coord1 = newCoord,
                    coord2 = oldSelectedObjectCoord,
                )
                if (successSwap) {
                    onBoardChange()
                    null
                } else {
                    newCoord
                }
            }

            0 -> null
            else -> newCoord
        }
        _selectedObjectCoord.value = newSelectedObjectCoord
    }

    private fun onBoardChange() {
        _gameBoard.update { gameBoard ->
            gameBoard.onBoardChange()
            gameBoard
        }
    }

    private fun spawnObjects() {
        _gameBoard.update { gameBoard ->
            gameBoard.spawnObjects()
            gameBoard
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

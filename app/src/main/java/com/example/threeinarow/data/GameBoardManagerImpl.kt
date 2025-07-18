package com.example.threeinarow.data

import com.example.threeinarow.domain.behavioral.Selectable
import com.example.threeinarow.domain.managers.GameBoardManager
import com.example.threeinarow.domain.managers.IdManager
import com.example.threeinarow.domain.managers.RandomManager
import com.example.threeinarow.domain.models.AnimationEvent
import com.example.threeinarow.domain.models.Coord
import com.example.threeinarow.domain.models.ExplosionPatterns
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class GameBoardManagerImpl(
    override val width: Int,
    override val height: Int,
    val randomManager: RandomManager,
    val idManager: IdManager,
    val explosionEffectSpawnProbability: Int = Config.EXPLOSION_EFFECT_SPAWN_PROBABILITY,
    val explosionPatternsToProbabilityWeight: (ExplosionPatterns) -> Int =
        { Config.explosionPatternsToProbabilityWeight(it) },
    initGameBoard: GameBoard = GameBoard(
        width = width,
        height = height,
        explosionEffectSpawnProbability = explosionEffectSpawnProbability,
        explosionPatternsToProbabilityWeight = explosionPatternsToProbabilityWeight,
        randomManager = randomManager,
        idManager = idManager,
    )
) : GameBoardManager {
    private var _gameBoard: MutableStateFlow<GameBoard> =
        MutableStateFlow(initGameBoard)
    override val gameBoard = _gameBoard.asStateFlow()

    private var _selectedObjectCoord: MutableStateFlow<Coord?> = MutableStateFlow(null)
    override val selectedObjectCoord = _selectedObjectCoord.asStateFlow()

    private val _animationEvent = MutableStateFlow<AnimationEvent?>(null)
    override val animationEvent = _animationEvent.asStateFlow()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        spawnObjects()
        scope.launch {
            onBoardChange(
                withAnimation = false
            )
        }
    }

    override suspend fun onSelectObject(coord: Coord): Boolean {
        val gameBoard = _gameBoard.value
        val obj = gameBoard[coord]
        val isSelectable = obj is Selectable
        if (isSelectable) {
            onObjectSelected(newCoord = coord)
        }
        return isSelectable
    }

    override fun release() {
        scope.cancel()
    }

    private suspend fun onObjectSelected(newCoord: Coord) {
        val animationEvent = _animationEvent.value
        if (animationEvent != null) {
            return
        }
        val oldSelectedObjectCoord = selectedObjectCoord.value
        val manhattanDistance = oldSelectedObjectCoord?.let { oldCoord ->
            manhattanDistance(
                coord1 = newCoord,
                coord2 = oldCoord,
            )
        }
        when (manhattanDistance) {
            1 -> {
                val gameBoard = _gameBoard.value
                val successSwap = gameBoard.swapObjects(
                    coord1 = newCoord,
                    coord2 = oldSelectedObjectCoord,
                )
                if (successSwap) {
                    _selectedObjectCoord.value = null
                    onBoardChange()
                } else {
                    _selectedObjectCoord.value = newCoord
                }
            }

            0 -> _selectedObjectCoord.value = null
            else -> _selectedObjectCoord.value = newCoord
        }
    }

    private fun spawnObjects() {
        _gameBoard.update { gameBoard ->
            gameBoard.spawnObjects()
            gameBoard
        }
    }

    private suspend fun onBoardChange(
        withAnimation: Boolean = true,
    ) {
        val board = _gameBoard.value
        board.onBoardChange(
            onAnimateDestroy = { coords ->
                _animationEvent.value = AnimationEvent.Destroy(coords)
                if (withAnimation) {
                    waitAnimationComplete()
                }
                _animationEvent.value = null
                _gameBoard.value = board
            },
            onAnimateFall = { fallDistances ->
                _animationEvent.value = AnimationEvent.Fall(fallDistances)
                if (withAnimation) {
                    waitAnimationComplete()
                }
                _animationEvent.value = null
                _gameBoard.value = board
            },
            onAnimateSpawn = { spawnDistances, tempGameBoard ->
                _gameBoard.value = tempGameBoard
                _animationEvent.value = AnimationEvent.Spawn(spawnDistances)
                if (withAnimation) {
                    waitAnimationComplete()
                }
                _animationEvent.value = null
                _gameBoard.value = board
            }
        )
    }

    private var animationCompletion = CompletableDeferred<Unit>()

    private suspend fun waitAnimationComplete() {
        animationCompletion = CompletableDeferred()
        animationCompletion.await()
    }

    override fun onAnimationFinished() {
        if (!animationCompletion.isCompleted) {
            animationCompletion.complete(Unit)
        }
    }
}

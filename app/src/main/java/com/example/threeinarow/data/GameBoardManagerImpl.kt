package com.example.threeinarow.data

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.threeinarow.data.fieldElements.EmptyObject
import com.example.threeinarow.domain.behavioral.Destroyable
import com.example.threeinarow.domain.behavioral.Swappable
import com.example.threeinarow.domain.behavioral.Unfallable
import com.example.threeinarow.domain.gameObjects.GameBoardObject
import com.example.threeinarow.domain.managers.GameBoardManager
import com.example.threeinarow.domain.managers.RandomManager
import com.example.threeinarow.presentation.screens.gameScreen.components.board.GameBoard
import com.example.threeinarow.presentation.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class GameBoardManagerImpl(
    override val width: Int,
    override val height: Int,
    val randomManager: RandomManager,
): GameBoardManager {
    private var _gameBoard: MutableStateFlow<List<List<GameBoardObject>>> =
        MutableStateFlow(getEmptyBoard())
    override val gameBoard = _gameBoard.asStateFlow()

    init {
        _gameBoard.value = spawnObjects(_gameBoard.value)
        onBoardChange()
    }

    override fun destroyObjectAt(x: Int, y: Int) {
        if (isNotValidPosition(x, y)) return
        val gameBoard = _gameBoard.value
        val newGameBoard = getEmptyBoard()
        val obj = gameBoard[y][x]
        if (obj !is Destroyable) return

        for (i in 0 ..< height) {
            for (j in 0..< width) {
                newGameBoard[y][x] = if (x == j && y == i ) {
                    EmptyObject()
                } else {
                    obj
                }
            }
        }
        _gameBoard.value = newGameBoard
        obj.onDestroy()
        onBoardChange()
    }

    override fun swapObjects(
        x1: Int,
        y1: Int,
        x2: Int,
        y2: Int,
    ) {
        val gameBoard = _gameBoard.value
        val obj1 = gameBoard[y1][x1]
        val obj2 = gameBoard[y2][x2]

        if (obj1 !is Swappable || obj2 !is Swappable) return

        val newGameBoard = getEmptyBoard()
        for (y in 0 ..< height) {
            for (x in 0..< width) {
                val obj = gameBoard[y][x]
                newGameBoard[y][x] = when {
                    x == x1 && y == y1 -> obj2
                    x == x2 && y == y2 -> obj1
                    else -> obj
                }
            }
        }

        val objectsCordsToDestroy = findConnections(
            gameBoard = newGameBoard,
        )
        if (objectsCordsToDestroy.isEmpty()) return

        _gameBoard.value = newGameBoard
        obj1.onSwap()
        obj2.onSwap()
        onBoardChange()
    }

    override fun onBoardChange() {
        var newGameBoard = _gameBoard.value
        while (true) {
            val objectsCordsToDestroy = findConnections(
                gameBoard = newGameBoard,
            )
            if (objectsCordsToDestroy.isEmpty()) break
            newGameBoard = destroyConnectedObjects(
                gameBoard = newGameBoard,
                objectsCordsToDestroy = objectsCordsToDestroy,
            )
            newGameBoard = moveObjectsDown(
                gameBoard = newGameBoard,
            )
            newGameBoard = spawnObjects(
                gameBoard = newGameBoard,
            )
        }
        _gameBoard.value = newGameBoard
    }

    private fun spawnObjects(
        gameBoard: List<List<GameBoardObject>>,
    ): List<List<GameBoardObject>> {
        val newGameBoard = getEmptyBoard()
        for (y in 0 ..< height) {
            for (x in 0..< width) {
                val obj = gameBoard[y][x]
                newGameBoard[y][x] = if (obj is EmptyObject) {
                    randomManager.getRandomObject()
                } else {
                    obj
                }
            }
        }
        return newGameBoard
    }

    private fun moveObjectsDown(
        gameBoard: List<List<GameBoardObject>>,
    ): List<List<GameBoardObject>> {
        val newGameBoard = getEmptyBoard()
        for (x in 0 ..< width) {
            val fallableObjects = getColumnWithFilter(
                gameBoard = gameBoard,
                x = x,
                filter = { it !is Unfallable },
            ).toMutableList()
            for (y in (0 ..< height).reversed()) {
                val obj = gameBoard[y][x]
                newGameBoard[y][x] = if (obj is Unfallable && obj !is EmptyObject) {
                    obj
                } else {
                    if (fallableObjects.isNotEmpty()) {
                        val lowestObj = fallableObjects.last()
                        fallableObjects.removeLastOrNull()
                        lowestObj
                    } else {
                        EmptyObject()
                    }
                }
            }
        }
        return newGameBoard
    }

    private fun destroyConnectedObjects(
        gameBoard: List<List<GameBoardObject>>,
        objectsCordsToDestroy: Set<Pair<Int, Int>>
    ): List<List<GameBoardObject>> {
        val newGameBoard = getEmptyBoard()
        for (y in 0 ..< height) {
            for (x in 0..< width) {
                val obj = gameBoard[y][x]
                newGameBoard[y][x] = if (
                    objectsCordsToDestroy.contains(Pair(x, y))
                    && obj is Destroyable
                ) {
                    EmptyObject()
                } else {
                    obj
                }
            }
        }
        for (objCords in objectsCordsToDestroy) {
            val obj = gameBoard[objCords.second][objCords.first]
            if (obj is Destroyable) {
                obj.onDestroy()
            }
        }
        return newGameBoard
    }

    private fun onSwap() {
        onBoardChange()
    }

    private fun getColumnWithFilter(
        gameBoard: List<List<GameBoardObject>>,
        x: Int,
        filter: (GameBoardObject) -> Boolean = { true }
    ): List<GameBoardObject> {
        val objects = mutableListOf<GameBoardObject>()
        for (y in 0 ..< height) {
            val obj = gameBoard[y][x]
            if (filter(obj)) {
                objects.add(obj)
            }
        }
        return objects
    }

    private fun isNotValidPosition(x: Int, y: Int): Boolean {
        return x !in (0 ..< width) || y !in (0 ..< height)
    }

    private fun getEmptyBoard(): MutableList<MutableList<GameBoardObject>> {
        return MutableList(height) { row ->
            MutableList(width) { col ->
                EmptyObject()
            }
        }
    }

    private fun findConnections(gameBoard: List<List<GameBoardObject>>): Set<Pair<Int, Int>> {
        return ConnectionFinder.findConnections(
            width = width,
            height = height,
            gameBoard = gameBoard,
        )
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

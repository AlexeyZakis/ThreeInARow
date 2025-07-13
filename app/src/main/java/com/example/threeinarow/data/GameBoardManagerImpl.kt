package com.example.threeinarow.data

import android.annotation.SuppressLint
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.example.threeinarow.data.fieldElements.Block
import com.example.threeinarow.data.fieldElements.Bomb
import com.example.threeinarow.data.fieldElements.EmptyObject
import com.example.threeinarow.domain.ExplosionPatterns
import com.example.threeinarow.domain.behavioral.Destroyable
import com.example.threeinarow.domain.behavioral.Swappable
import com.example.threeinarow.domain.behavioral.Unfallable
import com.example.threeinarow.domain.gameObjects.GameBoardObject
import com.example.threeinarow.domain.managers.GameBoardManager
import com.example.threeinarow.domain.managers.RandomManager
import com.example.threeinarow.domain.models.Coord
import com.example.threeinarow.presentation.screens.gameScreen.components.board.GameBoard
import com.example.threeinarow.presentation.theme.AppTheme
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class GameBoardManagerImpl(
    override val width: Int,
    override val height: Int,
    val randomManager: RandomManager,
) : GameBoardManager {
    private var _gameBoard: MutableStateFlow<GameBoard> =
        MutableStateFlow(
            GameBoard(
                width = width,
                height = height,
            )
        )
    override val gameBoard = _gameBoard.asStateFlow()

    private val objectsCoordsToDestroy = mutableSetOf<Coord>()

    init {
        _gameBoard.value = spawnObjects(_gameBoard.value)
        onBoardChange()
    }

    override fun destroyObjectAt(coord: Coord) {
        if (isNotValidPosition(coord)) return
        val gameBoard = _gameBoard.value
        val obj = gameBoard[coord]
        if (obj !is Destroyable) return
        if (objectsCoordsToDestroy.contains(coord)) return
        objectsCoordsToDestroy.add(coord)
        obj.onDestroy(this, coord)
    }

    override fun onSwapObjects(
        coord1: Coord,
        coord2: Coord,
    ): Boolean {
        val gameBoard = _gameBoard.value

        val obj1 = gameBoard[coord1]
        val obj2 = gameBoard[coord2]

        if (obj1 !is Swappable || obj2 !is Swappable) return false

        val newGameBoard = gameBoard.copy()
        newGameBoard.swapObjects(
            coord1 = coord1,
            coord2 = coord2,
        )
        if (!canSwap(newGameBoard)) return false

        _gameBoard.value = newGameBoard
        onBoardChange()
        return true
    }

    override fun onBoardChange() {
        var newGameBoard = _gameBoard.value
        while (true) {
            val connectedObjectsCoords = findConnections(
                gameBoard = newGameBoard,
            )
            if (connectedObjectsCoords.isEmpty()) break
            addObjectsCoordsToDestroySet(
                gameBoard = newGameBoard,
                objectsToDestroyCoords = connectedObjectsCoords,
            )
            newGameBoard = destroyObjects(
                gameBoard = newGameBoard,
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

    override fun spawnGameBoardObjectAt(
        gameBoardObject: GameBoardObject,
        coord: Coord,
    ) {
        val gameBoard = _gameBoard.value
        val newGameBoard = gameBoard.copy()
        newGameBoard[coord] = gameBoardObject
        _gameBoard.value = newGameBoard
    }

    fun addBombToObjectAt(
        coord: Coord,
        explosionPattern: ExplosionPatterns
    ) {
        val gameBoard = _gameBoard.value
        val newGameBoard = gameBoard.copy()
        val obj = newGameBoard[coord]

        if (obj !is Block) return

        val bomb = Bomb.addBombToBlock(obj, explosionPattern)
        newGameBoard[coord] = bomb
        _gameBoard.value = newGameBoard
    }

    fun canSwap(gameBoard: GameBoard): Boolean {
        val objectsCordsToDestroy = findConnections(
            gameBoard = gameBoard,
        )
        return objectsCordsToDestroy.isNotEmpty()
    }

    private fun spawnObjects(gameBoard: GameBoard): GameBoard {
        val newGameBoard = gameBoard.copy()
        newGameBoard.fillEmptyObjects(randomManager)
        return newGameBoard
    }

    private fun destroyObjects(
        gameBoard: GameBoard,
    ): GameBoard {
        val newGameBoard = gameBoard.copy()
        for (coord in objectsCoordsToDestroy) {
            newGameBoard[coord] = EmptyObject()
        }
        objectsCoordsToDestroy.clear()
        return newGameBoard
    }

    private fun moveObjectsDown(
        gameBoard: GameBoard,
    ): GameBoard {
        val newGameBoard = getEmptyBoard()
        for (x in 0..<width) {
            val fallableObjects = gameBoard.getColumnWithFilter(
                x = x,
                filter = { it !is Unfallable },
            ).toMutableList()
            for (y in (0..<height).reversed()) {
                val curCoord = Coord(x, y)
                val obj = gameBoard[curCoord]
                newGameBoard[curCoord] = if (obj is Unfallable && obj !is EmptyObject) {
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

    private fun addObjectsCoordsToDestroySet(
        gameBoard: GameBoard,
        objectsToDestroyCoords: Set<Coord>
    ) {
        for (y in 0..<height) {
            for (x in 0..<width) {
                val curCoord = Coord(x, y)
                val obj = gameBoard[curCoord]
                if (
                    objectsToDestroyCoords.contains(curCoord)
                    && obj is Destroyable
                ) {
                    if (objectsCoordsToDestroy.contains(curCoord)) continue
                    objectsCoordsToDestroy.add(curCoord)
                    obj.onDestroy(this, curCoord)
                }
            }
        }
    }

    private fun isNotValidPosition(coord: Coord): Boolean {
        return coord.x !in (0..<width) || coord.y !in (0..<height)
    }

    private fun getEmptyBoard(): GameBoard {
        return GameBoard(
            width = width,
            height = height,
        )
    }

    private fun findConnections(gameBoard: GameBoard): Set<Coord> {
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
        GameBoard(
            gameBoard = gameBoardManager.gameBoard.value,
            onObjectClick = {},
        )
    }
}

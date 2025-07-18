package com.example.threeinarow

import com.example.threeinarow.data.GameBoard
import com.example.threeinarow.data.RandomManagerImpl
import com.example.threeinarow.data.fieldElements.EmptyObject
import com.example.threeinarow.domain.managers.IdManager
import com.example.threeinarow.domain.models.Coord
import com.example.threeinarow.domain.models.Id
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Test

class GameBoardTest {
    fun getFiledGameBoard(): GameBoard {
        val width = 10
        val height = 8
        val idManager = TestIdManager()
        val randomManager = RandomManagerImpl()
        val gameBoard = GameBoard(
            width = width,
            height = height,
            randomManager = randomManager,
            idManager = idManager,
        )

        gameBoard.fillEmptyObjects()
        return gameBoard
    }

    @Test
    fun `Fill empty board with objects`() {
        val gameBoard = getFiledGameBoard()

        for (y in 0..<gameBoard.height) {
            for (x in 0..<gameBoard.width) {
                val curCoord = Coord(x, y)
                val obj = gameBoard[curCoord]
                assertNotEquals(obj, EmptyObject(Id()))
            }
        }
    }

    @Test
    fun `Swap objects`() {
        val gameBoard = getFiledGameBoard()

        val gameBoardBefore = gameBoard.copy()

        val coord1 = Coord(0, 0)
        val coord2 = Coord(1, 1)

        val objBefore1 = gameBoardBefore[coord1]
        val objBefore2 = gameBoardBefore[coord2]

        gameBoard.swapObjects(
            coord1 = coord1,
            coord2 = coord2,
            checkConnections = false,
        )

        for (y in 0..<gameBoard.height) {
            for (x in 0..<gameBoard.width) {
                val curCoord = Coord(x, y)
                val objBefore = gameBoardBefore[curCoord]
                val objAfter = gameBoard[curCoord]
                when (curCoord) {
                    coord1 -> assertEquals(objAfter, objBefore2)
                    coord2 -> assertEquals(objAfter, objBefore1)
                    else -> assertEquals(objAfter, objBefore)
                }
            }
        }
    }

    class TestIdManager : IdManager {
        override fun getNextSessionId(): Id {
            return Id()
        }
    }
}

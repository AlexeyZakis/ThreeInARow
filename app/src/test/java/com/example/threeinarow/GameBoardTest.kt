package com.example.threeinarow

import com.example.threeinarow.data.GameBoard
import com.example.threeinarow.data.RandomManagerImpl
import com.example.threeinarow.data.fieldElements.EmptyObject
import com.example.threeinarow.domain.models.Coord
import org.junit.Assert.assertEquals
import org.junit.Test

class GameBoardTest {
    @Test
    fun `Fill empty objects`() {
        val width = 10
        val height = 8
        val gameBoard = GameBoard(
            width = width,
            height = height,
        )
        val randomManager = RandomManagerImpl(1428)

        gameBoard.fillEmptyObjects(randomManager)

        for (y in 0..<height) {
            for (x in 0..<width) {
                val curCoord = Coord(x, y)
                val obj = gameBoard[curCoord]
                assert(obj !is EmptyObject) { "At $curCoord an EmptyObject" }
            }
        }
    }

    @Test
    fun `Swap objects`() {
        val width = 10
        val height = 8
        val gameBoard = GameBoard(
            width = width,
            height = height,
        )
        val randomManager = RandomManagerImpl(1428)
        gameBoard.fillEmptyObjects(randomManager)

        val gameBoardBefore = gameBoard.copy()

        val coord1 = Coord(0, 0)
        val coord2 = Coord(2, 0)

        val objBefore1 = gameBoardBefore[coord1]
        val objBefore2 = gameBoardBefore[coord2]

        gameBoard.swapObjects(coord1, coord2)

        for (y in 0..<height) {
            for (x in 0..<width) {
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
}

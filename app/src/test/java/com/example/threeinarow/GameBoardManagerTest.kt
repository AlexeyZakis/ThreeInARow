package com.example.threeinarow

import com.example.threeinarow.data.GameBoardManagerImpl
import com.example.threeinarow.data.RandomManagerImpl
import com.example.threeinarow.domain.models.Coord
import org.junit.Assert.assertEquals
import org.junit.Test

class GameBoardManagerTest {
    @Test
    fun `Swap vertical horizontal objects when can swap`() {
        val width = 10
        val height = 8
        val gameBoardManager = GameBoardManagerImpl(
            width = width,
            height = height,
            randomManager = RandomManagerImpl(1428)
        )
        val gameBoardBefore = gameBoardManager.gameBoard.value

        val coord1 = Coord(2, 0)
        val coord2 = Coord(2, 1)

        val objBefore1 = gameBoardBefore[coord1]
        val objBefore2 = gameBoardBefore[coord2]

        gameBoardManager.onSwapObjects(coord1, coord2)

        val gameBoardAfter = gameBoardManager.gameBoard.value

        for (y in 0..<height) {
            for (x in 0..<width) {
                val curCoord = Coord(x, y)
                val objBefore = gameBoardBefore[curCoord]
                val objAfter = gameBoardAfter[curCoord]
                when (curCoord) {
                    coord1 -> assertEquals(objAfter, objBefore2)
                    coord2 -> assertEquals(objAfter, objBefore1)
                    else -> assertEquals(objAfter, objBefore)
                }
            }
        }
    }
}

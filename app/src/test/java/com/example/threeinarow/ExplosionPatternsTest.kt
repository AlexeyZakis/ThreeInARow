package com.example.threeinarow

import com.example.threeinarow.data.GameBoard
import com.example.threeinarow.data.RandomManagerImpl
import com.example.threeinarow.data.fieldElements.EmptyObject
import com.example.threeinarow.domain.managers.IdManager
import com.example.threeinarow.domain.models.Coord
import com.example.threeinarow.domain.models.ExplosionPatterns
import com.example.threeinarow.domain.models.Id
import org.junit.Test
import kotlin.math.abs

class ExplosionPatternsTest {
    @Test
    fun `Explosion pattern 3x3`() {
        testExplosionPattern(
            explosionPattern = ExplosionPatterns.Bomb3x3
        ) { curCoord, bombCoord ->
            val radius = 1
            val shouldBeEmptyObjectByCoords = abs(curCoord.x - bombCoord.x) <= radius
                    && abs(curCoord.y - bombCoord.y) <= radius
            shouldBeEmptyObjectByCoords
        }
    }

    @Test
    fun `Explosion pattern 5x5`() {
        testExplosionPattern(
            explosionPattern = ExplosionPatterns.Bomb5x5
        ) { curCoord, bombCoord ->
            val radius = 2
            val shouldBeEmptyObjectByCoords = abs(curCoord.x - bombCoord.x) <= radius
                    && abs(curCoord.y - bombCoord.y) <= radius
            shouldBeEmptyObjectByCoords
        }
    }

    @Test
    fun `Explosion pattern horizontal line`() {
        testExplosionPattern(
            explosionPattern = ExplosionPatterns.HorizontalLine
        ) { curCoord, bombCoord ->
            val shouldBeEmptyObjectByCoords = curCoord.y == bombCoord.y
            shouldBeEmptyObjectByCoords
        }
    }

    @Test
    fun `Explosion pattern vertical line`() {
        testExplosionPattern(
            explosionPattern = ExplosionPatterns.VerticalLine
        ) { curCoord, bombCoord ->
            val shouldBeEmptyObjectByCoords = curCoord.x == bombCoord.x
            shouldBeEmptyObjectByCoords
        }
    }

    @Test
    fun `Explosion pattern cross`() {
        testExplosionPattern(
            explosionPattern = ExplosionPatterns.Cross
        ) { curCoord, bombCoord ->
            val shouldBeEmptyObjectByCoords = curCoord.x == bombCoord.x
                    || curCoord.y == bombCoord.y
            shouldBeEmptyObjectByCoords
        }
    }

    @Test
    fun `Explosion pattern diagonal left`() {
        testExplosionPattern(
            explosionPattern = ExplosionPatterns.DiagonalLeft
        ) { curCoord, bombCoord ->
            val deltaX = curCoord.x - bombCoord.x
            val deltaY = curCoord.y - bombCoord.y
            val shouldBeEmptyObjectByCoords = deltaX == deltaY
            shouldBeEmptyObjectByCoords
        }
    }

    @Test
    fun `Explosion pattern diagonal right`() {
        testExplosionPattern(
            explosionPattern = ExplosionPatterns.DiagonalRight
        ) { curCoord, bombCoord ->
            val deltaX = curCoord.x - bombCoord.x
            val deltaY = curCoord.y - bombCoord.y
            val shouldBeEmptyObjectByCoords = deltaX == -deltaY
            shouldBeEmptyObjectByCoords
        }
    }

    @Test
    fun `Explosion pattern x`() {
        testExplosionPattern(
            explosionPattern = ExplosionPatterns.X
        ) { curCoord, bombCoord ->
            val deltaX = curCoord.x - bombCoord.x
            val deltaY = curCoord.y - bombCoord.y
            val shouldBeEmptyObjectByCoords = deltaX == deltaY || deltaX == -deltaY
            shouldBeEmptyObjectByCoords
        }
    }

    @Test
    fun `Explosion pattern cross x`() {
        testExplosionPattern(
            explosionPattern = ExplosionPatterns.CrossX
        ) { curCoord, bombCoord ->
            val deltaX = curCoord.x - bombCoord.x
            val deltaY = curCoord.y - bombCoord.y
            val shouldBeEmptyObjectByCoords = curCoord.x == bombCoord.x
                    || curCoord.y == bombCoord.y
                    || deltaX == deltaY
                    || deltaX == -deltaY
            shouldBeEmptyObjectByCoords
        }
    }

    @Test
    fun `Explosion pattern all`() {
        testExplosionPattern(
            explosionPattern = ExplosionPatterns.All
        ) { curCoord, bombCoord ->
            val shouldBeEmptyObjectByCoords = true
            shouldBeEmptyObjectByCoords
        }
    }

    private fun testExplosionPattern(
        explosionPattern: ExplosionPatterns,
        shouldBeEmptyObjectByCoords: (curCoord: Coord, bombCoord: Coord) -> Boolean,
    ) {
        val width = 9
        val height = 9
        val randomManager = RandomManagerImpl()
        val gameBoard = GameBoard(
            width = width,
            height = height,
            randomManager = randomManager,
            idManager = TestIdManager()
        )
        val bombCoord = gameBoard.getRandomCoord()

        gameBoard.fillEmptyObjects()

        val gameBoardBefore = gameBoard.copy()

        gameBoard.addBombToObjectAt(
            coord = bombCoord,
            explosionPattern = explosionPattern,
        )

        gameBoard.addCoordToDestroySet(bombCoord)
        gameBoard.destroyObjects()

        for (y in 0..<height) {
            for (x in 0..<width) {
                val curCoord = Coord(x, y)
                val objBefore = gameBoardBefore[curCoord]
                val objAfter = gameBoard[curCoord]
                if (shouldBeEmptyObjectByCoords(curCoord, bombCoord)) {
                    assert(objAfter is EmptyObject)
                } else {
                    assert(objAfter == objBefore)
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

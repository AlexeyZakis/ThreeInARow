package com.example.threeinarow.data.explosionPattern

import com.example.threeinarow.data.GameBoard
import com.example.threeinarow.domain.models.Coord
import com.example.threeinarow.domain.objectEffects.ExplosionPattern

object ExplosionPatternDiagonalRight : ExplosionPattern { //    / - pattern
    override fun apply(gameBoard: GameBoard, coord: Coord): Set<Coord> {
        val objectsToDestroy = mutableSetOf<Coord>()
        var x = coord.x
        var y = coord.y
        while (x < gameBoard.width && y >= 0) {
            objectsToDestroy.add(Coord(x, y))
            x++; y--
        }

        x = coord.x - 1
        y = coord.y + 1
        while (x >= 0 && y < gameBoard.height) {
            objectsToDestroy.add(Coord(x, y))
            x--; y++
        }
        return objectsToDestroy
    }
}

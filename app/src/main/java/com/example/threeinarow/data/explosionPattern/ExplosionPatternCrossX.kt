package com.example.threeinarow.data.explosionPattern

import com.example.threeinarow.data.GameBoard
import com.example.threeinarow.domain.models.Coord
import com.example.threeinarow.domain.objectEffects.ExplosionPattern

object ExplosionPatternCrossX : ExplosionPattern {
    override fun apply(gameBoard: GameBoard, coord: Coord): Set<Coord> {
        val objectsToDestroy = mutableSetOf<Coord>()
        for (x in 0 until gameBoard.width) {
            objectsToDestroy.add(Coord(x, coord.y))
        }
        for (y in 0 until gameBoard.height) {
            objectsToDestroy.add(Coord(coord.x, y))
        }
        var x = coord.x
        var y = coord.y
        while (x >= 0 && y >= 0) {
            objectsToDestroy.add(Coord(x, y))
            x--; y--
        }
        x = coord.x + 1
        y = coord.y + 1
        while (x < gameBoard.width && y < gameBoard.height) {
            objectsToDestroy.add(Coord(x, y))
            x++; y++
        }

        x = coord.x + 1
        y = coord.y - 1
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

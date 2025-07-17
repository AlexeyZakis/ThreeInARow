package com.example.threeinarow.data.explosionPattern

import com.example.threeinarow.data.GameBoard
import com.example.threeinarow.domain.models.Coord
import com.example.threeinarow.domain.objectEffects.ExplosionPattern

abstract class ExplosionPatternNxM(
    private val rangeX: Int,
    private val rangeY: Int
) : ExplosionPattern {

    override fun apply(gameBoard: GameBoard, coord: Coord) {
        for (dy in -rangeY..rangeY) {
            for (dx in -rangeX..rangeX) {
                val x = coord.x + dx
                val y = coord.y + dy
                gameBoard.addCoordToDestroySet(Coord(x, y))
            }
        }
    }
}

object ExplosionPattern3x3 : ExplosionPatternNxM(1, 1)
object ExplosionPattern5x5 : ExplosionPatternNxM(2, 2)

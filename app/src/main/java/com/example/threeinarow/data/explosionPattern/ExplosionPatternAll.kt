package com.example.threeinarow.data.explosionPattern

import com.example.threeinarow.data.GameBoard
import com.example.threeinarow.domain.models.Coord
import com.example.threeinarow.domain.objectEffects.ExplosionPattern

object ExplosionPatternAll : ExplosionPattern {
    override fun apply(gameBoard: GameBoard, coord: Coord) {
        for (y in 0 until gameBoard.height) {
            for (x in 0 until gameBoard.width) {
                gameBoard.addCoordToDestroySet(Coord(x, y))
            }
        }
    }
}

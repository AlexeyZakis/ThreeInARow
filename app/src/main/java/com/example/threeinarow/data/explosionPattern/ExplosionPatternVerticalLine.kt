package com.example.threeinarow.data.explosionPattern

import com.example.threeinarow.domain.managers.GameBoardManager
import com.example.threeinarow.domain.models.Coord
import com.example.threeinarow.domain.objectEffects.ExplosionPattern

object ExplosionPatternVerticalLine : ExplosionPattern {
    override fun apply(board: GameBoardManager, coord: Coord) {
        for (y in 0 until board.height) {
            board.destroyObjectAt(Coord(coord.x, y))
        }
    }
}

package com.example.threeinarow.data.explosionPattern

import com.example.threeinarow.domain.managers.GameBoardManager
import com.example.threeinarow.domain.models.Coord
import com.example.threeinarow.domain.objectEffects.ExplosionPattern

object ExplosionPatternCrossLine : ExplosionPattern {
    override fun apply(board: GameBoardManager, coord: Coord) {
        for (x in 0 until board.width) {
            board.destroyObjectAt(Coord(x, coord.y))
        }
        for (y in 0 until board.height) {
            board.destroyObjectAt(Coord(coord.x, y))
        }
    }
}

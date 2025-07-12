package com.example.threeinarow.data.explosionPattern

import com.example.threeinarow.domain.ExplosionPattern
import com.example.threeinarow.domain.managers.GameBoardManager

object ExplosionPatternHorizontalLine : ExplosionPattern {
    override fun apply(board: GameBoardManager, x: Int, y: Int) {
        for (x in 0 until board.width) {
            board.destroyObjectAt(x, y)
        }
    }
}

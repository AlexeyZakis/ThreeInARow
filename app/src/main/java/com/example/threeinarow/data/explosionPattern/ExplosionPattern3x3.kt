package com.example.threeinarow.data.explosionPattern

import com.example.threeinarow.domain.ExplosionPattern
import com.example.threeinarow.domain.managers.GameBoardManager

object ExplosionPattern3x3 : ExplosionPattern {
    override fun apply(board: GameBoardManager, originX: Int, originY: Int) {
        for (dy in -1..1) {
            for (dx in -1..1) {
                val x = originX + dx
                val y = originY + dy
                board.destroyObjectAt(x, y)
            }
        }
    }
}

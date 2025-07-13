package com.example.threeinarow.domain.objectEffects

import com.example.threeinarow.domain.managers.GameBoardManager
import com.example.threeinarow.domain.models.Coord

fun interface ExplosionPattern : ObjectEffect {
    fun apply(board: GameBoardManager, coord: Coord)
}

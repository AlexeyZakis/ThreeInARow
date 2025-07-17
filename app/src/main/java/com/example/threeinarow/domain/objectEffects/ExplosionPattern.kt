package com.example.threeinarow.domain.objectEffects

import com.example.threeinarow.data.GameBoard
import com.example.threeinarow.domain.models.Coord

fun interface ExplosionPattern : ObjectEffect {
    fun apply(gameBoard: GameBoard, coord: Coord)
}

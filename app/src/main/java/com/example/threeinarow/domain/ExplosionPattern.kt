package com.example.threeinarow.domain

import com.example.threeinarow.domain.managers.GameBoardManager

fun interface ExplosionPattern {
    fun apply(board: GameBoardManager, x: Int, y: Int)
}
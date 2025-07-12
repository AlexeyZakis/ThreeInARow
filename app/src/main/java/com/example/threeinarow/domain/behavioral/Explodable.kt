package com.example.threeinarow.domain.behavioral

import com.example.threeinarow.domain.managers.GameBoardManager

interface Explodable {
    fun explode(board: GameBoardManager, x: Int, y: Int)
}

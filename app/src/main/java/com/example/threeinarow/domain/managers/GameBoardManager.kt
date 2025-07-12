package com.example.threeinarow.domain.managers

import com.example.threeinarow.domain.gameObjects.GameBoardObject
import kotlinx.coroutines.flow.StateFlow

interface GameBoardManager {
    val width: Int
    val height: Int

    val gameBoard: StateFlow<List<List<GameBoardObject>>>

    fun onBoardChange()
    fun destroyObjectAt(x: Int, y: Int)
    fun swapObjects(x1: Int, y1: Int, x2: Int, y2: Int)
}

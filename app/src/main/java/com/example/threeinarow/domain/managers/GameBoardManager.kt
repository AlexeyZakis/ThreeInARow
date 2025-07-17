package com.example.threeinarow.domain.managers

import com.example.threeinarow.data.GameBoard
import com.example.threeinarow.domain.models.Coord
import kotlinx.coroutines.flow.StateFlow

interface GameBoardManager {
    val width: Int
    val height: Int

    val gameBoard: StateFlow<GameBoard>
    val selectedObjectCoord: StateFlow<Coord?>

    fun onSelectObject(coord: Coord): Boolean
}

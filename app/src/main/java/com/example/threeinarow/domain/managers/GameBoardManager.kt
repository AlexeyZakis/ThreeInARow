package com.example.threeinarow.domain.managers

import com.example.threeinarow.data.GameBoard
import com.example.threeinarow.domain.gameObjects.GameBoardObject
import com.example.threeinarow.domain.models.Coord
import kotlinx.coroutines.flow.StateFlow

interface GameBoardManager {
    val width: Int
    val height: Int

    val gameBoard: StateFlow<GameBoard>

    fun onBoardChange()
    fun destroyObjectAt(coord: Coord)
    fun onSwapObjects(coord1: Coord, coord2: Coord): Boolean

    fun spawnGameBoardObjectAt(
        gameBoardObject: GameBoardObject,
        coord: Coord,
    )
}

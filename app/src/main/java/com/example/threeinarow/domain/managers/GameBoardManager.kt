package com.example.threeinarow.domain.managers

import com.example.threeinarow.data.GameBoard
import com.example.threeinarow.domain.models.AnimationEvent
import com.example.threeinarow.domain.models.Coord
import kotlinx.coroutines.flow.StateFlow

interface GameBoardManager {
    val width: Int
    val height: Int

    val gameBoard: StateFlow<GameBoard>
    val selectedObjectCoord: StateFlow<Coord?>
    val animationEvent: StateFlow<AnimationEvent?>

    suspend fun onSelectObject(coord: Coord): Boolean
    fun onAnimationFinished()

    fun release()
}

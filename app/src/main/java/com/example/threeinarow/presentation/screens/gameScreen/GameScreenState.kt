package com.example.threeinarow.presentation.screens.gameScreen

import com.example.threeinarow.data.GameBoard
import com.example.threeinarow.domain.models.AnimationEvent
import com.example.threeinarow.domain.models.Coord

data class GameScreenState(
    val gameBoard: GameBoard = GameBoard(),
    val selectedObjectCoord: Coord? = null,
    val animationEvent: AnimationEvent? = null,
)

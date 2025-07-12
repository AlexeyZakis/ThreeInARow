package com.example.threeinarow.presentation.screens.gameScreen

import com.example.threeinarow.domain.gameObjects.GameBoardObject

data class GameScreenState(
    val gameBoard: List<List<GameBoardObject>> = emptyList(),
    val activeObjectPosition: Pair<Int, Int>? = null,
)

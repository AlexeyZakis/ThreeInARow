package com.example.threeinarow.presentation.screens.gameScreen

import com.example.threeinarow.domain.models.Coord

sealed class GameScreenAction {
    data class OnBoardObjectClick(val coord: Coord) : GameScreenAction()
    data object OnAnimationFinished : GameScreenAction()
}

package com.example.threeinarow.presentation.screens.gameScreen

sealed class GameScreenAction {
    data class OnBoardObjectClick(val x: Int, val y: Int) : GameScreenAction()
}

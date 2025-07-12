package com.example.threeinarow.presentation.navigation

sealed class Route(val route: String) {
    data object GameScreenRoute : Route("GameScreenRoute")
}

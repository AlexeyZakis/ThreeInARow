package com.example.threeinarow.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.platform.LocalConfiguration
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.threeinarow.presentation.screens.gameScreen.GameScreen
import com.example.threeinarow.presentation.screens.gameScreen.GameScreenViewModel

@Composable
fun AppNavigation() {
    val navController: NavHostController = rememberNavController()
    val isTabletMode = isTablet()

    NavHost(
        navController = navController,
        startDestination = Route.GameScreenRoute.route
    ) {
        composable(
            route = Route.GameScreenRoute.route,
        ) {
            val gameScreenViewModel: GameScreenViewModel = hiltViewModel()
            val gameScreenState by gameScreenViewModel.screenState.collectAsState()
            GameScreen(
                screenState = gameScreenState,
                screenAction = gameScreenViewModel::screenAction,
            )
        }
    }
}

@Composable
private fun isTablet(): Boolean {
    val configuration = LocalConfiguration.current
    return configuration.screenHeightDp >= 600
}

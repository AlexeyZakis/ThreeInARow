package com.example.threeinarow.presentation.screens.gameScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.threeinarow.data.manhattanDistance
import com.example.threeinarow.domain.models.Coord
import com.example.threeinarow.domain.usecase.gameBoardManager.GetGameBoardUseCase
import com.example.threeinarow.domain.usecase.gameBoardManager.OnSwapBoardObjectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val onSwapBoardObjectUseCase: OnSwapBoardObjectUseCase,
    getGameBoardUseCase: GetGameBoardUseCase,
) : ViewModel() {
    private val _screenState = MutableStateFlow(GameScreenState())
    val screenState = _screenState.asStateFlow()

    private val gameBoardUseCase = getGameBoardUseCase()

    init {
        gameBoardUseCase.onEach { gameBoard ->
            _screenState.update { screenState ->
                screenState.copy(
                    gameBoard = gameBoard,
                )
            }
        }.launchIn(viewModelScope)
    }

    fun screenAction(action: GameScreenAction) {
        when (action) {
            is GameScreenAction.OnBoardObjectClick -> boardObjectClick(action.coord)
        }
    }

    private fun boardObjectClick(coord: Coord) {
        val activeObjectPosition = _screenState.value.activeObjectPosition
        var newActiveObjectPosition: Coord? = coord
        if (activeObjectPosition?.let { otherCoord ->
                manhattanDistance(
                    coord1 = coord,
                    coord2 = otherCoord,
                ) == 1
            } == true
        ) {
            val successSwap = onSwapBoardObjectUseCase(
                coord2 = activeObjectPosition,
                coord1 = coord,
            )
            if (successSwap) {
                newActiveObjectPosition = null
            }
        }
        _screenState.update { screenState ->
            screenState.copy(
                activeObjectPosition = newActiveObjectPosition
            )
        }
    }
}

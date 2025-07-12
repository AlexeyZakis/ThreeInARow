package com.example.threeinarow.presentation.screens.gameScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.threeinarow.data.manhattanDistance
import com.example.threeinarow.domain.usecase.gameBoardManager.GetGameBoardUseCase
import com.example.threeinarow.domain.usecase.gameBoardManager.SwapBoardObjectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val swapBoardObjectUseCase: SwapBoardObjectUseCase,
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
            is GameScreenAction.OnBoardObjectClick -> boardObjectClick(action.x, action.y)
        }
    }

    private fun boardObjectClick(x: Int, y: Int) {
        if (_screenState.value.activeObjectPosition == null) {
            _screenState.update { screenState ->
                screenState.copy(
                    activeObjectPosition = Pair(x, y)
                )
            }
        } else {
            val activeObjectPosition = _screenState.value.activeObjectPosition ?: return
            if (manhattanDistance(
                    x1 = activeObjectPosition.first,
                    y1 = activeObjectPosition.second,
                    x2 = x,
                    y2 = y,
                ) == 1
            ) {
                swapBoardObjectUseCase(
                    x1 = activeObjectPosition.first,
                    y1 = activeObjectPosition.second,
                    x2 = x,
                    y2 = y,
                )
            }
            _screenState.update { screenState ->
                screenState.copy(
                    activeObjectPosition = null
                )
            }
        }
    }
}

package com.example.threeinarow.presentation.screens.gameScreen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.threeinarow.domain.models.Coord
import com.example.threeinarow.domain.usecase.gameBoardManager.GetGameBoardUseCase
import com.example.threeinarow.domain.usecase.gameBoardManager.GetSelectedObjectCoordUseCase
import com.example.threeinarow.domain.usecase.gameBoardManager.OnSelectObjectUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import javax.inject.Inject

@HiltViewModel
class GameScreenViewModel @Inject constructor(
    private val onSelectObjectUseCase: OnSelectObjectUseCase,
    getSelectObjectUseCase: GetSelectedObjectCoordUseCase,
    getGameBoardUseCase: GetGameBoardUseCase,
) : ViewModel() {
    private val _screenState = MutableStateFlow(GameScreenState())
    val screenState = _screenState.asStateFlow()

    private val gameBoardUseCase = getGameBoardUseCase()
    private val selectedObjectCoord = getSelectObjectUseCase()

    init {
        gameBoardUseCase.onEach { gameBoard ->
            _screenState.update { screenState ->
                screenState.copy(
                    gameBoard = gameBoard,
                )
            }
        }.launchIn(viewModelScope)

        selectedObjectCoord.onEach { selectedObjectCoord ->
            _screenState.update { screenState ->
                screenState.copy(
                    selectedObjectCoord = selectedObjectCoord,
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
        onSelectObjectUseCase(coord = coord)
    }
}

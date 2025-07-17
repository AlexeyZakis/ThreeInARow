package com.example.threeinarow.domain.usecase.gameBoardManager

import com.example.threeinarow.domain.managers.GameBoardManager

class GetSelectedObjectCoordUseCase(
    private val gameBoardManager: GameBoardManager,
) {
    operator fun invoke() = gameBoardManager.selectedObjectCoord
}

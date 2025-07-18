package com.example.threeinarow.domain.usecase.gameBoardManager

import com.example.threeinarow.domain.managers.GameBoardManager

class OnAnimationFinishedUseCase(
    private val gameBoardManager: GameBoardManager,
) {
    operator fun invoke() = gameBoardManager.onAnimationFinished()
}

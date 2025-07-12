package com.example.threeinarow.domain.usecase.gameBoardManager

import com.example.threeinarow.domain.managers.GameBoardManager

class SwapBoardObjectUseCase(
    private val gameBoardManager: GameBoardManager,
) {
    operator fun invoke(x1: Int, y1: Int, x2: Int, y2: Int) =
        gameBoardManager.swapObjects(
            x1 = x1,
            y1 = y1,
            x2 = x2,
            y2 = y2,
        )
}

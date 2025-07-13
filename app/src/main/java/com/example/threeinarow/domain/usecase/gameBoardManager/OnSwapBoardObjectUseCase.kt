package com.example.threeinarow.domain.usecase.gameBoardManager

import com.example.threeinarow.domain.managers.GameBoardManager
import com.example.threeinarow.domain.models.Coord

class OnSwapBoardObjectUseCase(
    private val gameBoardManager: GameBoardManager,
) {
    operator fun invoke(coord1: Coord, coord2: Coord): Boolean =
        gameBoardManager.onSwapObjects(
            coord1 = coord1,
            coord2 = coord2,
        )
}

package com.example.threeinarow.domain.usecase.gameBoardManager

import com.example.threeinarow.domain.managers.GameBoardManager
import com.example.threeinarow.domain.models.Coord

class OnSelectObjectUseCase(
    private val gameBoardManager: GameBoardManager,
) {
    operator fun invoke(coord: Coord): Boolean =
        gameBoardManager.onSelectObject(coord = coord)
}

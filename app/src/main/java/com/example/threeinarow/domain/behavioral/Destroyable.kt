package com.example.threeinarow.domain.behavioral

import com.example.threeinarow.domain.managers.GameBoardManager
import com.example.threeinarow.domain.models.Coord

interface Destroyable {
    fun onDestroy(gameBoardManager: GameBoardManager, coord: Coord)
}

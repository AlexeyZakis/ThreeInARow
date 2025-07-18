package com.example.threeinarow.domain.behavioral

import com.example.threeinarow.data.GameBoard
import com.example.threeinarow.domain.models.Coord

interface Destroyable {
    fun onDestroy(
        gameBoard: GameBoard,
        coord: Coord,
        generation: Int,
        destroyedObjectsGenerations: MutableMap<Int, MutableSet<Coord>>,
        destroyedObjectsCoords: MutableSet<Coord>,
    )
}

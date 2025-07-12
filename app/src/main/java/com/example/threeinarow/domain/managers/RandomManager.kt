package com.example.threeinarow.domain.managers

import com.example.threeinarow.domain.gameObjects.GameBoardObject

interface RandomManager {
    fun getRandomObject(): GameBoardObject
}

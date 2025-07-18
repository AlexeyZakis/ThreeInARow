package com.example.threeinarow.domain.gameObjects

import com.example.threeinarow.domain.managers.IdManager
import com.example.threeinarow.domain.models.Id

interface GameBoardObject {
    val id: Id
    fun copy(idManager: IdManager): GameBoardObject
}

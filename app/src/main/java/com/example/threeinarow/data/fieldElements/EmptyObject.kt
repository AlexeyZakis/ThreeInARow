package com.example.threeinarow.data.fieldElements

import com.example.threeinarow.domain.behavioral.Unfallable
import com.example.threeinarow.domain.gameObjects.GameBoardObject

class EmptyObject : GameBoardObject, Unfallable {
    override fun copy(): GameBoardObject {
        val new = EmptyObject()
        return new
    }
}

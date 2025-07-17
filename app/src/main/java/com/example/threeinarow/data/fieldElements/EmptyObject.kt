package com.example.threeinarow.data.fieldElements

import com.example.threeinarow.domain.behavioral.Unfallable
import com.example.threeinarow.domain.gameObjects.GameBoardObject

object EmptyObject : GameBoardObject, Unfallable {
    override fun copy(): GameBoardObject {
        return this
    }
}

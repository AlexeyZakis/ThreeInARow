package com.example.threeinarow.data.fieldElements

import com.example.threeinarow.domain.behavioral.Unfallable
import com.example.threeinarow.domain.gameObjects.GameBoardObject
import com.example.threeinarow.domain.managers.IdManager
import com.example.threeinarow.domain.models.Id

class Obstacle(
    override val id: Id,
) : GameBoardObject, Unfallable {
    override fun copy(idManager: IdManager): GameBoardObject {
        val id = idManager.getNextSessionId()
        val new = Obstacle(
            id = id,
        )
        return new
    }
}

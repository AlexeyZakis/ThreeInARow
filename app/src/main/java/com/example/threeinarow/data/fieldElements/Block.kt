package com.example.threeinarow.data.fieldElements

import com.example.threeinarow.domain.BlockTypes
import com.example.threeinarow.domain.behavioral.Connectable
import com.example.threeinarow.domain.behavioral.Destroyable
import com.example.threeinarow.domain.behavioral.Swappable
import com.example.threeinarow.domain.gameObjects.GameBoardObject
import com.example.threeinarow.domain.managers.GameBoardManager
import com.example.threeinarow.domain.models.Coord

open class Block(
    override val type: BlockTypes = BlockTypes.default,
) : GameBoardObject, Connectable, Destroyable, Swappable {
    override fun onDestroy(gameBoardManager: GameBoardManager, coord: Coord) = Unit

    override fun copy(): GameBoardObject {
        val new = Block(
            type = type
        )
        return new
    }
}

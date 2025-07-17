package com.example.threeinarow.data.fieldElements

import com.example.threeinarow.data.GameBoard
import com.example.threeinarow.domain.BlockTypes
import com.example.threeinarow.domain.behavioral.Connectable
import com.example.threeinarow.domain.behavioral.Destroyable
import com.example.threeinarow.domain.behavioral.Selectable
import com.example.threeinarow.domain.behavioral.Swappable
import com.example.threeinarow.domain.gameObjects.GameBoardObject
import com.example.threeinarow.domain.models.Coord

open class Block(
    override val type: BlockTypes = BlockTypes.default,
) : GameBoardObject, Connectable, Destroyable, Swappable, Selectable {
    override fun onDestroy(gameBoard: GameBoard, coord: Coord) = Unit

    override fun copy(): GameBoardObject {
        val new = Block(
            type = type
        )
        return new
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Block

        return type == other.type
    }

    override fun hashCode(): Int {
        return type.hashCode()
    }

}

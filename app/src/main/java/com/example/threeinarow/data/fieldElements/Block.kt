package com.example.threeinarow.data.fieldElements

import com.example.threeinarow.data.GameBoard
import com.example.threeinarow.domain.behavioral.Connectable
import com.example.threeinarow.domain.behavioral.Destroyable
import com.example.threeinarow.domain.behavioral.Selectable
import com.example.threeinarow.domain.behavioral.Swappable
import com.example.threeinarow.domain.gameObjects.GameBoardObject
import com.example.threeinarow.domain.managers.IdManager
import com.example.threeinarow.domain.models.BlockTypes
import com.example.threeinarow.domain.models.Coord
import com.example.threeinarow.domain.models.Id

open class Block(
    override val id: Id,
    override val type: BlockTypes = BlockTypes.default,
) : GameBoardObject, Connectable, Destroyable, Swappable, Selectable {
    override fun onDestroy(
        gameBoard: GameBoard,
        coord: Coord,
        generation: Int,
        destroyedObjectsGenerations: MutableMap<Int, MutableSet<Coord>>,
        destroyedObjectsCoords: MutableSet<Coord>,
    ) = Unit

    override fun copy(idManager: IdManager): GameBoardObject {
        val id = idManager.getNextSessionId()
        val new = Block(
            id = id,
            type = type,
        )
        return new
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Block

        if (id != other.id) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

    companion object {
        val exampleBlock = Block(
            id = Id(),
        )
    }
}

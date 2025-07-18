package com.example.threeinarow.data.fieldElements

import com.example.threeinarow.data.GameBoard
import com.example.threeinarow.data.explosionPattern.ExplosionPattern3x3
import com.example.threeinarow.data.explosionPattern.ExplosionPattern5x5
import com.example.threeinarow.data.explosionPattern.ExplosionPatternAll
import com.example.threeinarow.data.explosionPattern.ExplosionPatternCross
import com.example.threeinarow.data.explosionPattern.ExplosionPatternCrossX
import com.example.threeinarow.data.explosionPattern.ExplosionPatternDiagonalLeft
import com.example.threeinarow.data.explosionPattern.ExplosionPatternDiagonalRight
import com.example.threeinarow.data.explosionPattern.ExplosionPatternHorizontalLine
import com.example.threeinarow.data.explosionPattern.ExplosionPatternVerticalLine
import com.example.threeinarow.data.explosionPattern.ExplosionPatternX
import com.example.threeinarow.domain.behavioral.Exploadable
import com.example.threeinarow.domain.gameObjects.GameBoardObject
import com.example.threeinarow.domain.managers.IdManager
import com.example.threeinarow.domain.models.BlockTypes
import com.example.threeinarow.domain.models.Coord
import com.example.threeinarow.domain.models.ExplosionPatterns
import com.example.threeinarow.domain.models.Id

class BombBlock(
    override val id: Id,
    override val type: BlockTypes,
    override val explosionPattern: ExplosionPatterns
) : Block(id = id, type = type), Exploadable {
    override fun onDestroy(
        gameBoard: GameBoard,
        coord: Coord,
        generation: Int,
        destroyedObjectsGenerations: MutableMap<Int, MutableSet<Coord>>,
        destroyedObjectsCoords: MutableSet<Coord>,
    ) {
        val objectsToDestroy = when (explosionPattern) {
            ExplosionPatterns.Bomb3x3 -> ExplosionPattern3x3.apply(gameBoard, coord)
            ExplosionPatterns.Bomb5x5 -> ExplosionPattern5x5.apply(gameBoard, coord)
            ExplosionPatterns.HorizontalLine -> ExplosionPatternHorizontalLine.apply(
                gameBoard,
                coord
            )

            ExplosionPatterns.VerticalLine -> ExplosionPatternVerticalLine.apply(
                gameBoard,
                coord
            )

            ExplosionPatterns.Cross -> ExplosionPatternCross.apply(
                gameBoard,
                coord
            )

            ExplosionPatterns.DiagonalLeft -> ExplosionPatternDiagonalLeft.apply(
                gameBoard,
                coord
            )

            ExplosionPatterns.DiagonalRight -> ExplosionPatternDiagonalRight.apply(
                gameBoard,
                coord
            )

            ExplosionPatterns.X -> ExplosionPatternX.apply(
                gameBoard,
                coord
            )

            ExplosionPatterns.CrossX -> ExplosionPatternCrossX.apply(
                gameBoard,
                coord
            )

            ExplosionPatterns.All -> ExplosionPatternAll.apply(
                gameBoard,
                coord
            )
        }
        gameBoard.addNewDestroyedObjects(
            gameBoard = gameBoard,
            objectsToDestroyCoords = objectsToDestroy,
            generation = generation,
        )
    }

    override fun copy(idManager: IdManager): GameBoardObject {
        val id = idManager.getNextSessionId()
        val new = BombBlock(
            id = id,
            type = type,
            explosionPattern = explosionPattern,
        )
        return new
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as BombBlock

        if (id != other.id) return false
        if (type != other.type) return false
        if (explosionPattern != other.explosionPattern) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + id.hashCode()
        result = 31 * result + type.hashCode()
        result = 31 * result + explosionPattern.hashCode()
        return result
    }

    companion object {
        fun addBombToBlock(block: Block, explosionPattern: ExplosionPatterns): BombBlock {
            val bombBlock = BombBlock(
                id = block.id,
                type = block.type,
                explosionPattern = explosionPattern,
            )
            return bombBlock
        }
    }
}

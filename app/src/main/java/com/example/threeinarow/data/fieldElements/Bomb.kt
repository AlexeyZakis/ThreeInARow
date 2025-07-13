package com.example.threeinarow.data.fieldElements

import com.example.threeinarow.data.explosionPattern.ExplosionPattern3x3
import com.example.threeinarow.data.explosionPattern.ExplosionPattern5x5
import com.example.threeinarow.data.explosionPattern.ExplosionPatternCrossLine
import com.example.threeinarow.data.explosionPattern.ExplosionPatternHorizontalLine
import com.example.threeinarow.data.explosionPattern.ExplosionPatternVerticalLine
import com.example.threeinarow.domain.BlockTypes
import com.example.threeinarow.domain.ExplosionPatterns
import com.example.threeinarow.domain.behavioral.Exploadable
import com.example.threeinarow.domain.gameObjects.GameBoardObject
import com.example.threeinarow.domain.managers.GameBoardManager
import com.example.threeinarow.domain.models.Coord

class Bomb(
    override val type: BlockTypes,
    override val explosionPattern: ExplosionPatterns
) : Block(), Exploadable {
    override fun onDestroy(gameBoardManager: GameBoardManager, coord: Coord) {
        when (explosionPattern) {
            ExplosionPatterns.Bomb3x3 -> ExplosionPattern3x3.apply(gameBoardManager, coord)
            ExplosionPatterns.Bomb5x5 -> ExplosionPattern5x5.apply(gameBoardManager, coord)
            ExplosionPatterns.HorizontalLine -> ExplosionPatternHorizontalLine.apply(
                gameBoardManager,
                coord
            )

            ExplosionPatterns.VerticalLine -> ExplosionPatternVerticalLine.apply(
                gameBoardManager,
                coord
            )

            ExplosionPatterns.CrossLine -> ExplosionPatternCrossLine.apply(
                gameBoardManager,
                coord
            )
        }
    }

    override fun copy(): GameBoardObject {
        val new = Bomb(
            explosionPattern = explosionPattern,
            type = type,
        )
        return new
    }

    companion object {
        fun addBombToBlock(block: Block, explosionPattern: ExplosionPatterns): Bomb {
            val bomb = Bomb(
                type = block.type,
                explosionPattern = explosionPattern,
            )
            return bomb
        }
    }
}

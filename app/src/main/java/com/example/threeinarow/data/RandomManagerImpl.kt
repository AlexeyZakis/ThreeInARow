package com.example.threeinarow.data

import com.example.threeinarow.data.fieldElements.Block
import com.example.threeinarow.data.fieldElements.EmptyObject
import com.example.threeinarow.data.fieldElements.Obstacle
import com.example.threeinarow.domain.BlockTypes
import com.example.threeinarow.domain.gameObjects.GameBoardObject
import com.example.threeinarow.domain.managers.RandomManager
import kotlin.random.Random
import kotlin.random.nextUInt

class RandomManagerImpl(
    seed: Long = System.currentTimeMillis(),
): RandomManager {
    private val rng = Random(seed)

    override fun getRandomObject(): GameBoardObject {
        val objectIndex = rng.nextUInt() % gameBoardObjects.size.toUInt()
        return when (gameBoardObjects[objectIndex.toInt()]) {
            is Block -> {
                val typeIndex = rng.nextUInt() % BlockTypes.entries.size.toUInt()
                val type = BlockTypes.entries[typeIndex.toInt()]
                Block(
                    type = type,
                )
            }
            is Obstacle -> Obstacle()
            else -> EmptyObject()
        }
    }

    private val gameBoardObjects = listOf<GameBoardObject>(
//        EmptyObject(),
        Block(),
//        Obstacle(),
    )
}

package com.example.threeinarow.data

import com.example.threeinarow.data.fieldElements.Block
import com.example.threeinarow.data.fieldElements.Bomb
import com.example.threeinarow.data.fieldElements.EmptyObject
import com.example.threeinarow.data.fieldElements.Obstacle
import com.example.threeinarow.domain.BlockTypes
import com.example.threeinarow.domain.ExplosionPatterns
import com.example.threeinarow.domain.gameObjects.GameBoardObject
import com.example.threeinarow.domain.managers.RandomManager
import com.example.threeinarow.domain.models.Coord

class GameBoard(
    val width: Int = 0,
    val height: Int = 0,
) {
    private var _gameBoard: MutableList<MutableList<GameBoardObject>> = MutableList(height) { row ->
        MutableList(width) { col ->
            EmptyObject()
        }
    }
    val gameBoard: List<List<GameBoardObject>> get() = _gameBoard

    operator fun get(coord: Coord): GameBoardObject {
        return _gameBoard[coord.y][coord.x]
    }

    operator fun set(coord: Coord, gameBoardObject: GameBoardObject) {
        _gameBoard[coord.y][coord.x] = gameBoardObject
    }

    fun swapObjects(
        coord1: Coord,
        coord2: Coord,
    ) {
        val obj1 = get(coord1)
        val obj2 = get(coord2)

        set(coord1, obj2)
        set(coord2, obj1)
    }

    fun fillEmptyObjects(randomManager: RandomManager) {
        for (y in 0..<height) {
            for (x in 0..<width) {
                val curCoord = Coord(x, y)
                val obj = get(curCoord)
                if (obj is EmptyObject) {
                    val newObj = getRandomObject(randomManager)
                    set(curCoord, newObj)
                }
            }
        }
    }

    override fun toString(): String {
        val height = gameBoard.size
        val width = gameBoard.firstOrNull()?.size ?: return ""
        val stringBuilder = StringBuilder()
        for (y in 0..<height) {
            for (x in 0..<width) {
                val curCoord = Coord(x, y)
                val obj = get(curCoord)
                val char = obj.toChar()
                stringBuilder.append("$char ")
            }
            stringBuilder.append("\n")
        }
        return stringBuilder.toString()
    }

    fun copy(): GameBoard {
        val newGameBoard = GameBoard(
            width = width,
            height = height
        )
        for (y in 0..<height) {
            for (x in 0..<width) {
                val coord = Coord(x, y)
                newGameBoard[coord] = get(coord).copy()
            }
        }
        return newGameBoard
    }

    fun getColumnWithFilter(
        x: Int,
        filter: (GameBoardObject) -> Boolean = { true }
    ): List<GameBoardObject> {
        val objects = mutableListOf<GameBoardObject>()
        for (y in 0..<height) {
            val curCoord = Coord(x, y)
            val obj = get(curCoord)
            if (filter(obj)) {
                objects.add(obj)
            }
        }
        return objects
    }

    private fun addBombToBlock(
        block: Block,
        randomManager: RandomManager,
    ): Block {
        val bomb = Bomb.addBombToBlock(
            block = block,
            explosionPattern = ExplosionPatterns.getRandomPattern(randomManager)
        )
        return bomb
    }

    private fun getRandomObject(randomManager: RandomManager): GameBoardObject {
        val objectIndex = randomManager.getInt(0, gameBoardObjects.size)
        return when (gameBoardObjects[objectIndex.toInt()]) {
            is Block -> {
                val typeIndex = randomManager.getInt(0, BlockTypes.entries.size)
                val type = BlockTypes.entries[typeIndex.toInt()]
                val newBlock = Block(
                    type = type,
                )
                val blockWithEffect = if (
                    randomManager.getTrueWithProbability(Config.EXPLOSION_EFFECT_SPAWN_PROBABILITY)
                ) {
                    val bomb = addBombToBlock(
                        block = newBlock,
                        randomManager = randomManager,
                    )
                    bomb
                } else {
                    newBlock
                }
                blockWithEffect
            }

            is Obstacle -> Obstacle()
            else -> EmptyObject()
        }
    }

    private val gameBoardObjects = listOf<GameBoardObject>(
        Block(),
    )

    private fun GameBoardObject.toChar(): Char {
        return when (this) {
            is Block -> {
                when (type) {
                    BlockTypes.RED -> 'R'
                    BlockTypes.GREEN -> 'G'
                    BlockTypes.BLUE -> 'B'
                    BlockTypes.YELLOW -> 'Y'
                    BlockTypes.VIOLET -> 'V'
                }
            }

            is Obstacle -> 'O'
            else -> '-'
        }
    }
}
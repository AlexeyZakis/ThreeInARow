package com.example.threeinarow.data

import com.example.threeinarow.data.fieldElements.Block
import com.example.threeinarow.data.fieldElements.BombBlock
import com.example.threeinarow.data.fieldElements.EmptyObject
import com.example.threeinarow.data.fieldElements.Obstacle
import com.example.threeinarow.domain.BlockTypes
import com.example.threeinarow.domain.ExplosionPatterns
import com.example.threeinarow.domain.behavioral.Destroyable
import com.example.threeinarow.domain.behavioral.Swappable
import com.example.threeinarow.domain.behavioral.Unfallable
import com.example.threeinarow.domain.gameObjects.GameBoardObject
import com.example.threeinarow.domain.managers.RandomManager
import com.example.threeinarow.domain.models.Coord
import kotlin.math.roundToInt

class GameBoard(
    val width: Int = 0,
    val height: Int = 0,
    val explosionEffectSpawnProbability: Int = 0,
    val explosionPatternsToProbabilityWeight: (ExplosionPatterns) -> Int = { 0 },
    val randomManager: RandomManager = RandomManagerImpl(),
) {
    private var _gameBoard: MutableList<MutableList<GameBoardObject>> = MutableList(height) { row ->
        MutableList(width) { col ->
            EmptyObject
        }
    }
    val gameBoard: List<List<GameBoardObject>> get() = _gameBoard

    private val objectsCoordsToDestroy = mutableSetOf<Coord>()

    operator fun get(coord: Coord): GameBoardObject {
        return _gameBoard[coord.y][coord.x]
    }

    operator fun set(coord: Coord, gameBoardObject: GameBoardObject) {
        _gameBoard[coord.y][coord.x] = gameBoardObject
    }

    fun swapObjects(
        coord1: Coord,
        coord2: Coord,
        checkConnections: Boolean = true,
    ): Boolean {
        val obj1 = get(coord1)
        val obj2 = get(coord2)

        if (obj1 !is Swappable || obj2 !is Swappable) return false

        val newGameBoard = copy()
        newGameBoard[coord1] = obj2
        newGameBoard[coord2] = obj1

        if (checkConnections && !hasConnections(newGameBoard)) return false

        setGameBoard(newGameBoard)
        return true
    }

    fun fillEmptyObjects() {
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
            height = height,
            explosionEffectSpawnProbability = explosionEffectSpawnProbability,
            explosionPatternsToProbabilityWeight = explosionPatternsToProbabilityWeight,
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

    private fun blockToBombBlock(
        block: Block,
        randomManager: RandomManager,
        explosionPattern: ExplosionPatterns? = null,
    ): Block {
        val explosionPattern = explosionPattern
            ?: getRandomPattern(randomManager)
            ?: return block
        val bombBlock = BombBlock.addBombToBlock(
            block = block,
            explosionPattern = explosionPattern
        )
        return bombBlock
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
                    randomManager.getTrueWithProbability(explosionEffectSpawnProbability)
                ) {
                    val bomb = blockToBombBlock(
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
            else -> EmptyObject
        }
    }

    private fun getRandomPattern(randomManager: RandomManager): ExplosionPatterns? {
        val randomValue = randomManager.getInt(1, 101)
        val probabilities = getProbabilities() ?: return null
        val cumulative = probabilities.runningReduce { acc, value -> acc + value }

        var index = 0
        while (index < cumulative.size) {
            if (cumulative[index] >= randomValue) break
            ++index
        }
        val patternIndex = index.coerceIn(0, cumulative.size - 1)
        val pattern = ExplosionPatterns.entries[patternIndex]
        return pattern
    }

    private fun getProbabilities(): List<Int>? {
        val probabilityWeights = ExplosionPatterns.entries.map {
            explosionPatternsToProbabilityWeight(it)
        }
        val sum = probabilityWeights.sum()
        if (sum == 0) return null
        val normalizedProbabilityWeights = probabilityWeights.map { it.toFloat() / sum }
        val result = normalizedProbabilityWeights.map { (it * 100).roundToInt() }
        return result
    }

    fun addCoordToDestroySet(coord: Coord) {
        if (isNotValidPosition(coord)) return
        val obj = get(coord)
        if (obj !is Destroyable) return
        if (objectsCoordsToDestroy.contains(coord)) return
        objectsCoordsToDestroy.add(coord)
        obj.onDestroy(this, coord)
    }

    fun onBoardChange() {
        var newGameBoard = copy()
        while (true) {
            val connectedObjectsCoords = findConnections(
                gameBoard = newGameBoard,
            )
            if (connectedObjectsCoords.isEmpty()) break
            addObjectsCoordsToDestroySet(
                gameBoard = newGameBoard,
                objectsToDestroyCoords = connectedObjectsCoords,
            )
            newGameBoard = getBoardWithDestroyedObjects(
                gameBoard = newGameBoard,
            )
            newGameBoard = getBoardWithMovedObjectsDown(
                gameBoard = newGameBoard,
            )
            newGameBoard = getBoardWithSpawnedObjects(
                gameBoard = newGameBoard,
            )
        }
        setGameBoard(newGameBoard)
    }

    fun spawnObjects() {
        val newGameBoard = getBoardWithSpawnedObjects(this)
        setGameBoard(newGameBoard)
    }

    fun spawnObjectAt(
        gameBoardObject: GameBoardObject,
        coord: Coord,
    ) {
        set(coord, gameBoardObject)
    }

    fun destroyObjects() {
        val newGameBoard = getBoardWithDestroyedObjects(this)
        setGameBoard(newGameBoard)
    }

    fun addBombToObjectAt(
        coord: Coord,
        explosionPattern: ExplosionPatterns
    ) {
        val obj = get(coord)

        if (obj !is Block) return

        val bombBlock = BombBlock.addBombToBlock(obj, explosionPattern)
        set(coord, bombBlock)
    }

    fun getRandomCoord(): Coord {
        val x = randomManager.getInt(0, width)
        val y = randomManager.getInt(0, height)
        val coord = Coord(x, y)
        return coord
    }

    private fun hasConnections(gameBoard: GameBoard): Boolean {
        val objectsCordsToDestroy = findConnections(
            gameBoard = gameBoard,
        )
        return objectsCordsToDestroy.isNotEmpty()
    }

    private fun getBoardWithSpawnedObjects(gameBoard: GameBoard): GameBoard {
        val newGameBoard = gameBoard.copy()
        newGameBoard.fillEmptyObjects()
        return newGameBoard
    }

    private fun getBoardWithDestroyedObjects(
        gameBoard: GameBoard,
    ): GameBoard {
        val newGameBoard = gameBoard.copy()
        for (coord in objectsCoordsToDestroy) {
            newGameBoard[coord] = EmptyObject
        }
        objectsCoordsToDestroy.clear()
        return newGameBoard
    }

    private fun getBoardWithMovedObjectsDown(
        gameBoard: GameBoard,
    ): GameBoard {
        val newGameBoard = getEmptyBoard()
        for (x in 0..<width) {
            val fallableObjects = gameBoard.getColumnWithFilter(
                x = x,
                filter = { it !is Unfallable },
            ).toMutableList()
            for (y in (0..<height).reversed()) {
                val curCoord = Coord(x, y)
                val obj = gameBoard[curCoord]
                newGameBoard[curCoord] = if (obj is Unfallable && obj !is EmptyObject) {
                    obj
                } else {
                    if (fallableObjects.isNotEmpty()) {
                        val lowestObj = fallableObjects.last()
                        fallableObjects.removeLastOrNull()
                        lowestObj
                    } else {
                        EmptyObject
                    }
                }
            }
        }
        return newGameBoard
    }

    private fun addObjectsCoordsToDestroySet(
        gameBoard: GameBoard,
        objectsToDestroyCoords: Set<Coord>
    ) {
        for (y in 0..<height) {
            for (x in 0..<width) {
                val curCoord = Coord(x, y)
                val obj = gameBoard[curCoord]
                if (
                    objectsToDestroyCoords.contains(curCoord)
                    && obj is Destroyable
                ) {
                    if (objectsCoordsToDestroy.contains(curCoord)) continue
                    objectsCoordsToDestroy.add(curCoord)
                    obj.onDestroy(this, curCoord)
                }
            }
        }
    }

    private fun isNotValidPosition(coord: Coord): Boolean {
        return coord.x !in (0..<width) || coord.y !in (0..<height)
    }

    private fun getEmptyBoard(): GameBoard {
        return GameBoard(
            width = width,
            height = height,
            explosionEffectSpawnProbability = explosionEffectSpawnProbability,
            explosionPatternsToProbabilityWeight = explosionPatternsToProbabilityWeight,
        )
    }

    private fun setGameBoard(gameBoard: GameBoard) {
        _gameBoard = gameBoard.copy().gameBoard as MutableList<MutableList<GameBoardObject>>
    }

    private fun findConnections(gameBoard: GameBoard): Set<Coord> {
        return ConnectionFinder.findConnections(
            width = width,
            height = height,
            gameBoard = gameBoard,
        )
    }

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

    private val gameBoardObjects = listOf<GameBoardObject>(
        Block(),
    )
}

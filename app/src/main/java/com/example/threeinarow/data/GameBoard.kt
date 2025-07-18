package com.example.threeinarow.data

import com.example.threeinarow.data.fieldElements.Block
import com.example.threeinarow.data.fieldElements.BombBlock
import com.example.threeinarow.data.fieldElements.EmptyObject
import com.example.threeinarow.data.fieldElements.Obstacle
import com.example.threeinarow.domain.behavioral.Destroyable
import com.example.threeinarow.domain.behavioral.Swappable
import com.example.threeinarow.domain.behavioral.Unfallable
import com.example.threeinarow.domain.gameObjects.GameBoardObject
import com.example.threeinarow.domain.managers.IdManager
import com.example.threeinarow.domain.managers.RandomManager
import com.example.threeinarow.domain.models.BlockTypes
import com.example.threeinarow.domain.models.Coord
import com.example.threeinarow.domain.models.ExplosionPatterns
import kotlin.math.roundToInt

class GameBoard(
    val width: Int = 0,
    val height: Int = 0,
    val explosionEffectSpawnProbability: Int = 0,
    val explosionPatternsToProbabilityWeight: (ExplosionPatterns) -> Int = { 0 },
    val randomManager: RandomManager = RandomManagerImpl(),
    val idManager: IdManager = IdManagerImpl(),
    private val destroyedObjectsGenerations: MutableMap<Int, MutableSet<Coord>> = mutableMapOf<Int, MutableSet<Coord>>(),
    private val destroyedObjectsCoords: MutableSet<Coord> = mutableSetOf<Coord>(),
) {
    private var _gameBoard: MutableList<MutableList<GameBoardObject>> = MutableList(height) { row ->
        MutableList(width) { col ->
            val id = idManager.getNextSessionId()
            EmptyObject(id)
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
            destroyedObjectsGenerations = destroyedObjectsGenerations,
            destroyedObjectsCoords = destroyedObjectsCoords,
            randomManager = randomManager,
            idManager = idManager,
        )
        for (y in 0..<height) {
            for (x in 0..<width) {
                val coord = Coord(x, y)
                newGameBoard[coord] = get(coord).copy(idManager)
            }
        }
        return newGameBoard
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
        val id = idManager.getNextSessionId()
        return when (gameBoardObjects[objectIndex.toInt()]) {
            is Block -> {
                val typeIndex = randomManager.getInt(0, BlockTypes.entries.size)
                val type = BlockTypes.entries[typeIndex.toInt()]
                val newBlock = Block(
                    id = id,
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

            is Obstacle -> Obstacle(
                id = id,
            )

            else -> EmptyObject(
                id = id,
            )
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
        if (destroyedObjectsCoords.contains(coord)) return
        destroyedObjectsCoords.add(coord)
        obj.onDestroy(
            gameBoard = this,
            coord = coord,
            generation = 0,
            destroyedObjectsGenerations = destroyedObjectsGenerations,
            destroyedObjectsCoords = destroyedObjectsCoords,
        )
    }

    suspend fun onBoardChange(
        onAnimateDestroy: suspend (Set<Coord>) -> Unit,
        onAnimateFall: suspend (Map<Coord, Int>) -> Unit,
        onAnimateSpawn: suspend (Map<Coord, Int>, GameBoard) -> Unit,
    ) {
        var currentBoard = copy()

        while (true) {
            val connectedCoords = findConnections(currentBoard)
            if (connectedCoords.isEmpty()) break
            currentBoard = handleDestructionPhase(
                boardBeforeDestroy = currentBoard,
                connectedObjectCoords = connectedCoords,
                onAnimateDestroy = onAnimateDestroy,
            )
            currentBoard = handleFallPhase(
                boardBeforeFall = currentBoard,
                onAnimateFall = onAnimateFall,
            )
            currentBoard = handleSpawnPhase(
                boardBeforeSpawn = currentBoard,
                onAnimateSpawn = onAnimateSpawn,
            )
        }
    }

    private suspend fun handleDestructionPhase(
        boardBeforeDestroy: GameBoard,
        connectedObjectCoords: Set<Coord>,
        onAnimateDestroy: suspend (Set<Coord>) -> Unit,
    ): GameBoard {
        val firstGeneration = 0

        addNewDestroyedObjects(
            gameBoard = boardBeforeDestroy,
            generation = firstGeneration,
            objectsToDestroyCoords = connectedObjectCoords
        )

        var newBoard = boardBeforeDestroy.copy()
        val generations = destroyedObjectsGenerations.keys.sorted()
        for (generation in generations) {
            val coords = destroyedObjectsGenerations[generation] ?: continue
            if (coords.isEmpty()) continue
            onAnimateDestroy(coords)
            newBoard = getBoardWithDestroyedObjects(newBoard, coords)
            setGameBoard(newBoard)
        }
        destroyedObjectsGenerations.clear()
        destroyedObjectsCoords.clear()
        return newBoard
    }

    private suspend fun handleFallPhase(
        boardBeforeFall: GameBoard,
        onAnimateFall: suspend (Map<Coord, Int>) -> Unit,
    ): GameBoard {
        val newBoard = getBoardWithMovedObjectsDown(boardBeforeFall)
        val fallDistances = calculateFallDistances(
            oldBoard = boardBeforeFall,
            newBoard = newBoard
        )
        setGameBoard(newBoard)
        onAnimateFall(fallDistances)
        return newBoard
    }

    private suspend fun handleSpawnPhase(
        boardBeforeSpawn: GameBoard,
        onAnimateSpawn: suspend (Map<Coord, Int>, GameBoard) -> Unit,
    ): GameBoard {
        val newBoard = getBoardWithSpawnedObjects(boardBeforeSpawn)

        val spawnMap = buildMap<Coord, Int> {
            for (y in 0 until height) {
                for (x in 0 until width) {
                    val coord = Coord(x, y)
                    val oldObj = boardBeforeSpawn[coord]
                    val newObj = newBoard[coord]
                    if (oldObj is EmptyObject && newObj !is EmptyObject) {
                        put(coord, y + height)
                    }
                }
            }
        }

        onAnimateSpawn(spawnMap, newBoard)
        setGameBoard(newBoard)
        return newBoard
    }

    private fun calculateFallDistances(
        oldBoard: GameBoard,
        newBoard: GameBoard
    ): Map<Coord, Int> {
        val fallDistances = mutableMapOf<Coord, Int>()

        for (x in 0 until oldBoard.width) {
            for (newY in newBoard.height - 1 downTo 0) {
                val coord = Coord(x, newY)
                val newObj = newBoard[coord]

                var oldY = newY
                for (searchY in (newY - 1) downTo 0) {
                    val oldObj = oldBoard[Coord(x, searchY)]
                    if (oldObj == newObj) {
                        oldY = searchY
                        break
                    }
                }
                val fallDistance = newY - oldY
                if (fallDistance > 0) {
                    fallDistances[coord] = fallDistance
                }
            }
        }
        return fallDistances
    }

    fun spawnObjects() {
        val newGameBoard = getBoardWithSpawnedObjects(this)
        setGameBoard(newGameBoard)
    }

    fun destroyObjects() {
        val newBoard = getBoardWithDestroyedObjects(this, destroyedObjectsCoords)
        setGameBoard(newBoard)
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
        coords: Set<Coord>
    ): GameBoard {
        val newGameBoard = gameBoard.copy()
        for (coord in coords) {
            val id = idManager.getNextSessionId()
            newGameBoard[coord] = EmptyObject(
                id = id,
            )
        }
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
                        val id = idManager.getNextSessionId()
                        EmptyObject(
                            id = id,
                        )
                    }
                }
            }
        }
        return newGameBoard
    }

    fun addNewDestroyedObjects(
        gameBoard: GameBoard,
        objectsToDestroyCoords: Set<Coord>,
        generation: Int,
    ) {
        val newObjectsToDestroy = objectsToDestroyCoords - destroyedObjectsCoords
        val destroyableObjects = newObjectsToDestroy.filter { gameBoard[it] is Destroyable }
        destroyedObjectsGenerations.getOrPut(generation) {
            mutableSetOf()
        }.addAll(destroyableObjects)
        destroyedObjectsCoords.addAll(destroyableObjects)
        for (coord in destroyableObjects) {
            val destroyedObject = gameBoard[coord] as Destroyable
            destroyedObject.onDestroy(
                gameBoard = gameBoard,
                coord = coord,
                generation = generation + 1,
                destroyedObjectsGenerations = destroyedObjectsGenerations,
                destroyedObjectsCoords = destroyedObjectsCoords,
            )
        }
    }

    fun isNotValidPosition(coord: Coord): Boolean {
        return coord.x !in (0..<width) || coord.y !in (0..<height)
    }

    private fun getEmptyBoard(): GameBoard {
        return GameBoard(
            width = width,
            height = height,
            explosionEffectSpawnProbability = explosionEffectSpawnProbability,
            explosionPatternsToProbabilityWeight = explosionPatternsToProbabilityWeight,
            destroyedObjectsGenerations = destroyedObjectsGenerations,
            destroyedObjectsCoords = destroyedObjectsCoords,
            randomManager = randomManager,
            idManager = idManager,
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

    private fun getColumnWithFilter(
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
        Block.exampleBlock,
    )
}

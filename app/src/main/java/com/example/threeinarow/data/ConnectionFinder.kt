package com.example.threeinarow.data

import com.example.threeinarow.domain.behavioral.Connectable
import com.example.threeinarow.domain.gameObjects.GameBoardObject
import com.example.threeinarow.domain.models.BlockTypes
import com.example.threeinarow.domain.models.Coord

object ConnectionFinder {
    fun findConnections(
        width: Int,
        height: Int,
        gameBoard: GameBoard,
    ): Set<Coord> {
        val vertical = findVerticalConnections(width, height, gameBoard)
        val horizontal = findHorizontalConnections(width, height, gameBoard)
        return vertical union horizontal
    }

    private fun findVerticalConnections(
        width: Int,
        height: Int,
        gameBoard: GameBoard,
    ): Set<Coord> {
        return findConnectionsByDirection(
            lineCount = width,
            getObject = { coord -> gameBoard[coord] },
            generateLine = { x -> (0 until height).map { y -> Coord(x, y) } }
        )
    }

    private fun findHorizontalConnections(
        width: Int,
        height: Int,
        gameBoard: GameBoard,
    ): Set<Coord> {
        return findConnectionsByDirection(
            lineCount = height,
            getObject = { coord -> gameBoard[coord] },
            generateLine = { y -> (0 until width).map { x -> Coord(x, y) } }
        )
    }

    private fun findConnectionsByDirection(
        lineCount: Int,
        getObject: (coord: Coord) -> GameBoardObject,
        generateLine: (index: Int) -> List<Coord>
    ): Set<Coord> {
        val result = mutableSetOf<Coord>()

        for (lineIndex in 0 until lineCount) {
            val line = generateLine(lineIndex)

            var currentType = BlockTypes.default
            var connectionStart = 0
            var count = 0

            for (i in line.indices) {
                val coord = line[i]
                val obj = getObject(coord)

                if (obj !is Connectable) {
                    if (count >= Config.MIN_OBJECTS_TO_CONNECTION) {
                        result += line.subList(connectionStart, i)
                    }
                    count = 0
                    currentType = BlockTypes.default
                    continue
                }

                val type = obj.type
                if (type == currentType) {
                    count++
                } else {
                    if (count >= Config.MIN_OBJECTS_TO_CONNECTION) {
                        result += line.subList(connectionStart, i)
                    }
                    currentType = type
                    connectionStart = i
                    count = 1
                }
            }

            if (count >= Config.MIN_OBJECTS_TO_CONNECTION) {
                result += line.subList(connectionStart, line.size)
            }
        }

        return result
    }
}

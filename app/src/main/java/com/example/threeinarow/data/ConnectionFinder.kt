package com.example.threeinarow.data

import com.example.threeinarow.domain.BlockTypes
import com.example.threeinarow.domain.behavioral.Connectable
import com.example.threeinarow.domain.gameObjects.GameBoardObject

object ConnectionFinder {
    private const val MIN_OBJECTS_TO_CONNECTION = 3

    fun findConnections(
        width: Int,
        height: Int,
        gameBoard: List<List<GameBoardObject>>,
    ): Set<Pair<Int, Int>> {
        val verticalConnections = findVerticalConnections(width, height, gameBoard)
        val horizontalConnections = findHorizontalConnections(width, height, gameBoard)
        return verticalConnections union horizontalConnections
    }

    private fun findVerticalConnections(
        width: Int,
        height: Int,
        board: List<List<GameBoardObject>>,
    ): Set<Pair<Int, Int>> {
        return findConnectionsByDirection(
            width = width,
            height = height,
            getObject = { x, y -> board[y][x] },
            generateLine = { x -> (0 until height).map { y -> x to y } }
        )
    }

    private fun findHorizontalConnections(
        width: Int,
        height: Int,
        board: List<List<GameBoardObject>>,
    ): Set<Pair<Int, Int>> {
        return findConnectionsByDirection(
            width = width,
            height = height,
            getObject = { x, y -> board[y][x] },
            generateLine = { y -> (0 until width).map { x -> x to y } }
        )
    }

    private fun findConnectionsByDirection(
        width: Int,
        height: Int,
        getObject: (x: Int, y: Int) -> GameBoardObject,
        generateLine: (index: Int) -> List<Pair<Int, Int>>
    ): Set<Pair<Int, Int>> {
        val result = mutableSetOf<Pair<Int, Int>>()

        val lineCount = if (
            generateLine(0).first().first != generateLine(0).last().first
        ) height else width

        for (lineIndex in 0 until lineCount) {
            val line = generateLine(lineIndex)

            var currentType = BlockTypes.default
            var connectionStart = 0
            var count = 0

            for (i in line.indices) {
                val (x, y) = line[i]
                val obj = getObject(x, y)
                if (obj !is Connectable) {
                    if (count >= MIN_OBJECTS_TO_CONNECTION) {
                        result += line.subList(connectionStart, i)
                    }
                    count = 0
                    currentType = BlockTypes.default
                    continue
                }

                val type = obj.type
                if (type == currentType) {
                    ++count
                } else {
                    if (count >= MIN_OBJECTS_TO_CONNECTION) {
                        result += line.subList(connectionStart, i)
                    }
                    currentType = type
                    connectionStart = i
                    count = 1
                }
            }
            if (count >= MIN_OBJECTS_TO_CONNECTION) {
                result += line.subList(connectionStart, line.size)
            }
        }
        return result
    }
}

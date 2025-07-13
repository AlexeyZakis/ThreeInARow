package com.example.threeinarow.data

import com.example.threeinarow.domain.models.Coord
import kotlin.math.abs

fun manhattanDistance(coord1: Coord, coord2: Coord): Int {
    return abs(coord1.x - coord2.x) + abs(coord1.y - coord2.y)
}

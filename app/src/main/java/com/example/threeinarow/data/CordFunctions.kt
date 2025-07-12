package com.example.threeinarow.data

import kotlin.math.abs

fun manhattanDistance(x1: Int, y1: Int, x2: Int, y2: Int): Int {
    return abs(x1 - x2) + abs(y1 - y2)
}

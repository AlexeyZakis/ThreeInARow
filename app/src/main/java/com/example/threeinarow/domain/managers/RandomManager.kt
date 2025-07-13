package com.example.threeinarow.domain.managers

interface RandomManager {
    fun getInt(min: Int = Int.MIN_VALUE, max: Int = Int.MAX_VALUE): Int
    fun getTrueWithProbability(probability: Int): Boolean
}

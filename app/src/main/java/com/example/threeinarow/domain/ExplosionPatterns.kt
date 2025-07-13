package com.example.threeinarow.domain

import com.example.threeinarow.data.Config
import com.example.threeinarow.domain.managers.RandomManager
import kotlin.math.roundToInt

enum class ExplosionPatterns {
    Bomb3x3,
    Bomb5x5,
    HorizontalLine,
    VerticalLine,
    CrossLine,
    ;

    companion object {
        val default = Bomb3x3

        fun getRandomPattern(randomManager: RandomManager): ExplosionPatterns {
            val randomValue = randomManager.getInt(0, 100)
            val probabilities = getProbabilities()
            val cumulative = probabilities.runningReduce { acc, value -> acc + value }
            var index = 0
            while (index < cumulative.size) {
                if (cumulative[index] >= randomValue) break
                ++index
            }
            return ExplosionPatterns.entries[index.coerceIn(0, cumulative.size - 1)]
        }

        private fun getProbabilities(): List<Int> {
            val probabilityWeights = ExplosionPatterns.entries.map {
                Config.explosionPatternsToProbabilityWeight(it)
            }
            val sum = probabilityWeights.sum()
            val normalizedProbabilityWeights = probabilityWeights.map { it.toFloat() / sum }
            val result = normalizedProbabilityWeights.map { (it * 100).roundToInt() }
            return result
        }
    }
}

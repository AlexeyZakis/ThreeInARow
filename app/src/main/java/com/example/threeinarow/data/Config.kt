package com.example.threeinarow.data

import com.example.threeinarow.domain.ExplosionPatterns

object Config {
    const val SEED: Long = 1428

    const val BOARD_WIDTH = 6
    const val BOARD_HEIGHT = 10

    const val BLOCK_BORDER_RELATIVE_WIDTH = 0.07f
    const val BLOCK_CORNER_CUT_RELATIVE_WIDTH = 0.1f
    const val BLOCK_BORDER_BRIGHTNESS = 0.7f
    const val BLOCK_ICON_RELATIVE_SIZE = 0.7f
    const val SPACE_BETWEEN_BLOCK_RELATIVE = 0.09f

    const val EXPLOSION_EFFECT_SPAWN_PROBABILITY = 4

    fun explosionPatternsToProbabilityWeight(explosionPattern: ExplosionPatterns): Int {
        return when (explosionPattern) {
            ExplosionPatterns.Bomb3x3 -> 100
            ExplosionPatterns.Bomb5x5 -> 50
            ExplosionPatterns.HorizontalLine -> 20
            ExplosionPatterns.VerticalLine -> 20
            ExplosionPatterns.CrossLine -> 10
        }
    }
}

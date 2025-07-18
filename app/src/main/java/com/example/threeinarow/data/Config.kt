package com.example.threeinarow.data

import com.example.threeinarow.domain.models.ExplosionPatterns

object Config {
    const val USE_SEED: Boolean = false
    const val SEED: Long = 1428

    const val BOARD_WIDTH = 6
    const val BOARD_HEIGHT = 10

    const val BLOCK_BORDER_RELATIVE_WIDTH = 0.07f
    const val BLOCK_CORNER_CUT_RELATIVE_WIDTH = 0.1f
    const val BLOCK_BORDER_BRIGHTNESS = 0.7f
    const val BLOCK_ICON_RELATIVE_SIZE = 0.7f
    const val SPACE_BETWEEN_BLOCK_RELATIVE = 0.09f

    const val EXPLOSION_EFFECT_SPAWN_PROBABILITY = 4

    const val ANIMATION_DURATION_MS = 300

    const val MIN_OBJECTS_TO_CONNECTION = 3

    fun explosionPatternsToProbabilityWeight(explosionPattern: ExplosionPatterns): Int {
        return when (explosionPattern) {
            ExplosionPatterns.Bomb3x3 -> 60
            ExplosionPatterns.Bomb5x5 -> 40
            ExplosionPatterns.HorizontalLine -> 30
            ExplosionPatterns.VerticalLine -> 30
            ExplosionPatterns.DiagonalLeft -> 30
            ExplosionPatterns.DiagonalRight -> 30
            ExplosionPatterns.Cross -> 20
            ExplosionPatterns.X -> 20
            ExplosionPatterns.CrossX -> 10
            ExplosionPatterns.All -> 2
        }
    }
}

package com.example.threeinarow.data

import com.example.threeinarow.domain.managers.RandomManager
import kotlin.random.Random

class RandomManagerImpl(
    seed: Long = System.currentTimeMillis(),
) : RandomManager {
    private val rng = Random(seed)

    override fun getInt(from: Int, untilExclusive: Int): Int {
        val value = rng.nextInt(from, untilExclusive)
        return value
    }

    override fun getTrueWithProbability(probability: Int): Boolean {
        val isSuccess = rng.nextInt(1, 100) <= probability
        return isSuccess
    }
}

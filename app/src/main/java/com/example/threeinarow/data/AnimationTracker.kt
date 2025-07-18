package com.example.threeinarow.data

class AnimationTracker(
    private val total: Int,
    private val onAllFinished: () -> Unit
) {
    private var finished = 0

    init {
        if (total == finished) {
            onAllFinished()
        }
    }

    @Synchronized
    fun notifyFinished() {
        finished++
        if (finished == total) {
            onAllFinished()
        }
    }
}

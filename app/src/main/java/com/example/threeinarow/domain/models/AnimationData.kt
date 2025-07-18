package com.example.threeinarow.domain.models

sealed class AnimationData {
    data object Destroy : AnimationData()
    data class Fall(val fallFromHeight: Int) : AnimationData()
    data class Spawn(val spawnFromHeight: Int) : AnimationData()
    data object Nothing : AnimationData()
}
package com.example.threeinarow.domain.models

sealed class AnimationEvent {
    data class Destroy(val coords: Set<Coord>) : AnimationEvent()
    data class Fall(val fallDistances: Map<Coord, Int>) : AnimationEvent()
    data class Spawn(val spawnDistances: Map<Coord, Int>) : AnimationEvent()
}

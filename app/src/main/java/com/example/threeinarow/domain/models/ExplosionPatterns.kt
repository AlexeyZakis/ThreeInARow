package com.example.threeinarow.domain.models

enum class ExplosionPatterns {
    Bomb3x3,
    Bomb5x5,
    HorizontalLine,
    VerticalLine,
    Cross,
    DiagonalLeft,
    DiagonalRight,
    X,
    CrossX,
    All,
    ;

    companion object {
        val default = Bomb3x3
    }
}

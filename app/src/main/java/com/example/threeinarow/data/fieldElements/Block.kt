package com.example.threeinarow.data.fieldElements

import com.example.threeinarow.domain.BlockTypes
import com.example.threeinarow.domain.behavioral.Connectable
import com.example.threeinarow.domain.behavioral.Destroyable
import com.example.threeinarow.domain.behavioral.Swappable
import com.example.threeinarow.domain.gameObjects.GameBoardObject

data class Block(
    override val type: BlockTypes = BlockTypes.default,
) : GameBoardObject, Connectable, Destroyable, Swappable {
    override fun onDestroy() = Unit
    override fun onSwap() = Unit
}

package com.example.threeinarow.data

import com.example.threeinarow.domain.managers.IdManager
import com.example.threeinarow.domain.models.Id
import java.util.concurrent.atomic.AtomicLong

class IdManagerImpl : IdManager {
    private val idCounter = AtomicLong()

    override fun getNextSessionId(): Id {
        val nextCounter = idCounter.getAndIncrement()
        val nextId = Id(nextCounter)
        return nextId
    }

}

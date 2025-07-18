package com.example.threeinarow.domain.managers

import com.example.threeinarow.domain.models.Id

interface IdManager {
    fun getNextSessionId(): Id
}

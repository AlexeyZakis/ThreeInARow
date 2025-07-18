package com.example.threeinarow.presentation.di.modules

import com.example.threeinarow.data.Config
import com.example.threeinarow.data.GameBoardManagerImpl
import com.example.threeinarow.data.IdManagerImpl
import com.example.threeinarow.data.RandomManagerImpl
import com.example.threeinarow.domain.managers.GameBoardManager
import com.example.threeinarow.domain.managers.IdManager
import com.example.threeinarow.domain.managers.RandomManager
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class ManagersModule {
    @Provides
    @Singleton
    fun provideRandomManager(): RandomManager =
        RandomManagerImpl(
            seed = if (Config.USE_SEED) {
                Config.SEED
            } else {
                System.currentTimeMillis()
            },
        )

    @Provides
    @Singleton
    fun provideIdManager(): IdManager =
        IdManagerImpl()

    @Provides
    @Singleton
    fun provideUpgradesManager(
        randomManager: RandomManager,
        idManager: IdManager,
    ): GameBoardManager =
        GameBoardManagerImpl(
            width = Config.BOARD_WIDTH,
            height = Config.BOARD_HEIGHT,
            randomManager = randomManager,
            idManager = idManager,
        )
}

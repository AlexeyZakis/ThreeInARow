package com.example.threeinarow.presentation.di.modules

import com.example.threeinarow.data.Config
import com.example.threeinarow.data.GameBoardManagerImpl
import com.example.threeinarow.data.RandomManagerImpl
import com.example.threeinarow.domain.managers.GameBoardManager
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
            seed = Config.SEED,
        )

    @Provides
    @Singleton
    fun provideUpgradesManager(
        randomManager: RandomManager,
    ): GameBoardManager =
        GameBoardManagerImpl(
            width = Config.BOARD_WIDTH,
            height = Config.BOARD_HEIGHT,
            randomManager = randomManager,
        )
}

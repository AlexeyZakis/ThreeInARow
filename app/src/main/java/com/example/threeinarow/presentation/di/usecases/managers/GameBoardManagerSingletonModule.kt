package com.example.threeinarow.presentation.di.usecases.managers

import com.example.threeinarow.domain.managers.GameBoardManager
import com.example.threeinarow.domain.usecase.gameBoardManager.GetGameBoardUseCase
import com.example.threeinarow.domain.usecase.gameBoardManager.GetSelectedObjectCoordUseCase
import com.example.threeinarow.domain.usecase.gameBoardManager.OnSelectObjectUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
class GameBoardManagerSingletonModule {
    @Provides
    fun provideSwapBoardObjectUseCase(gameBoardManager: GameBoardManager) =
        OnSelectObjectUseCase(gameBoardManager = gameBoardManager)

    @Provides
    fun provideGetGameBoardUseCase(gameBoardManager: GameBoardManager) =
        GetGameBoardUseCase(gameBoardManager = gameBoardManager)

    @Provides
    fun provideGetSelectedObjectCoordUseCase(gameBoardManager: GameBoardManager) =
        GetSelectedObjectCoordUseCase(gameBoardManager = gameBoardManager)
}

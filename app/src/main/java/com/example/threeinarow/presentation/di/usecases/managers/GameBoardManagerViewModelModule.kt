package com.example.threeinarow.presentation.di.usecases.managers

import com.example.threeinarow.domain.managers.GameBoardManager
import com.example.threeinarow.domain.usecase.gameBoardManager.GameBoardManagerReleaseUseCase
import com.example.threeinarow.domain.usecase.gameBoardManager.GetAnimationEventUseCase
import com.example.threeinarow.domain.usecase.gameBoardManager.GetGameBoardUseCase
import com.example.threeinarow.domain.usecase.gameBoardManager.GetSelectedObjectCoordUseCase
import com.example.threeinarow.domain.usecase.gameBoardManager.OnAnimationFinishedUseCase
import com.example.threeinarow.domain.usecase.gameBoardManager.OnSelectObjectUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
class GameBoardManagerViewModelModule {
    @Provides
    fun provideSwapBoardObjectUseCase(gameBoardManager: GameBoardManager) =
        OnSelectObjectUseCase(gameBoardManager = gameBoardManager)

    @Provides
    fun provideOnAnimationFinishedUseCase(gameBoardManager: GameBoardManager) =
        OnAnimationFinishedUseCase(gameBoardManager = gameBoardManager)

    @Provides
    fun provideGetGameBoardUseCase(gameBoardManager: GameBoardManager) =
        GetGameBoardUseCase(gameBoardManager = gameBoardManager)

    @Provides
    fun provideGetSelectedObjectCoordUseCase(gameBoardManager: GameBoardManager) =
        GetSelectedObjectCoordUseCase(gameBoardManager = gameBoardManager)

    @Provides
    fun provideGetAnimationEventUseCase(gameBoardManager: GameBoardManager) =
        GetAnimationEventUseCase(gameBoardManager = gameBoardManager)

    @Provides
    fun provideGameBoardManagerReleaseUseCase(gameBoardManager: GameBoardManager) =
        GameBoardManagerReleaseUseCase(gameBoardManager = gameBoardManager)
}

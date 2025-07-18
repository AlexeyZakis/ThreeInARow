package com.example.threeinarow.presentation.screens.gameScreen.components.board.animation

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.threeinarow.data.Config
import com.example.threeinarow.data.fieldElements.Block
import com.example.threeinarow.domain.models.AnimationData
import com.example.threeinarow.domain.models.ExplosionPatterns
import com.example.threeinarow.presentation.screens.gameScreen.blockSizeDp
import com.example.threeinarow.presentation.screens.gameScreen.components.board.GameBlock
import com.example.threeinarow.presentation.theme.AppTheme

@Composable
fun AnimatedGameBlock(
    block: Block,
    isActive: Boolean,
    onClick: () -> Unit,
    clickable: Boolean,
    explosionPattern: ExplosionPatterns?,
    animationData: AnimationData,
    onAnimationFinished: () -> Unit,
    modifier: Modifier = Modifier,
) {
    var fromOffsetY = 0
    val fromAlpha = 1f
    var toAlpha = 1f

    when (animationData) {
        is AnimationData.Destroy -> {
            toAlpha = 0f
        }

        is AnimationData.Fall -> {
            fromOffsetY = animationData.fallFromHeight
        }

        is AnimationData.Spawn -> {
            fromOffsetY = animationData.spawnFromHeight
        }

        is AnimationData.Nothing -> {}
    }
    val blockSizeDp = blockSizeDp

    val animationOffsetY =
        remember(animationData, block.id) { Animatable((-fromOffsetY) * blockSizeDp.value) }
    val animationAlpha = remember(animationData, block.id) { Animatable(fromAlpha) }

    val animationDurationMs = Config.ANIMATION_DURATION_MS

    LaunchedEffect(animationData, block.id) {
        when (animationData) {
            is AnimationData.Destroy -> {
                animationAlpha.animateTo(
                    targetValue = toAlpha,
                    animationSpec = tween(animationDurationMs)
                )
                onAnimationFinished()
            }

            is AnimationData.Fall, is AnimationData.Spawn -> {
                animationOffsetY.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(animationDurationMs)
                )
                onAnimationFinished()
            }

            is AnimationData.Nothing -> {}
        }
    }

    GameBlock(
        block = block,
        isActive = isActive,
        onClick = onClick,
        clickable = clickable,
        explosionPattern = explosionPattern,
        modifier = modifier
            .offset(y = animationOffsetY.value.dp)
            .alpha(alpha = animationAlpha.value)
    )
}

@Preview(
    widthDp = 300,
    heightDp = 300,
)
@Composable
private fun AnimatedGameBlockDestroyPreview() {
    AppTheme {
        val block = Block.exampleBlock
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            AnimatedGameBlock(
                block = block,
                isActive = false,
                explosionPattern = null,
                onClick = {},
                clickable = true,
                animationData = AnimationData.Destroy,
                onAnimationFinished = {},
            )
        }
    }
}

@Preview(
    widthDp = 300,
    heightDp = 300,
)
@Composable
private fun AnimatedGameBlockFallPreview() {
    AppTheme {
        val block = Block.exampleBlock
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            AnimatedGameBlock(
                block = block,
                isActive = false,
                explosionPattern = null,
                onClick = {},
                clickable = true,
                animationData = AnimationData.Fall(fallFromHeight = 1),
                onAnimationFinished = {},
            )
        }
    }
}

@Preview(
    widthDp = 300,
    heightDp = 300,
)
@Composable
private fun AnimatedGameBlockSpawnPreview() {
    AppTheme {
        val block = Block.exampleBlock
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {

            AnimatedGameBlock(
                block = block,
                isActive = false,
                explosionPattern = null,
                onClick = {},
                clickable = true,
                animationData = AnimationData.Spawn(spawnFromHeight = 1),
                onAnimationFinished = {},
            )
        }
    }
}

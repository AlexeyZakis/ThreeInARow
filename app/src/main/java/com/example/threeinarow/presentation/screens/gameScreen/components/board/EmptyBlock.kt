package com.example.threeinarow.presentation.screens.gameScreen.components.board

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.threeinarow.presentation.theme.AppTheme

@Composable
fun EmptyBlock(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .size(100.dp)
    )
}

@Preview
@Composable
private fun EmptyBlockPreview() {
    AppTheme {
        EmptyBlock()
    }
}

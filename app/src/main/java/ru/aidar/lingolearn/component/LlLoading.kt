package ru.aidar.lingolearn.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ru.aidar.lingolearn.ui.theme.LlColors

@Composable
fun LlLoading(
    llColors: LlColors
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(llColors.background),
        contentAlignment = Alignment.Center
    ) {
        LinearProgressIndicator(
            modifier = Modifier
                .padding(top = 50.dp)
                .padding(horizontal = 20.dp),
            color = llColors.inverseColor,
            trackColor = llColors.additionalColor
        )

    }
}
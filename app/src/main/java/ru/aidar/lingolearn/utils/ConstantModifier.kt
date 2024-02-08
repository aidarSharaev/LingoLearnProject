package ru.aidar.lingolearn.utils

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

object ConstantModifier {
    val columnModifier = Modifier
        .fillMaxWidth(0.7f)
        .fillMaxHeight()
    val textModifier = Modifier
        .fillMaxSize()
        .padding(top = 7.dp)
    val iconModifier = Modifier
        .padding(top = 10.dp)
        .fillMaxHeight(0.7f)
        .fillMaxWidth()
}
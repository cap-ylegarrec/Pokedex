package com.legarrec.pokedex.ui

import androidx.compose.foundation.background
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.TileMode
import androidx.core.graphics.toColorInt

fun Modifier.backgroundWithColors(colors: List<String>): Modifier {
    val parsedColors = colors.mapNotNull { runCatching { Color(it.toColorInt()) }.getOrNull() }
    return when {
        parsedColors.size == 1 -> this.background(parsedColors.first())
        parsedColors.size > 1 -> this.background(
            Brush.linearGradient(
                colors = parsedColors,
                start = Offset(0f, 0f),
                end = Offset(1000f, 0f),
                tileMode = TileMode.Clamp
            )
        )
        else -> this.background(Color.White)
    }
}

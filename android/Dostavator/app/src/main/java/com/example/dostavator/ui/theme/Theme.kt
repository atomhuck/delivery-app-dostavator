package com.example.dostavator.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

@Composable
fun DostavatorTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) {
        darkColorScheme(
            primary = PurplePrimary,
            secondary = PurpleLight,
            background = Color.Black,
            surface = Color(0xFF1E1E1E)
        )
    } else {
        lightColorScheme(
            primary = PurplePrimary,
            secondary = PurpleLight,
            background = White,
            surface = White
        )
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography(),
        content = content
    )
}
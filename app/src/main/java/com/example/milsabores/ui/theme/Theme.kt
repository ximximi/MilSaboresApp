package com.example.milsabores.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

// --- Paleta de colores CLARA (Light) personalizada ---
private val LightColorScheme = lightColorScheme(
    primary = RosaAcento, // Color principal (botones, acentos)
    secondary = CafeAcento, // Color secundario
    background = CremaFondo, // Color de fondo de la app
    surface = CremaFondo, // Color de las "tarjetas" (Cards)
    onPrimary = CafeLetra, // Texto sobre el color primario (ej. texto en botón rosa)
    onSecondary = CremaFondo, // Texto sobre el color secundario
    onBackground = CafeLetra, // Color del texto principal (sobre el fondo crema)
    onSurface = CafeLetra // Color del texto sobre las tarjetas
)

// --- Paleta de colores OSCURA (Dark) personalizada ---
// (Por ahora podemos hacer que se parezca a la clara, o usar los defaults)
private val DarkColorScheme = darkColorScheme(
    primary = RosaAcento,
    secondary = CafeAcento,
    background = CafeLetra, // Fondo oscuro
    surface = CafeLetra,
    onPrimary = CafeLetra,
    onSecondary = CremaFondo,
    onBackground = CremaFondo, // Texto claro
    onSurface = CremaFondo
)

@Composable
fun MilSaboresTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color (Android 12+)
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }

        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.primary.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
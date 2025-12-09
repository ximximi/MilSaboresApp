package com.example.milsabores.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.milsabores.R // <-- Importante para R.font

// Define las familias de fuentes
val Pacifico = FontFamily(
    Font(R.font.pacifico_regular, FontWeight.Normal)
)

val Lato = FontFamily(
    Font(R.font.lato_regular, FontWeight.Normal),
    Font(R.font.lato_bold, FontWeight.Bold)
    // Puedes añadir más pesos de Lato aquí (light, black, etc.)
)

// Define la Tipografía de Material 3
val Typography = Typography(
    // --- Títulos (H1, H2, etc.) ---
    // Usaremos Pacifico para los títulos grandes, como en tu web
    displayLarge = TextStyle(
        fontFamily = Pacifico,
        fontWeight = FontWeight.Normal,
        fontSize = 57.sp
    ),
    headlineLarge = TextStyle(
        fontFamily = Pacifico,
        fontWeight = FontWeight.Normal,
        fontSize = 32.sp
    ),
    titleLarge = TextStyle(
        fontFamily = Pacifico,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp
    ),

    // --- Cuerpo del Texto (Párrafos, botones, etc.) ---
    // Usaremos Lato para todo lo demás
    bodyLarge = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
    bodyMedium = TextStyle(
        fontFamily = Lato,
        fontWeight = FontWeight.Normal,
        fontSize = 14.sp
    ),
    labelMedium = TextStyle( // Texto de los botones
        fontFamily = Lato,
        fontWeight = FontWeight.Bold, // Los botones suelen ser en negrita
        fontSize = 14.sp
    )
)
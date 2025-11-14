package com.example.milsabores.ui.components

import android.widget.TextView
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

/**
 * Este Composable "traduce" un string de HTML simple
 * a un componente de texto nativo de Android.
 */
@Composable
fun HtmlText(html: String, modifier: Modifier = Modifier) {

    // Obtenemos los colores y tamaños de nuestro Tema (Crema, Café, etc.)
    val color = MaterialTheme.colorScheme.onBackground.toArgb()
    val fontSize = MaterialTheme.typography.bodyLarge.fontSize.value

    AndroidView(
        modifier = modifier,
        factory = { context ->
            // 1. Crea un TextView (el componente de texto clásico)
            TextView(context).apply {
                // Le aplica nuestros colores y tamaños del tema
                setTextColor(color)
                textSize = fontSize
            }
        },
        update = { view ->
            // 2. "Traduce" el HTML y lo pone en el TextView
            view.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT)
        }
    )
}
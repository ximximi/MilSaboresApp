package com.example.milsabores.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.milsabores.data.local.entity.Producto

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductoCard(
    producto: Producto,
    onProductoClick: () -> Unit
) {
    Card(
        onClick = onProductoClick, // La tarjeta completa es clickeable
        modifier = Modifier
            .width(180.dp) // Ancho fijo para la tarjeta
            .padding(8.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background // Fondo Crema
        )
    ) {
        Column {
            // --- TRADUCCIÓN de <div class="container-img"> ---
            // Coil (AsyncImage) puede cargar imágenes desde "assets"
            // usando un URI especial
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    // Esta es la "magia": le decimos que cargue desde assets
                    .data("file:///android_asset/${producto.imagen}")
                    .crossfade(true)
                    .build(),
                contentDescription = producto.nombre,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp),
                contentScale = ContentScale.Crop // Rellena el espacio
            )

            // --- TRADUCCIÓN de <div class="content-card-product"> ---
            Column(modifier = Modifier.padding(12.dp)) {
                // (Tu JSON no tenía estrellas, pero así las pondrías)
                // Row {
                //     Icon(Icons.Default.Star, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                // }
                Text(
                    text = producto.nombre,
                    style = MaterialTheme.typography.titleMedium, // Fuente Lato Bold (por defecto)
                    fontWeight = FontWeight.Bold,
                    maxLines = 1, // Solo una línea
                    overflow = TextOverflow.Ellipsis // Pone "..." si es muy largo
                )
                // --- TRADUCCIÓN de <p class="price"> ---
                Text(
                    text = "$${producto.precio}", // Formato de precio
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary // Color Rosa
                )
            }
        }
    }
}
package com.example.milsabores.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
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
import com.example.milsabores.data.local.entity.Blog

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BlogCard(
    blog: Blog,
    onClick: () -> Unit
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth() // La tarjeta ocupará todo el ancho
            .padding(vertical = 8.dp, horizontal = 16.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Column {
            // --- Imagen del Blog ---
            AsyncImage(
                model = ImageRequest.Builder(LocalContext.current)
                    // Cargamos desde assets (recuerda mover tus img/ a assets/img/)
                    .data("file:///android_asset/${blog.imagen}")
                    .crossfade(true)
                    .build(),
                contentDescription = blog.titulo,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp),
                contentScale = ContentScale.Crop
            )

            // --- Contenido del Blog ---
            Column(modifier = Modifier.padding(16.dp)) {
                // --- Título ---
                Text(
                    text = blog.titulo,
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                // --- Fecha ---
                Text(
                    text = blog.fecha,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant // Un color más suave
                )

                Spacer(modifier = Modifier.height(8.dp))

                // --- Resumen ---
                Text(
                    text = blog.resumen,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
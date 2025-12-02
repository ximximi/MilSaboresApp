package com.example.milsabores.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Remove
// ---------------------------------
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.milsabores.R
import com.example.milsabores.ui.viewmodel.ItemCarritoDetallado

@Composable
fun CarritoItemCard(
    item: ItemCarritoDetallado,
    onSumar: () -> Unit,
    onRestar: () -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        elevation = CardDefaults.cardElevation(2.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.background
        )
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // --- Imagen del Producto ---
            Card(modifier = Modifier.size(80.dp), elevation = CardDefaults.cardElevation(0.dp)) {
                // Lógica inteligente para la imagen pequeña
                val imagenUrl = if (item.producto.imagen.startsWith("http")) {
                    item.producto.imagen
                } else {
                    "file:///android_asset/${item.producto.imagen}"
                }

                AsyncImage(
                    model = ImageRequest.Builder(LocalContext.current)
                        .data(imagenUrl)
                        .crossfade(true)
                        .build(),
                    contentDescription = item.producto.nombre,
                    modifier = Modifier
                        .size(80.dp) // Tamaño pequeño para el carrito
                        .clip(RoundedCornerShape(8.dp)),
                    contentScale = ContentScale.Crop,
                    // Placeholder si falla
                    error = painterResource(R.drawable.logo_mil_sabores)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // --- Nombre y Precio ---
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = item.producto.nombre,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "$${item.producto.precio * item.cantidad}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            // --- Controles de Cantidad (+ / -) ---
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Botón Restar (-)
                IconButton(
                    onClick = onRestar,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Filled.Remove, "Restar") // <-- CORREGIDO
                }

                Text(
                    text = "${item.cantidad}",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                // Botón Sumar (+)
                IconButton(
                    onClick = onSumar,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Filled.Add, "Sumar") // <-- CORREGIDO
                }

                // Botón Eliminar (Basura)
                IconButton(
                    onClick = onEliminar,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(Icons.Filled.Delete, "Eliminar", tint = MaterialTheme.colorScheme.error) // <-- CORREGIDO
                }
            }
        }
    }
}
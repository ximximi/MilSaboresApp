package com.example.milsabores.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.milsabores.ui.viewmodel.AppViewModelProvider
import com.example.milsabores.ui.viewmodel.DetalleBlogViewModel
import com.example.milsabores.ui.components.HtmlText
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetalleBlogScreen(
    onVolverClick: () -> Unit,
    viewModel: DetalleBlogViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Detalle del Blog") },
                navigationIcon = {
                    IconButton(onClick = onVolverClick) {
                        Icon(Icons.Default.ArrowBack, "Volver")
                    }
                }
            )
        }
    ) { paddingValues ->
        when {
            uiState.estaCargando -> {
                Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }
            uiState.error != null -> {
                Box(Modifier.fillMaxSize().padding(paddingValues), contentAlignment = Alignment.Center) {
                    Text("Error: ${uiState.error}")
                }
            }
            uiState.entrada != null -> {
                val entrada = uiState.entrada!!
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                ) {
                    // Imagen
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data("file:///android_asset/${entrada.imagen}")
                            .crossfade(true)
                            .build(),
                        contentDescription = entrada.titulo,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(250.dp)
                    )

                    // Contenido
                    Column(Modifier.padding(16.dp)) {
                        Text(
                            text = entrada.titulo,
                            style = MaterialTheme.typography.headlineLarge,
                            fontFamily = MaterialTheme.typography.titleLarge.fontFamily
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Por ${entrada.autor} | ${entrada.fecha}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )

                        Divider(modifier = Modifier.padding(vertical = 16.dp))

                        // --- ¡AQUÍ RENDERIZAMOS EL HTML! ---
                        HtmlText(html = entrada.contenido)
                    }
                }
            }
        }
    }
}
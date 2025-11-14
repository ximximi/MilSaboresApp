package com.example.milsabores.ui.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart // Reemplazaremos este ícono
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.milsabores.ui.theme.MilSaboresTheme
import androidx.compose.ui.res.painterResource
import com.example.milsabores.R

@Composable
fun IndexScreen(
    onEntrarClick: () -> Unit,
    onAdminClick: () -> Unit
){
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ){
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ){
            // Usamos el color 'primary' (Rosa) para el ícono
            Image(
                painter = painterResource(id = R.drawable.logo_mil_sabores), // <-- ¡Usa el nombre de tu archivo!
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )

            // Usamos la fuente 'Pacifico' para el título
            Text(
                text = "Mil Sabores",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Este botón usará los colores 'primary' y 'onPrimary'
            Button(
                onClick = onEntrarClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ){
                Text("Ver Tienda")
            }
            Spacer(modifier = Modifier.height(16.dp))

            OutlinedButton(
                onClick = onAdminClick,
                modifier = Modifier.fillMaxWidth()
            ){
                Text("Iniciar Sesión")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IndexScreenPreview() {
    MilSaboresTheme {
        IndexScreen({}, {})
    }
}
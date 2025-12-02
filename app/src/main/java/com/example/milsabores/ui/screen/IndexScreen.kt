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
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.milsabores.R
import com.example.milsabores.ui.theme.MilSaboresTheme

@Composable
fun IndexScreen(
    // --- PARÁMETROS ACTUALIZADOS (3 Botones) ---
    onLoginClick: () -> Unit,
    onRegistroClick: () -> Unit,
    onInvitadoClick: () -> Unit
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
            Image(
                painter = painterResource(id = R.drawable.logo_mil_sabores),
                contentDescription = "Logo",
                modifier = Modifier.size(120.dp)
            )

            Text(
                text = "Mil Sabores",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(32.dp))

            // 1. Botón Iniciar Sesión
            Button(
                onClick = onLoginClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ){
                Text("INICIAR SESIÓN")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 2. Botón Crear Cuenta
            Button(
                onClick = onRegistroClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ){
                Text("CREAR CUENTA")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 3. Botón Invitado
            OutlinedButton(
                onClick = onInvitadoClick,
                modifier = Modifier.fillMaxWidth()
            ){
                Text("Entrar como invitado")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun IndexScreenPreview() {
    MilSaboresTheme {
        IndexScreen({}, {}, {})
    }
}
package com.example.futbolapp.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.futbolapp.R
import com.example.futbolapp.ui.theme.*

@Composable
fun FinalScreen(navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }
    
    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(Color(0xFF001A33), Color(0xFF000000)))),
        contentAlignment = Alignment.Center
    ) {
        // Fondo sutil con icono
        Image(
            painter = painterResource(id = R.drawable.icono4),
            contentDescription = null,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.15f
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.padding(32.dp)
        ) {
            AnimatedVisibility(
                visible = startAnimation,
                enter = scaleIn(animationSpec = tween(1000)) + fadeIn()
            ) {
                Surface(
                    modifier = Modifier.size(160.dp).clip(CircleShape),
                    color = Color.White.copy(alpha = 0.1f),
                    border = androidx.compose.foundation.BorderStroke(2.dp, BlueVivoPrimary)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icono10),
                        contentDescription = "Logo",
                        modifier = Modifier.padding(30.dp).fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            AnimatedVisibility(
                visible = startAnimation,
                enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn(animationSpec = tween(800, delayMillis = 500))
            ) {
                Text(
                    text = "¡Misión Cumplida!",
                    color = Color.White,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            AnimatedVisibility(
                visible = startAnimation,
                enter = slideInVertically(initialOffsetY = { 50 }) + fadeIn(animationSpec = tween(800, delayMillis = 800))
            ) {
                Text(
                    text = "Tu equipo está listo para la gloria.\nNos vemos en la próxima jornada.",
                    color = Color.Gray,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center,
                    lineHeight = 24.sp
                )
            }

            Spacer(modifier = Modifier.height(60.dp))

            Button(
                onClick = { /* Aquí puedes cerrar la app o volver al inicio */
                    navController.navigate("welcome") {
                        popUpTo(0)
                    }
                },
                modifier = Modifier.fillMaxWidth().height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueVivoPrimary),
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.ExitToApp, contentDescription = null)
                Spacer(Modifier.width(12.dp))
                Text("CERRAR SESIÓN", fontWeight = FontWeight.Bold)
            }
        }
    }
}

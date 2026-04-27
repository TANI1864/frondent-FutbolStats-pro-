package com.example.futbolapp.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.futbolapp.NavRoutes
import com.example.futbolapp.R
import com.example.futbolapp.ui.theme.*

@Composable
fun WelcomeScreen(navController: NavController) {
    var startAnimation by remember { mutableStateOf(false) }
    
    val infiniteTransition = rememberInfiniteTransition(label = "infinite")
    
    // Animación de pulso para el logo
    val logoScale by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "logoScale"
    )

    // Animación de flotación para el contenido
    val floatAnim by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 15f,
        animationSpec = infiniteRepeatable(
            animation = tween(2500, easing = EaseInOutSine),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floatAnim"
    )

    LaunchedEffect(Unit) {
        startAnimation = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(BlueVivoPrimary, Color(0xFF001A33))))
            .pointerInput(Unit) {
                detectDragGestures { change, dragAmount ->
                    if (dragAmount.y < -50) {
                        navController.navigate(NavRoutes.HOME) {
                            popUpTo(NavRoutes.WELCOME) { inclusive = true }
                        }
                    }
                }
            },
        contentAlignment = Alignment.Center
    ) {
        // Imagen de fondo con tinte oscuro
        Image(
            painter = painterResource(id = R.drawable.icono4),
            contentDescription = null,
            modifier = Modifier.fillMaxSize().graphicsLayer(alpha = 0.3f),
            contentScale = ContentScale.Crop
        )

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(24.dp)
                .offset(y = floatAnim.dp) // Efecto flotante
        ) {
            // Contenedor del Logo con Efecto Glassmorphism
            AnimatedVisibility(
                visible = startAnimation,
                enter = scaleIn(animationSpec = tween(1000)) + fadeIn(animationSpec = tween(1000))
            ) {
                Surface(
                    modifier = Modifier
                        .size(200.dp)
                        .scale(logoScale) // Efecto pulso
                        .clip(CircleShape),
                    color = Color.White.copy(alpha = 0.15f),
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color.White.copy(alpha = 0.3f))
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.icono10),
                        contentDescription = "App Logo",
                        modifier = Modifier.padding(30.dp).fillMaxSize(),
                        contentScale = ContentScale.Fit
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Texto con entrada escalonada
            AnimatedVisibility(
                visible = startAnimation,
                enter = slideInVertically(initialOffsetY = { 40 }) + fadeIn(animationSpec = tween(1200, delayMillis = 300))
            ) {
                Text(
                    text = "FútbolStats Pro",
                    color = Color.White,
                    fontSize = 42.sp,
                    fontWeight = FontWeight.Black,
                    textAlign = TextAlign.Center,
                    letterSpacing = 1.sp
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            AnimatedVisibility(
                visible = startAnimation,
                enter = slideInVertically(initialOffsetY = { 40 }) + fadeIn(animationSpec = tween(1200, delayMillis = 600))
            ) {
                Text(
                    text = "EL PODER DE TU EQUIPO\nEN TUS MANOS",
                    color = Color(0xFF38BDF8),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp,
                    letterSpacing = 2.sp
                )
            }
        }

        // Indicador de deslizamiento "Top"
        val swipeAlpha by infiniteTransition.animateFloat(
            initialValue = 0.3f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = FastOutSlowInEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "swipeAlpha"
        )

        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.KeyboardArrowUp,
                contentDescription = null,
                tint = Color.White.copy(alpha = swipeAlpha),
                modifier = Modifier.size(44.dp)
            )
            Text(
                text = "DESLIZA PARA EMPEZAR",
                color = Color.White.copy(alpha = swipeAlpha),
                fontSize = 12.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 3.sp
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun WelcomeScreenPreview() {
    FutbolappTheme {
        WelcomeScreen(navController = rememberNavController())
    }
}

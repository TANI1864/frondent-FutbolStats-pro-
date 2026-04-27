package com.example.futbolapp.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.futbolapp.NavRoutes
import com.example.futbolapp.R
import com.example.futbolapp.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController) {
    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(listOf(BlueVivoPrimary, BlueVivoSecondary)))
            ) {
                TopAppBar(
                    title = { Text("⚽ FútbolStats Pro", color = Color.White, fontWeight = FontWeight.ExtraBold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Image(
                painter = painterResource(id = R.drawable.icono3),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop,
                alpha = 0.12f
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Sección Superior: Refinada
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.88f)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "¡Es tu turno de liderar!",
                                style = MaterialTheme.typography.titleLarge,
                                color = BlueVivoPrimary,
                                fontWeight = FontWeight.ExtraBold,
                                lineHeight = 28.sp
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = buildAnnotatedString {
                                    append("Gestiona tu plantilla y forja un ")
                                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                                        append("legado imbatible.")
                                    }
                                },
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.DarkGray
                            )
                        }
                        Image(
                            painter = painterResource(id = R.drawable.icono11),
                            contentDescription = null,
                            modifier = Modifier.size(90.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Grid de botones interactivos
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        GridButton(
                            text = "Equipos 🛡️",
                            icon = Icons.Default.Groups,
                            gradient = Brush.verticalGradient(listOf(CardEquipos, BlueVivoPrimary)),
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate(NavRoutes.EQUIPOS) }
                        )
                        GridButton(
                            text = "Jugadores 🏃‍♂️",
                            icon = Icons.Default.Person,
                            gradient = Brush.verticalGradient(listOf(CardJugadores, Color(0xFF60A5FA))),
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate(NavRoutes.JUGADORES) }
                        )
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                        GridButton(
                            text = "Entrenadores 📋",
                            icon = Icons.Default.Sports,
                            gradient = Brush.verticalGradient(listOf(CardEntrenadores, Color(0xFF38BDF8))),
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate(NavRoutes.ENTRENADORES) }
                        )
                        GridButton(
                            text = "Partidos ⚽",
                            icon = Icons.Default.SportsSoccer,
                            gradient = Brush.verticalGradient(listOf(CardPartidos, Color(0xFF3B82F6))),
                            modifier = Modifier.weight(1f),
                            onClick = { navController.navigate(NavRoutes.PARTIDOS) }
                        )
                    }
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        GridButton(
                            text = "Estadísticas 📊",
                            icon = Icons.Default.BarChart,
                            gradient = Brush.verticalGradient(listOf(CardEstadisticas, Color(0xFF818CF8))),
                            modifier = Modifier.fillMaxWidth(0.5f),
                            onClick = { navController.navigate(NavRoutes.ESTADISTICAS) }
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

@Composable
fun GridButton(text: String, icon: ImageVector, gradient: Brush, modifier: Modifier = Modifier, onClick: () -> Unit) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    
    // Animación de escala suave al presionar
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.94f else 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy, stiffness = Spring.StiffnessLow),
        label = "scale"
    )

    Card(
        modifier = modifier
            .height(120.dp)
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .clickable(
                interactionSource = interactionSource,
                indication = null,
                onClick = onClick
            ),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isPressed) 2.dp else 8.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Icon(icon, contentDescription = null, tint = Color.White.copy(alpha = 0.9f), modifier = Modifier.size(42.dp))
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = text, color = Color.White, fontWeight = FontWeight.Black, fontSize = 15.sp, letterSpacing = 0.5.sp)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    FutbolappTheme {
        HomeScreen(navController = rememberNavController())
    }
}

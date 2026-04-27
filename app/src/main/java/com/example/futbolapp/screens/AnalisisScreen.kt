package com.example.futbolapp.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
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
fun AnalisisScreen(navController: NavController) {
    Scaffold(
        topBar = {
            // TopBar con Degradado consistente
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(listOf(BlueVivoPrimary, BlueVivoSecondary)))
            ) {
                TopAppBar(
                    title = { Text("Análisis y Reportes", color = Color.White, fontWeight = FontWeight.ExtraBold) },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        }
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(padding)) {
            // Imagen de fondo sutil
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
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center // Centramos los botones
            ) {
                Text(
                    text = "Funciones de Análisis",
                    style = MaterialTheme.typography.headlineSmall,
                    color = BlueVivoPrimary,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = "Accede a las estadísticas avanzadas y reportes detallados del equipo.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray,
                    modifier = Modifier.padding(bottom = 32.dp)
                )

                // Botón de Estadísticas con Degradado
                AnalisisButton(
                    text = "Estadísticas Generales",
                    icon = Icons.Default.List,
                    gradient = Brush.verticalGradient(listOf(CardEstadisticas, BlueVivoPrimary)),
                    onClick = { navController.navigate(NavRoutes.ESTADISTICAS) }
                )
                
                // Se eliminó el botón de Consultas Avanzadas para mantener la limpieza total del proyecto.
            }
        }
    }
}

@Composable
fun AnalisisButton(text: String, icon: ImageVector, gradient: Brush, onClick: () -> Unit) {
    Card(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth().height(80.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(gradient),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                Text(text = text, color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 18.sp)
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AnalisisScreenPreview() {
    FutbolappTheme {
        AnalisisScreen(navController = rememberNavController())
    }
}

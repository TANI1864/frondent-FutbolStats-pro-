package com.example.futbolapp

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.futbolapp.screens.*

@Composable
fun NavGraph(navController: NavHostController) {

    NavHost(
        navController = navController,
        startDestination = NavRoutes.WELCOME
    ) {

        composable(NavRoutes.WELCOME) {
            WelcomeScreen(navController)
        }

        composable(NavRoutes.HOME) {
            HomeScreen(navController)
        }

        composable(NavRoutes.EQUIPOS) {
            EquiposScreen(navController)
        }

        composable(NavRoutes.JUGADORES) {
            JugadoresScreen(navController)
        }

        composable(NavRoutes.ENTRENADORES) {
            EntrenadoresScreen(navController)
        }

        composable(NavRoutes.PARTIDOS) {
            PartidosScreen(navController)
        }

        composable(NavRoutes.ESTADISTICAS) {
            EstadisticasScreen(navController)
        }
        
        composable(NavRoutes.REGISTRAR_ESTADISTICA) {
            RegistrarEstadisticaScreen(navController)
        }

        composable(NavRoutes.ANALISIS) {
            AnalisisScreen(navController)
        }
        
        composable(NavRoutes.FINAL) {
            FinalScreen(navController)
        }
    }
}

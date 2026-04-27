package com.example.futbolapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.rememberNavController
import com.example.futbolapp.ui.theme.FutbolappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FutbolappTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}

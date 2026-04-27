package com.example.futbolapp.screens

import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.futbolapp.NavRoutes
import com.example.futbolapp.ui.theme.BlueVivoPrimary
import com.example.futbolapp.ui.theme.BlueVivoSecondary

@Composable
fun CustomFormField(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    icon: ImageVector? = null,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        Text(
            label, 
            fontSize = 11.sp, 
            fontWeight = FontWeight.Bold, 
            color = BlueVivoPrimary,
            modifier = Modifier.padding(start = 4.dp, bottom = 4.dp)
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            leadingIcon = icon?.let { { Icon(it, contentDescription = null, tint = Color.DarkGray.copy(alpha = 0.6f), modifier = Modifier.size(18.dp)) } },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                unfocusedTextColor = Color.Black,
                unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                focusedBorderColor = BlueVivoPrimary,
                unfocusedContainerColor = Color.White,
                focusedContainerColor = Color.White
            ),
            singleLine = true
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDismissItem(
    onDelete: () -> Unit,
    content: @Composable () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = {
            if (it == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else false
        },
        positionalThreshold = { it * 0.6f }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        backgroundContent = {
            val color = when (dismissState.dismissDirection) {
                SwipeToDismissBoxValue.EndToStart -> Color(0xFFEF4444)
                else -> Color.Transparent
            }
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(18.dp))
                    .background(color)
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    Icons.Default.Delete, 
                    contentDescription = null, 
                    tint = Color.White,
                    modifier = Modifier.size(28.dp)
                )
            }
        }
    ) {
        content()
    }
}

// NUEVA BARRA DE NAVEGACIÓN SUPERIOR TIPO TABS
@Composable
fun TopNavigationBar(navController: NavController, currentRoute: String?) {
    val items = listOf(
        Triple("EQUIPOS", NavRoutes.EQUIPOS, Icons.Default.Groups),
        Triple("JUGADORES", NavRoutes.JUGADORES, Icons.Default.Person),
        Triple("TÉCNICOS", NavRoutes.ENTRENADORES, Icons.Default.Sports),
        Triple("PARTIDOS", NavRoutes.PARTIDOS, Icons.Default.SportsSoccer)
    )

    ScrollableTabRow(
        selectedTabIndex = items.indexOfFirst { it.second == currentRoute }.coerceAtLeast(0),
        containerColor = Color.Transparent,
        contentColor = Color.White,
        edgePadding = 16.dp,
        indicator = { tabPositions ->
            TabRowDefaults.SecondaryIndicator(
                Modifier.tabIndicatorOffset(tabPositions[items.indexOfFirst { it.second == currentRoute }.coerceAtLeast(0)]),
                color = Color.White,
                height = 3.dp
            )
        },
        divider = {}
    ) {
        items.forEach { (label, route, icon) ->
            Tab(
                selected = currentRoute == route,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(NavRoutes.HOME) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                text = {
                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = if (currentRoute == route) FontWeight.Black else FontWeight.Bold
                    )
                },
                icon = { Icon(icon, null, modifier = Modifier.size(18.dp)) }
            )
        }
    }
}

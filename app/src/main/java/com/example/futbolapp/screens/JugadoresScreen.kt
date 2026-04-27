package com.example.futbolapp.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.futbolapp.model.*
import com.example.futbolapp.NavRoutes
import com.example.futbolapp.ui.theme.*
import com.example.futbolapp.viewmodel.JugadorViewModel
import com.example.futbolapp.viewmodel.EquipoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JugadoresScreen(
    navController: NavController,
    viewModel: JugadorViewModel = viewModel(),
    equipoViewModel: EquipoViewModel = viewModel()
) {
    val jugadores by viewModel.jugadores.collectAsState()
    val equipos by equipoViewModel.equipos.collectAsState()
    val cargando by viewModel.cargando.collectAsState()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // ESTADO PARA EL BUSCADOR
    var searchQuery by remember { mutableStateOf("") }

    var nombre by remember { mutableStateOf("") }
    var posicion by remember { mutableStateOf("") }
    var dorsal by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var equipoSeleccionado by remember { mutableStateOf<Equipo?>(null) }
    var isFormVisible by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        equipoViewModel.cargarEquipos()
        viewModel.cargarJugadores()
    }

    // LÓGICA DE FILTRADO
    val filteredJugadores = jugadores.filter { 
        it.nombre.contains(searchQuery, ignoreCase = true)
    }
    
    val jugadoresAgrupados = filteredJugadores.groupBy { jugador ->
        jugador.equipo?.nombre 
            ?: equipos.find { it.idEquipo == jugador.idEquipo }?.nombre 
            ?: "Sin Equipo"
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(Brush.horizontalGradient(listOf(BlueVivoPrimary, BlueVivoSecondary)))) {
                TopAppBar(
                    title = {
                        Column {
                            Text("Jugadores 🏃‍♂️", fontWeight = FontWeight.ExtraBold, color = Color.White, fontSize = 20.sp)
                            Text("● Gestión de plantel", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp, fontWeight = FontWeight.Medium)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate(NavRoutes.HOME) { popUpTo(NavRoutes.HOME) { inclusive = true } } }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Inicio", tint = Color.White)
                        }
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
                TopNavigationBar(navController, currentRoute)
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FAFC))
                .padding(16.dp)
        ) {
            // BOTÓN REGISTRO
            Button(
                onClick = { isFormVisible = !isFormVisible },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isFormVisible) Color.Gray else BlueVivoPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(if (isFormVisible) Icons.Default.ExpandLess else Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text(if (isFormVisible) "CERRAR FORMULARIO" else "REGISTRAR NUEVO JUGADOR", fontWeight = FontWeight.Bold)
            }

            AnimatedVisibility(visible = isFormVisible) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            CustomFormField(label = "NOMBRE COMPLETO", value = nombre, onValueChange = { nombre = it }, icon = Icons.Default.Person)
                            Spacer(Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                CustomFormField(label = "POSICIÓN", value = posicion, onValueChange = { posicion = it }, modifier = Modifier.weight(1f))
                                CustomFormField(label = "DORSAL", value = dorsal, onValueChange = { dorsal = it }, modifier = Modifier.width(90.dp))
                            }
                            Spacer(Modifier.height(12.dp))
                            
                            Text("SELECCIONAR EQUIPO", fontSize = 11.sp, fontWeight = FontWeight.Black, color = BlueVivoPrimary, modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
                            ExposedDropdownMenuBox(expanded = expanded, onExpandedChange = { expanded = !expanded }) {
                                OutlinedTextField(
                                    value = equipoSeleccionado?.nombre ?: "Elige un equipo",
                                    onValueChange = {},
                                    readOnly = true,
                                    leadingIcon = { Icon(Icons.Default.Groups, null, tint = BlueVivoPrimary, modifier = Modifier.size(20.dp)) },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BlueVivoPrimary, unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f), unfocusedContainerColor = Color(0xFFF1F5F9), focusedContainerColor = Color.White)
                                )
                                ExposedDropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }, modifier = Modifier.background(Color.White)) {
                                    equipos.forEach { equipo ->
                                        DropdownMenuItem(text = { Text(equipo.nombre, fontWeight = FontWeight.Bold) }, onClick = { equipoSeleccionado = equipo; expanded = false })
                                    }
                                }
                            }
                            
                            Spacer(Modifier.height(24.dp))
                            Button(
                                onClick = {
                                    if (nombre.isNotEmpty() && equipoSeleccionado != null) {
                                        scope.launch {
                                            viewModel.crearJugador(JugadorRequest(nombre, posicion, dorsal.toIntOrNull() ?: 0, equipo = EquipoRef(equipoSeleccionado!!.idEquipo!!)))
                                            nombre = ""; posicion = ""; dorsal = ""; equipoSeleccionado = null; isFormVisible = false
                                            showSuccess = true; delay(3000); showSuccess = false
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BlueVivoPrimary),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("REGISTRAR", color = Color.White, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(visible = showSuccess) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(color = Color(0xFF4CAF50), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Text("¡Jugador registrado!", color = Color.White, modifier = Modifier.padding(12.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            // BARRA DE BÚSQUEDA (VISIBLE AQUÍ)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth().padding(bottom = 12.dp),
                placeholder = { Text("Buscar por nombre...", color = Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, null, tint = BlueVivoPrimary) },
                trailingIcon = { if (searchQuery.isNotEmpty()) IconButton(onClick = { searchQuery = "" }) { Icon(Icons.Default.Close, null) } },
                shape = RoundedCornerShape(16.dp),
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    unfocusedContainerColor = Color.White,
                    focusedContainerColor = Color.White,
                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                    focusedBorderColor = BlueVivoPrimary
                )
            )

            Text("Plantilla Actual", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = Color.DarkGray)
            
            if (cargando && jugadores.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BlueVivoPrimary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f).padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    jugadoresAgrupados.forEach { (nombreEquipo, lista) ->
                        item(key = "header_$nombreEquipo") { TeamSectionHeader(nombreEquipo) }
                        items(items = lista, key = { j -> j.idJugador ?: j.hashCode() }) { jugador ->
                            SwipeToDismissItem(onDelete = { jugador.idJugador?.let { viewModel.borrarJugador(it) } }) {
                                JugadorCardFinal(jugador, nombreEquipo) {
                                    jugador.idJugador?.let { viewModel.borrarJugador(it) }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun TeamSectionHeader(nombre: String) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
        color = Color.Transparent
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(Modifier.height(2.dp).weight(0.1f).background(BlueVivoPrimary.copy(alpha = 0.3f)))
            Text(" $nombre 🛡️ ", style = MaterialTheme.typography.labelMedium, color = BlueVivoPrimary, fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            Box(Modifier.height(2.dp).weight(1f).background(BlueVivoPrimary.copy(alpha = 0.3f)))
        }
    }
}

@Composable
fun JugadorCardFinal(jugador: Jugador, equipoNombre: String, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(48.dp).clip(CircleShape), color = BlueVivoPrimary.copy(alpha = 0.1f)) {
                Icon(Icons.Default.Person, null, modifier = Modifier.padding(12.dp), tint = BlueVivoPrimary)
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = jugador.nombre, style = MaterialTheme.typography.titleMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                Text(text = "$equipoNombre • ${jugador.posicion} • #${jugador.dorsal}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, null, tint = Color.Red.copy(alpha = 0.7f), modifier = Modifier.size(24.dp))
            }
        }
    }
}

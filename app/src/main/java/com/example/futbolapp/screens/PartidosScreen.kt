package com.example.futbolapp.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import com.example.futbolapp.model.Equipo
import com.example.futbolapp.model.EquipoRef
import com.example.futbolapp.model.Partido
import com.example.futbolapp.model.PartidoRequest
import com.example.futbolapp.ui.theme.*
import com.example.futbolapp.viewmodel.EquipoViewModel
import com.example.futbolapp.viewmodel.PartidoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PartidosScreen(
    navController: NavController,
    partidoViewModel: PartidoViewModel = viewModel(),
    equipoViewModel: EquipoViewModel = viewModel()
) {
    val partidos by partidoViewModel.partidos.collectAsState()
    val equipos by equipoViewModel.equipos.collectAsState()
    val cargando by partidoViewModel.cargando.collectAsState()
    val error by partidoViewModel.error.collectAsState()

    var estadio by remember { mutableStateOf("") }
    var golesLocal by remember { mutableStateOf("") }
    var golesVisita by remember { mutableStateOf("") }
    
    var isFormVisible by remember { mutableStateOf(false) }
    var expandedLocal by remember { mutableStateOf(false) }
    var equipoLocalSeleccionado by remember { mutableStateOf<Equipo?>(null) }
    
    var expandedVisita by remember { mutableStateOf(false) }
    var equipoVisitaSeleccionado by remember { mutableStateOf<Equipo?>(null) }
    
    var showSuccess by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        partidoViewModel.getPartidos()
        equipoViewModel.cargarEquipos()
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(listOf(BlueVivoPrimary, BlueVivoSecondary)))
            ) {
                TopAppBar(
                    title = {
                        Column {
                            Text("Partidos ⚽", fontWeight = FontWeight.ExtraBold, color = Color.White, fontSize = 20.sp)
                            Text("● Calendario y resultados", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                        }
                    },
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(Color(0xFFF8FAFC))
                .padding(16.dp)
        ) {
            // BOTÓN DESPLEGABLE
            Button(
                onClick = { isFormVisible = !isFormVisible },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFormVisible) Color.Gray else BlueVivoPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (isFormVisible) Icons.Default.Remove else Icons.Default.Add, contentDescription = null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (isFormVisible) "CERRAR FORMULARIO" else "REGISTRAR NUEVO PARTIDO", fontWeight = FontWeight.Bold)
                }
            }

            AnimatedVisibility(
                visible = isFormVisible,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(16.dp))
                    ElevatedCard(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(24.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            CustomFormField(label = "ESTADIO", value = estadio, onValueChange = { estadio = it }, icon = Icons.Default.LocationOn)
                            
                            Spacer(modifier = Modifier.height(12.dp))
                            
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("LOCAL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BlueVivoPrimary)
                                    ExposedDropdownMenuBox(expanded = expandedLocal, onExpandedChange = { expandedLocal = !expandedLocal }) {
                                        OutlinedTextField(
                                            value = equipoLocalSeleccionado?.nombre ?: "Sel.",
                                            onValueChange = {},
                                            readOnly = true,
                                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BlueVivoPrimary, unfocusedContainerColor = Color.White, focusedContainerColor = Color.White)
                                        )
                                        ExposedDropdownMenu(expanded = expandedLocal, onDismissRequest = { expandedLocal = false }) {
                                            equipos.forEach { equipo ->
                                                DropdownMenuItem(text = { Text(equipo.nombre, color = Color.Black) }, onClick = { equipoLocalSeleccionado = equipo; expandedLocal = false })
                                            }
                                        }
                                    }
                                }

                                Column(modifier = Modifier.weight(1f)) {
                                    Text("VISITA", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BlueVivoPrimary)
                                    ExposedDropdownMenuBox(expanded = expandedVisita, onExpandedChange = { expandedVisita = !expandedVisita }) {
                                        OutlinedTextField(
                                            value = equipoVisitaSeleccionado?.nombre ?: "Sel.",
                                            onValueChange = {},
                                            readOnly = true,
                                            modifier = Modifier.menuAnchor().fillMaxWidth(),
                                            shape = RoundedCornerShape(12.dp),
                                            colors = OutlinedTextFieldDefaults.colors(focusedBorderColor = BlueVivoPrimary, unfocusedContainerColor = Color.White, focusedContainerColor = Color.White)
                                        )
                                        ExposedDropdownMenu(expanded = expandedVisita, onDismissRequest = { expandedVisita = false }) {
                                            equipos.forEach { equipo ->
                                                DropdownMenuItem(text = { Text(equipo.nombre, color = Color.Black) }, onClick = { equipoVisitaSeleccionado = equipo; expandedVisita = false })
                                            }
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(12.dp))
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                CustomFormField(label = "GOLES L", value = golesLocal, onValueChange = { golesLocal = it }, icon = Icons.Default.SportsSoccer, modifier = Modifier.weight(1f))
                                CustomFormField(label = "GOLES V", value = golesVisita, onValueChange = { golesVisita = it }, icon = Icons.Default.SportsSoccer, modifier = Modifier.weight(1f))
                            }
                            
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = {
                                    if (estadio.isNotEmpty() && equipoLocalSeleccionado != null && equipoVisitaSeleccionado != null) {
                                        scope.launch {
                                            partidoViewModel.createPartido(
                                                PartidoRequest(
                                                    fecha = "2026-04-24",
                                                    estadio = estadio,
                                                    equipoLocal = EquipoRef(equipoLocalSeleccionado!!.idEquipo!!),
                                                    equipoVisita = EquipoRef(equipoVisitaSeleccionado!!.idEquipo!!),
                                                    golesLocal = golesLocal.toIntOrNull() ?: 0,
                                                    golesVisita = golesVisita.toIntOrNull() ?: 0
                                                )
                                            )
                                            delay(1000)
                                            if (partidoViewModel.error.value == null) {
                                                estadio = ""; golesLocal = ""; golesVisita = ""
                                                equipoLocalSeleccionado = null; equipoVisitaSeleccionado = null
                                                isFormVisible = false
                                                showSuccess = true
                                                delay(3000)
                                                showSuccess = false
                                            }
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BlueVivoPrimary),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                if (cargando) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                                else Text("REGISTRAR PARTIDO", color = Color.White, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(visible = showSuccess) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(color = Color(0xFF4CAF50), shape = RoundedCornerShape(12.dp)) {
                    Text("¡Partido registrado!", color = Color.White, modifier = Modifier.padding(12.dp).fillMaxWidth(), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Resultados Recientes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = Color.DarkGray)
            
            if (cargando && partidos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = BlueVivoPrimary) }
            } else {
                LazyColumn(
                    modifier = Modifier.padding(top = 12.dp).weight(1f),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    itemsIndexed(partidos) { index, partido ->
                        AnimatedPartidoCard(partido, index) {
                            partido.idPartido?.let { partidoViewModel.deletePartido(it) }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedPartidoCard(partido: Partido, index: Int, onDelete: () -> Unit) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        delay(index * 100L)
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = slideInVertically(initialOffsetY = { it / 2 }) + fadeIn(animationSpec = tween(600))
    ) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text(text = "🏟 ${partido.estadio}", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                    IconButton(onClick = onDelete, modifier = Modifier.size(20.dp)) { Icon(Icons.Default.Delete, null, tint = Color.Red.copy(alpha = 0.6f)) }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly, verticalAlignment = Alignment.CenterVertically) {
                    Text(text = partido.equipoLocal.nombre, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                    Surface(color = BlueVivoPrimary.copy(alpha = 0.1f), shape = RoundedCornerShape(10.dp)) {
                        Text(text = "${partido.golesLocal} - ${partido.golesVisita}", modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp), color = BlueVivoPrimary, style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Black)
                    }
                    Text(text = partido.equipoVisita.nombre, fontWeight = FontWeight.Bold, modifier = Modifier.weight(1f), textAlign = TextAlign.Center)
                }
            }
        }
    }
}

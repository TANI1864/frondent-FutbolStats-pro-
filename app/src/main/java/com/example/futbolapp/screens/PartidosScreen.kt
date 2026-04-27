package com.example.futbolapp.screens

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.futbolapp.model.*
import com.example.futbolapp.NavRoutes
import com.example.futbolapp.ui.theme.*
import com.example.futbolapp.viewmodel.EquipoViewModel
import com.example.futbolapp.viewmodel.PartidoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

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
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    var estadio by remember { mutableStateOf("") }
    var golesLocal by remember { mutableStateOf("") }
    var golesVisita by remember { mutableStateOf("") }
    var fechaSeleccionada by remember { mutableStateOf(SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())) }
    var showDatePicker by remember { mutableStateOf(false) }
    val datePickerState = rememberDatePickerState()

    var isFormVisible by remember { mutableStateOf(false) }
    var equipoLocalSeleccionado by remember { mutableStateOf<Equipo?>(null) }
    var equipoVisitaSeleccionado by remember { mutableStateOf<Equipo?>(null) }
    var expandedLocal by remember { mutableStateOf(false) }
    var expandedVisita by remember { mutableStateOf(false) }
    
    var showSuccess by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        partidoViewModel.getPartidos()
        equipoViewModel.cargarEquipos()
    }

    if (showDatePicker) {
        DatePickerDialog(
            onDismissRequest = { showDatePicker = false },
            confirmButton = {
                TextButton(onClick = {
                    datePickerState.selectedDateMillis?.let {
                        fechaSeleccionada = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date(it))
                    }
                    showDatePicker = false
                }) { Text("Confirmar") }
            }
        ) { DatePicker(state = datePickerState) }
    }

    Scaffold(
        topBar = {
            Column(modifier = Modifier.background(Brush.horizontalGradient(listOf(BlueVivoPrimary, BlueVivoSecondary)))) {
                TopAppBar(
                    title = {
                        Column {
                            Text("Partidos ⚽", fontWeight = FontWeight.ExtraBold, color = Color.White, fontSize = 20.sp)
                            Text("● Calendario y resultados", color = Color.White.copy(alpha = 0.7f), fontSize = 11.sp, fontWeight = FontWeight.Medium)
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
            Button(
                onClick = { isFormVisible = !isFormVisible },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isFormVisible) Color.Gray else BlueVivoPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(if (isFormVisible) Icons.Default.Remove else Icons.Default.Add, null)
                    Spacer(Modifier.width(8.dp))
                    Text(if (isFormVisible) "CERRAR FORMULARIO" else "REGISTRAR NUEVO PARTIDO", fontWeight = FontWeight.Bold)
                }
            }

            AnimatedVisibility(visible = isFormVisible) {
                Column {
                    Spacer(Modifier.height(16.dp))
                    ElevatedCard(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(24.dp), colors = CardDefaults.elevatedCardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)) {
                        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                            CustomFormField(label = "ESTADIO", value = estadio, onValueChange = { estadio = it }, icon = Icons.Default.LocationOn)
                            
                            Column {
                                Text("FECHA", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BlueVivoPrimary)
                                OutlinedTextField(
                                    value = fechaSeleccionada,
                                    onValueChange = {},
                                    readOnly = true,
                                    leadingIcon = { Icon(Icons.Default.DateRange, null, tint = BlueVivoPrimary) },
                                    modifier = Modifier.fillMaxWidth().clickable { showDatePicker = true },
                                    enabled = false,
                                    shape = RoundedCornerShape(12.dp),
                                    colors = OutlinedTextFieldDefaults.colors(disabledTextColor = Color.Black, disabledBorderColor = Color.LightGray)
                                )
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("LOCAL", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BlueVivoPrimary)
                                    ExposedDropdownMenuBox(expanded = expandedLocal, onExpandedChange = { expandedLocal = !expandedLocal }) {
                                        OutlinedTextField(value = equipoLocalSeleccionado?.nombre ?: "Sel.", onValueChange = {}, readOnly = true, modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                                        ExposedDropdownMenu(expanded = expandedLocal, onDismissRequest = { expandedLocal = false }) {
                                            equipos.forEach { e -> DropdownMenuItem(text = { Text(e.nombre) }, onClick = { equipoLocalSeleccionado = e; expandedLocal = false }) }
                                        }
                                    }
                                }
                                Column(modifier = Modifier.weight(1f)) {
                                    Text("VISITA", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = BlueVivoPrimary)
                                    ExposedDropdownMenuBox(expanded = expandedVisita, onExpandedChange = { expandedVisita = !expandedVisita }) {
                                        OutlinedTextField(value = equipoVisitaSeleccionado?.nombre ?: "Sel.", onValueChange = {}, readOnly = true, modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(), shape = RoundedCornerShape(12.dp))
                                        ExposedDropdownMenu(expanded = expandedVisita, onDismissRequest = { expandedVisita = false }) {
                                            equipos.forEach { e -> DropdownMenuItem(text = { Text(e.nombre) }, onClick = { equipoVisitaSeleccionado = e; expandedVisita = false }) }
                                        }
                                    }
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                CustomFormField(label = "GOLES L", value = golesLocal, onValueChange = { golesLocal = it }, icon = Icons.Default.SportsSoccer, modifier = Modifier.weight(1f))
                                CustomFormField(label = "GOLES V", value = golesVisita, onValueChange = { golesVisita = it }, icon = Icons.Default.SportsSoccer, modifier = Modifier.weight(1f))
                            }

                            Button(onClick = {
                                if (estadio.isNotEmpty() && equipoLocalSeleccionado != null && equipoVisitaSeleccionado != null) {
                                    scope.launch {
                                        partidoViewModel.createPartido(PartidoRequest(fecha = fechaSeleccionada, estadio = estadio, equipoLocal = EquipoRef(equipoLocalSeleccionado!!.idEquipo!!), equipoVisita = EquipoRef(equipoVisitaSeleccionado!!.idEquipo!!), golesLocal = golesLocal.toIntOrNull() ?: 0, golesVisita = golesVisita.toIntOrNull() ?: 0))
                                        isFormVisible = false; showSuccess = true; delay(3000); showSuccess = false
                                    }
                                }
                            }, modifier = Modifier.fillMaxWidth().height(56.dp), colors = ButtonDefaults.buttonColors(containerColor = BlueVivoPrimary), shape = RoundedCornerShape(16.dp)) {
                                Text("REGISTRAR PARTIDO", color = Color.White, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(visible = showSuccess) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(color = Color(0xFF4CAF50), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Text("¡Partido registrado!", color = Color.White, modifier = Modifier.padding(12.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Resultados Recientes", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = Color.DarkGray)
            
            if (cargando && partidos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = BlueVivoPrimary) }
            } else {
                LazyColumn(modifier = Modifier.weight(1f).padding(top = 8.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    itemsIndexed(items = partidos, key = { _, p -> p.idPartido ?: p.hashCode() }) { _, partido ->
                        SwipeToDismissItem(onDelete = { partido.idPartido?.let { partidoViewModel.deletePartido(it) } }) {
                            AnimatedPartidoCard(partido) { partido.idPartido?.let { partidoViewModel.deletePartido(it) } }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AnimatedPartidoCard(partido: Partido, onDelete: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(18.dp), colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                Text(text = "🏟 ${partido.estadio} • ${partido.fecha}", style = MaterialTheme.typography.labelMedium, color = Color.Gray)
                IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, null, tint = Color.Red.copy(alpha = 0.7f), modifier = Modifier.size(24.dp)) }
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

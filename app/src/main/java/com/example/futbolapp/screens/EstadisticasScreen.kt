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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.futbolapp.model.*
import com.example.futbolapp.ui.theme.*
import com.example.futbolapp.viewmodel.EstadisticaViewModel
import com.example.futbolapp.viewmodel.JugadorViewModel
import com.example.futbolapp.viewmodel.PartidoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EstadisticasScreen(
    navController: NavController,
    viewModel: EstadisticaViewModel = viewModel(),
    jugadorViewModel: JugadorViewModel = viewModel(),
    partidoViewModel: PartidoViewModel = viewModel()
) {
    val estadisticas by viewModel.estadisticas.collectAsState()
    val jugadores by jugadorViewModel.jugadores.collectAsState()
    val partidos by partidoViewModel.partidos.collectAsState()
    val cargando by viewModel.cargando.collectAsState()

    var isFormVisible by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    var selectedJugador by remember { mutableStateOf<Jugador?>(null) }
    var selectedPartido by remember { mutableStateOf<Partido?>(null) }
    var goles by remember { mutableIntStateOf(0) }
    var asistencias by remember { mutableIntStateOf(0) }
    var amarillas by remember { mutableIntStateOf(0) }
    var rojas by remember { mutableIntStateOf(0) }
    var minutos by remember { mutableStateOf("90") }
    
    var expandedJ by remember { mutableStateOf(false) }
    var expandedP by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getEstadisticas()
        jugadorViewModel.cargarJugadores()
        partidoViewModel.getPartidos()
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(listOf(Color(0xFF1E3A8A), Color(0xFF3B82F6))))
            ) {
                TopAppBar(
                    title = {
                        Column {
                            Text("CENTRO DE DATOS 📊", fontWeight = FontWeight.Black, color = Color.White, fontSize = 20.sp)
                            Text("Control total de estadísticas", color = Color.White.copy(alpha = 0.8f), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
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
            Button(
                onClick = { isFormVisible = !isFormVisible },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isFormVisible) Color.Gray else Color(0xFF1E3A8A)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(if (isFormVisible) Icons.Default.ExpandLess else Icons.Default.AddChart, null)
                Spacer(Modifier.width(8.dp))
                Text(if (isFormVisible) "CERRAR FORMULARIO" else "REGISTRAR ACCIÓN DEL PARTIDO", fontWeight = FontWeight.Black)
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
                        Column(modifier = Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                            ExposedDropdownMenuBox(expanded = expandedP, onExpandedChange = { expandedP = !expandedP }) {
                                OutlinedTextField(
                                    value = if (selectedPartido != null) "${selectedPartido!!.equipoLocal.nombre} vs ${selectedPartido!!.equipoVisita.nombre}" else "Seleccionar Partido",
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("¿EN QUÉ ENCUENTRO?") },
                                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedP) }
                                )
                                ExposedDropdownMenu(expanded = expandedP, onDismissRequest = { expandedP = false }) {
                                    partidos.forEach { p ->
                                        DropdownMenuItem(text = { Text("${p.equipoLocal.nombre} vs ${p.equipoVisita.nombre}") }, onClick = { selectedPartido = p; expandedP = false })
                                    }
                                }
                            }

                            ExposedDropdownMenuBox(expanded = expandedJ, onExpandedChange = { expandedJ = !expandedJ }) {
                                OutlinedTextField(
                                    value = selectedJugador?.nombre ?: "Seleccionar Jugador",
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("¿QUÉ JUGADOR?") },
                                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedJ) }
                                )
                                ExposedDropdownMenu(expanded = expandedJ, onDismissRequest = { expandedJ = false }) {
                                    jugadores.forEach { j ->
                                        DropdownMenuItem(text = { Text(j.nombre) }, onClick = { selectedJugador = j; expandedJ = false })
                                    }
                                }
                            }

                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                StatCounterItem("GOLES ⚽", goles, Color(0xFF22C55E), Modifier.weight(1f)) { goles = it }
                                StatCounterItem("ASIST ⭐", asistencias, Color(0xFFEAB308), Modifier.weight(1f)) { asistencias = it }
                            }
                            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                                StatCounterItem("AMARILLAS 🟨", amarillas, Color(0xFFFFD700), Modifier.weight(1f)) { amarillas = it }
                                StatCounterItem("ROJAS 🟥", rojas, Color(0xFFEF4444), Modifier.weight(1f)) { rojas = it }
                            }

                            CustomFormField(label = "MINUTOS JUGADOS", value = minutos, onValueChange = { minutos = it }, icon = Icons.Default.Timer)

                            Button(
                                onClick = {
                                    if (selectedJugador != null && selectedPartido != null) {
                                        scope.launch {
                                            viewModel.crearEstadistica(
                                                EstadisticaRequest(
                                                    jugador = JugadorRef(selectedJugador!!.idJugador!!),
                                                    partido = PartidoRef(selectedPartido!!.idPartido!!),
                                                    goles = goles,
                                                    asistencias = asistencias,
                                                    tarjetasAmarillas = amarillas,
                                                    tarjetasRojas = rojas,
                                                    minutosJugados = minutos.toIntOrNull() ?: 0
                                                )
                                            )
                                            isFormVisible = false
                                            showSuccess = true
                                            delay(2000)
                                            showSuccess = false
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(54.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1E3A8A)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("GUARDAR ESTADISTICA", fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(visible = showSuccess) {
                Spacer(Modifier.height(16.dp))
                Surface(color = Color(0xFF4CAF50), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Text("✔ Guardado correctamente", color = Color.White, modifier = Modifier.padding(12.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("RESUMEN DE DESEMPEÑO", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Black, color = Color.DarkGray)

            if (cargando && estadisticas.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = Color(0xFF1E3A8A)) }
            } else {
                val statsPorPartido = estadisticas.groupBy { it.partido?.idPartido ?: it.idPartido ?: 0L }
                LazyColumn(modifier = Modifier.weight(1f).padding(top = 12.dp), verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    statsPorPartido.forEach { (partidoId, lista) ->
                        item {
                            val partidoInfo = lista.firstOrNull()?.partido ?: partidos.find { it.idPartido == partidoId }
                            MatchStatsDisplayCard(partidoId, partidoInfo, lista, jugadores) { statId ->
                                viewModel.eliminarEstadistica(statId)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MatchStatsDisplayCard(partidoId: Long, partido: Partido?, stats: List<EstadisticaJugador>, jugadores: List<Jugador>, onDelete: (Long) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val titulo = if (partido != null) "${partido.equipoLocal.nombre} vs ${partido.equipoVisita.nombre}" else "Partido #$partidoId"
    
    Card(
        modifier = Modifier.fillMaxWidth().clickable { expanded = !expanded },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.animateContentSize().padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(modifier = Modifier.size(40.dp), shape = CircleShape, color = Color(0xFF1E3A8A).copy(alpha = 0.1f)) {
                    Icon(Icons.Default.SportsSoccer, null, modifier = Modifier.padding(10.dp), tint = Color(0xFF1E3A8A))
                }
                Spacer(Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(titulo, fontWeight = FontWeight.Black, fontSize = 16.sp, color = Color.Black)
                    Text(partido?.estadio ?: "Ver detalles", color = Color.Gray, fontSize = 11.sp)
                }
                Icon(if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore, null, tint = Color.Gray)
            }
            
            if (expanded) {
                Spacer(Modifier.height(16.dp))
                stats.forEach { stat ->
                    val nombreJ = stat.jugador?.nombre ?: jugadores.find { it.idJugador == stat.idJugador }?.nombre ?: "Jugador #${stat.idJugador}"
                    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp), verticalAlignment = Alignment.CenterVertically) {
                        Text(nombreJ, modifier = Modifier.weight(1f), fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Text("${stat.goles} G | ${stat.asistencias} A", color = Color(0xFF1E3A8A), fontSize = 13.sp, fontWeight = FontWeight.Bold)
                        Spacer(Modifier.width(8.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            repeat(stat.tarjetasAmarillas) { Box(Modifier.size(10.dp, 14.dp).padding(horizontal = 1.dp).background(Color(0xFFFFD700), RoundedCornerShape(2.dp))) }
                            repeat(stat.tarjetasRojas) { Box(Modifier.size(10.dp, 14.dp).padding(horizontal = 1.dp).background(Color(0xFFEF4444), RoundedCornerShape(2.dp))) }
                            Spacer(Modifier.width(12.dp))
                            // BOTÓN ELIMINAR ESTADÍSTICA
                            IconButton(onClick = { stat.id?.let { onDelete(it) } }, modifier = Modifier.size(20.dp)) {
                                Icon(Icons.Default.Close, null, tint = Color.Red.copy(alpha = 0.5f))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCounterItem(label: String, value: Int, color: Color, modifier: Modifier, onValueChange: (Int) -> Unit) {
    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { if (value > 0) onValueChange(value - 1) }) { Icon(Icons.Default.RemoveCircleOutline, null, tint = Color.Gray) }
            Text("$value", fontWeight = FontWeight.Black, fontSize = 18.sp)
            IconButton(onClick = { onValueChange(value + 1) }) { Icon(Icons.Default.AddCircleOutline, null, tint = color) }
        }
    }
}

package com.example.futbolapp.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
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
fun RegistrarEstadisticaScreen(
    navController: NavController,
    viewModel: EstadisticaViewModel = viewModel(),
    jugadorViewModel: JugadorViewModel = viewModel(),
    partidoViewModel: PartidoViewModel = viewModel()
) {
    val jugadores by jugadorViewModel.jugadores.collectAsState()
    val partidos by partidoViewModel.partidos.collectAsState()
    
    var selectedJugador by remember { mutableStateOf<Jugador?>(null) }
    var selectedPartido by remember { mutableStateOf<Partido?>(null) }
    
    var goles by remember { mutableIntStateOf(0) }
    var asistencias by remember { mutableIntStateOf(0) }
    var amarillas by remember { mutableIntStateOf(0) }
    var rojas by remember { mutableIntStateOf(0) }
    var minutos by remember { mutableStateOf("90") }
    
    var expandedJ by remember { mutableStateOf(false) }
    var expandedP by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()
    var showSuccess by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        jugadorViewModel.cargarJugadores()
        partidoViewModel.getPartidos()
    }

    Scaffold(
        topBar = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Brush.horizontalGradient(listOf(BlueVivoPrimary, BlueVivoSecondary)))
            ) {
                TopAppBar(
                    title = { Text("Registrar Acción ⚽", color = Color.White, fontWeight = FontWeight.Black) },
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
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // SELECTOR DE PARTIDO
            Column {
                Text("PARTIDO", style = MaterialTheme.typography.labelLarge, color = BlueVivoPrimary, fontWeight = FontWeight.Bold)
                ExposedDropdownMenuBox(expanded = expandedP, onExpandedChange = { expandedP = !expandedP }) {
                    OutlinedTextField(
                        value = selectedPartido?.let { "${it.equipoLocal.nombre} vs ${it.equipoVisita.nombre}" } ?: "Seleccionar encuentro",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedP) },
                        colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.LightGray)
                    )
                    ExposedDropdownMenu(expanded = expandedP, onDismissRequest = { expandedP = false }) {
                        partidos.forEach { p ->
                            DropdownMenuItem(
                                text = { Text("${p.equipoLocal.nombre} vs ${p.equipoVisita.nombre}") }, 
                                onClick = { selectedPartido = p; expandedP = false }
                            )
                        }
                    }
                }
            }

            // SELECTOR DE JUGADOR
            Column {
                Text("JUGADOR", style = MaterialTheme.typography.labelLarge, color = BlueVivoPrimary, fontWeight = FontWeight.Bold)
                ExposedDropdownMenuBox(expanded = expandedJ, onExpandedChange = { expandedJ = !expandedJ }) {
                    OutlinedTextField(
                        value = selectedJugador?.nombre ?: "Seleccionar jugador",
                        onValueChange = {},
                        readOnly = true,
                        modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedJ) },
                        colors = OutlinedTextFieldDefaults.colors(unfocusedBorderColor = Color.LightGray)
                    )
                    ExposedDropdownMenu(expanded = expandedJ, onDismissRequest = { expandedJ = false }) {
                        jugadores.forEach { j ->
                            DropdownMenuItem(text = { Text(j.nombre) }, onClick = { selectedJugador = j; expandedJ = false })
                        }
                    }
                }
            }

            // CONTADORES
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                CounterItem(label = "GOLES", value = goles, onValueChange = { goles = it }, color = Color(0xFF22C55E), modifier = Modifier.weight(1f))
                CounterItem(label = "ASIST.", value = asistencias, onValueChange = { asistencias = it }, color = Color(0xFFEAB308), modifier = Modifier.weight(1f))
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                CounterItem(label = "AMARILLAS", value = amarillas, onValueChange = { amarillas = it }, color = Color(0xFFFFD700), modifier = Modifier.weight(1f))
                CounterItem(label = "ROJAS", value = rojas, onValueChange = { rojas = it }, color = Color(0xFFEF4444), modifier = Modifier.weight(1f))
            }
            
            CustomFormField(label = "MINUTOS JUGADOS", value = minutos, onValueChange = { minutos = it }, icon = Icons.Default.Timer)

            Spacer(Modifier.weight(1f))

            if (showSuccess) {
                Surface(color = Color(0xFF4CAF50), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Text("¡Registro exitoso!", color = Color.White, modifier = Modifier.padding(12.dp), textAlign = androidx.compose.ui.text.style.TextAlign.Center, fontWeight = FontWeight.Bold)
                }
            }

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
                            showSuccess = true
                            delay(1500)
                            navController.popBackStack()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth().height(60.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = BlueVivoPrimary)
            ) {
                Text("REGISTRAR ESTADÍSTICA", fontWeight = FontWeight.Black, letterSpacing = 1.sp)
            }
        }
    }
}

@Composable
fun CounterItem(label: String, value: Int, onValueChange: (Int) -> Unit, color: Color, modifier: Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = Color.White), elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(label, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = Color.Gray)
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { if (value > 0) onValueChange(value - 1) }) { Icon(Icons.Default.Remove, null, tint = color) }
                Text("$value", fontSize = 18.sp, fontWeight = FontWeight.Black)
                IconButton(onClick = { onValueChange(value + 1) }) { Icon(Icons.Default.Add, null, tint = color) }
            }
        }
    }
}

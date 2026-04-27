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
import com.example.futbolapp.model.*
import com.example.futbolapp.ui.theme.*
import com.example.futbolapp.viewmodel.EntrenadorViewModel
import com.example.futbolapp.viewmodel.EquipoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntrenadoresScreen(
    navController: NavController,
    viewModel: EntrenadorViewModel = viewModel(),
    equipoViewModel: EquipoViewModel = viewModel()
) {
    val entrenadores by viewModel.entrenadores.collectAsState()
    val equipos by equipoViewModel.equipos.collectAsState()
    val cargando by viewModel.cargando.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var especialidad by remember { mutableStateOf("") }
    
    // Selector de Equipo Estilizado
    var expanded by remember { mutableStateOf(false) }
    var equipoSeleccionado by remember { mutableStateOf<Equipo?>(null) }
    
    var isFormVisible by remember { mutableStateOf(false) }
    var showSuccess by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        equipoViewModel.cargarEquipos()
        viewModel.cargarEntrenadores()
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
                            Text("Entrenadores 📋", fontWeight = FontWeight.ExtraBold, color = Color.White, fontSize = 20.sp)
                            Text("● Gestión técnica", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp, fontWeight = FontWeight.Medium)
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
                colors = ButtonDefaults.buttonColors(containerColor = if (isFormVisible) Color.Gray else BlueVivoPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(if (isFormVisible) Icons.Default.ExpandLess else Icons.Default.Add, null)
                Spacer(Modifier.width(8.dp))
                Text(if (isFormVisible) "CERRAR FORMULARIO" else "REGISTRAR ENTRENADOR", fontWeight = FontWeight.Bold)
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
                            CustomFormField(label = "ESPECIALIDAD", value = especialidad, onValueChange = { especialidad = it }, icon = Icons.Default.Sports)
                            Spacer(Modifier.height(12.dp))
                            
                            // DISEÑO TOP DEL SELECTOR
                            Text("EQUIPO ASOCIADO", fontSize = 11.sp, fontWeight = FontWeight.Black, color = BlueVivoPrimary, modifier = Modifier.padding(start = 4.dp, bottom = 4.dp))
                            ExposedDropdownMenuBox(
                                expanded = expanded,
                                onExpandedChange = { expanded = !expanded }
                            ) {
                                OutlinedTextField(
                                    value = equipoSeleccionado?.nombre ?: "Vincular un equipo...",
                                    onValueChange = {},
                                    readOnly = true,
                                    leadingIcon = { Icon(Icons.Default.Groups, null, tint = BlueVivoPrimary, modifier = Modifier.size(20.dp)) },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                                    modifier = Modifier.menuAnchor(MenuAnchorType.PrimaryNotEditable).fillMaxWidth(),
                                    shape = RoundedCornerShape(14.dp),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = BlueVivoPrimary,
                                        unfocusedBorderColor = Color.LightGray.copy(alpha = 0.5f),
                                        unfocusedContainerColor = Color(0xFFF1F5F9),
                                        focusedContainerColor = Color.White
                                    )
                                )
                                ExposedDropdownMenu(
                                    expanded = expanded,
                                    onDismissRequest = { expanded = false },
                                    modifier = Modifier.background(Color.White)
                                ) {
                                    equipos.forEach { equipo ->
                                        DropdownMenuItem(
                                            text = {
                                                Row(verticalAlignment = Alignment.CenterVertically) {
                                                    Icon(Icons.Default.Shield, null, modifier = Modifier.size(20.dp), tint = BlueVivoPrimary)
                                                    Spacer(Modifier.width(12.dp))
                                                    Text(equipo.nombre, fontWeight = FontWeight.Bold)
                                                }
                                            },
                                            onClick = {
                                                equipoSeleccionado = equipo
                                                expanded = false
                                            }
                                        )
                                    }
                                }
                            }
                            
                            Spacer(Modifier.height(24.dp))
                            Button(
                                onClick = {
                                    if (nombre.isNotEmpty() && equipoSeleccionado != null) {
                                        scope.launch {
                                            viewModel.crearEntrenador(
                                                EntrenadorRequest(
                                                    nombre = nombre,
                                                    especialidad = especialidad,
                                                    equipo = EquipoRef(equipoSeleccionado!!.idEquipo!!)
                                                )
                                            )
                                            nombre = ""; especialidad = ""; equipoSeleccionado = null; isFormVisible = false
                                            showSuccess = true; delay(3000); showSuccess = false
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BlueVivoPrimary),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Text("REGISTRAR ENTRENADOR", color = Color.White, fontWeight = FontWeight.ExtraBold, letterSpacing = 1.sp)
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(visible = showSuccess) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(color = Color(0xFF4CAF50), shape = RoundedCornerShape(12.dp), modifier = Modifier.fillMaxWidth()) {
                    Text("¡Entrenador contratado!", color = Color.White, modifier = Modifier.padding(12.dp), textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text("Cuerpo Técnico", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = Color.DarkGray)
            
            if (cargando && entrenadores.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize().weight(1f), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = BlueVivoPrimary)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f).padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(bottom = 24.dp)
                ) {
                    itemsIndexed(items = entrenadores, key = { _, e -> e.idEntrenador ?: e.hashCode() }) { _, entrenador ->
                        val equipoNombre = entrenador.equipo?.nombre 
                            ?: equipos.find { it.idEquipo == entrenador.idEquipo }?.nombre 
                            ?: "Sin Equipo"
                        SwipeToDismissItem(onDelete = { entrenador.idEntrenador?.let { viewModel.borrarEntrenador(it) } }) {
                            EntrenadorCardFinal(entrenador, equipoNombre) {
                                entrenador.idEntrenador?.let { viewModel.borrarEntrenador(it) }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun EntrenadorCardFinal(entrenador: Entrenador, equipoNombre: String, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(48.dp).clip(CircleShape), color = BlueVivoPrimary.copy(alpha = 0.1f)) {
                Icon(Icons.Default.Sports, null, modifier = Modifier.padding(12.dp), tint = BlueVivoPrimary)
            }
            Spacer(Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = entrenador.nombre, style = MaterialTheme.typography.titleMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                Text(text = "$equipoNombre • ${entrenador.especialidad}", style = MaterialTheme.typography.bodySmall, color = Color.Gray)
            }
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, null, tint = Color.Red.copy(alpha = 0.7f), modifier = Modifier.size(24.dp))
            }
        }
    }
}

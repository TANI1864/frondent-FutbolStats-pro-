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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.futbolapp.model.Equipo
import com.example.futbolapp.ui.theme.*
import com.example.futbolapp.viewmodel.EquipoViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EquiposScreen(
    navController: NavController,
    viewModel: EquipoViewModel = viewModel()
) {
    val equipos by viewModel.equipos.collectAsState()
    val cargando by viewModel.cargando.collectAsState()

    var nombre by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var showSuccess by remember { mutableStateOf(false) }
    
    var isFormVisible by remember { mutableStateOf(false) }
    
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        viewModel.cargarEquipos()
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
                            Text("Equipos 🛡️", fontWeight = FontWeight.ExtraBold, color = Color.White, fontSize = 20.sp)
                            Text("● Gestión de clubes", color = Color.White.copy(alpha = 0.7f), fontSize = 12.sp, fontWeight = FontWeight.Medium)
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
                    Text(
                        if (isFormVisible) "CERRAR FORMULARIO" else "REGISTRAR NUEVO EQUIPO",
                        fontWeight = FontWeight.Bold
                    )
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
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.elevatedCardColors(containerColor = Color.White),
                        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
                    ) {
                        Column(modifier = Modifier.padding(20.dp)) {
                            CustomFormField(label = "NOMBRE DEL EQUIPO", value = nombre, onValueChange = { nombre = it }, icon = Icons.Default.Groups)
                            Spacer(modifier = Modifier.height(12.dp))
                            CustomFormField(label = "CIUDAD", value = ciudad, onValueChange = { ciudad = it }, icon = Icons.Default.LocationOn)
                            
                            Spacer(modifier = Modifier.height(20.dp))
                            
                            Button(
                                onClick = {
                                    if (nombre.isNotEmpty() && ciudad.isNotEmpty()) {
                                        scope.launch {
                                            viewModel.crearEquipo(Equipo(idEquipo = null, nombre = nombre, ciudad = ciudad))
                                            delay(800)
                                            nombre = ""; ciudad = ""; isFormVisible = false
                                            showSuccess = true
                                            delay(3000)
                                            showSuccess = false
                                        }
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().height(54.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = BlueVivoPrimary),
                                shape = RoundedCornerShape(14.dp)
                            ) {
                                if (cargando) CircularProgressIndicator(color = Color.White, modifier = Modifier.size(24.dp))
                                else Text("REGISTRAR", color = Color.White, fontWeight = FontWeight.ExtraBold)
                            }
                        }
                    }
                }
            }

            AnimatedVisibility(visible = showSuccess) {
                Spacer(modifier = Modifier.height(16.dp))
                Surface(color = Color(0xFF4CAF50), shape = RoundedCornerShape(12.dp)) {
                    Text("¡Equipo registrado!", color = Color.White, modifier = Modifier.padding(12.dp).fillMaxWidth(), textAlign = androidx.compose.ui.text.style.TextAlign.Center, fontWeight = FontWeight.Bold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                Text("Clubes Registrados", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.ExtraBold, color = Color.DarkGray)
                Surface(color = BlueVivoPrimary.copy(alpha = 0.1f), shape = CircleShape) {
                    Text("${equipos.size}", modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp), color = BlueVivoPrimary, fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
            
            Spacer(modifier = Modifier.height(12.dp))

            if (cargando && equipos.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator(color = BlueVivoPrimary) }
            } else {
                LazyColumn(modifier = Modifier.weight(1f), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    itemsIndexed(items = equipos, key = { _, equipo -> equipo.idEquipo ?: equipo.hashCode() }) { _, equipo ->
                        EquipoCardReal(equipo = equipo, onDelete = { equipo.idEquipo?.let { viewModel.borrarEquipo(it) } })
                    }
                }
            }
        }
    }
}

@Composable
fun EquipoCardReal(equipo: Equipo, onDelete: () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(modifier = Modifier.fillMaxWidth().padding(16.dp), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                Surface(modifier = Modifier.size(45.dp), color = BlueVivoPrimary.copy(alpha = 0.1f), shape = CircleShape) {
                    Icon(Icons.Default.Groups, contentDescription = null, modifier = Modifier.padding(10.dp), tint = BlueVivoPrimary)
                }
                Spacer(Modifier.width(12.dp))
                Column {
                    Text(text = equipo.nombre, style = MaterialTheme.typography.titleMedium, color = Color.Black, fontWeight = FontWeight.Bold)
                    Text(text = equipo.ciudad, style = MaterialTheme.typography.bodySmall, color = Color.Gray)
                }
            }
            // BOTÓN DE BORRADO DIRECTO SIN AVISOS
            IconButton(onClick = onDelete) { Icon(Icons.Default.Delete, contentDescription = "Eliminar", tint = Color.Red.copy(alpha = 0.8f), modifier = Modifier.size(26.dp)) }
        }
    }
}

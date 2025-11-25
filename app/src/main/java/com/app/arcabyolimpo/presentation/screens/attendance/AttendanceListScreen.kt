package com.app.arcabyolimpo.presentation.screens.attendance

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.arcabyolimpo.data.remote.dto.attendance.AttendanceDto
import com.app.arcabyolimpo.presentation.screens.user.detail.components.formatDate
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ReturnIcon
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AttendanceListScreen(
    viewModel: AttendanceListViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color(0xFF040610),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Asistencias",
                        color = Color.White,
                        fontSize = 20.sp,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        ReturnIcon(tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF040610),
                ),
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White,
                    )
                }

                uiState.error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Error: ${uiState.error}",
                            color = Color.White,
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadAttendance() }) {
                            Text("Reintentar")
                        }
                    }
                }

                uiState.attendances.isEmpty() -> {
                    Text(
                        text = "No hay asistencias registradas.",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                    ) {
                        items(uiState.attendances) { attendance ->
                            AttendanceItem(attendance = attendance)
                            Spacer(modifier = Modifier.height(12.dp))
                            Divider(
                                color = Color(0xFF1E293B),
                                thickness = 1.dp,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun AttendanceItem(attendance: AttendanceDto) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF050819), shape = RoundedCornerShape(16.dp))
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {

            // Primera fila: fecha izquierda, taller derecha
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = formatDate(attendance.fechaInicio) ?: "",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "Taller: ${attendance.nombreTaller}",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Hora de entrada: ${formatTime(attendance.fechaInicio) ?: "--:--"}",
                color = Color(0xFF9CA3AF),
                fontSize = 12.sp,
            )

            Text(
                text = "Hora de salida: ${attendance.fechaFin?.let { formatTime(it) } ?: "--:--"}",
                color = Color(0xFF9CA3AF),
                fontSize = 12.sp,
            )

        }
    }
}


private fun formatDate(dateTime: String?): String {
    if (dateTime.isNullOrBlank()) return ""
    return try {
        val datePart = dateTime.substringBefore("T")
        val (year, month, day) = datePart.split("-")
        "$day/$month/$year"
    } catch (e: Exception) {
        dateTime ?: ""
    }
}

private fun formatTime(dateTime: String?): String {
    if (dateTime.isNullOrBlank()) return ""
    return try {
        dateTime.substringAfter("T").substring(0, 5)
    } catch (e: Exception) {
        ""
    }
}


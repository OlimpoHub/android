//package com.app.arcabyolimpo.presentation.screens.attendance.components
//
//import java.time.ZoneId
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import com.app.arcabyolimpo.data.remote.dto.attendance.AttendanceDto
//import java.time.OffsetDateTime
//import java.time.format.DateTimeFormatter
//@Composable
//
//private fun AttendanceItem(attendance: AttendanceDto) {
//    Card(
//        modifier = Modifier.fillMaxWidth(),
//        colors = CardDefaults.cardColors(
//            containerColor = Color(0xFF121525),
//        ),
//    ) {
//        Column(
//            modifier = Modifier.padding(16.dp),
//        ) {
//            // Fila de fecha + taller
//            Row(
//                modifier = Modifier.fillMaxWidth(),
//                horizontalArrangement = Arrangement.SpaceBetween,
//                verticalAlignment = Alignment.CenterVertically,
//            ) {
//                Text(
//                    text = formatDate(attendance.fechaInicio),
//                    color = Color.White,
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Bold,
//                )
//                Text(
//                    text = "Taller: ${attendance.nombreTaller ?: "Sin nombre"}",
//                    color = Color.White,
//                    fontSize = 14.sp,
//                    fontWeight = FontWeight.Medium,
//                )
//
//            }
//
//            Spacer(modifier = Modifier.height(8.dp))
//
//            Text(
//                text = "Hora de entrada: ${formatTime(attendance.fechaInicio)}",
//                color = Color.LightGray,
//                fontSize = 12.sp,
//            )
//            Text(
//                text = "Hora de salida: ${
//                    attendance.fechaFin?.let { formatTime(it) } ?: "N/A"
//                }",
//                color = Color.LightGray,
//                fontSize = 12.sp,
//            )
//        }
//    }
//}
//
//
//
//
//private fun formatDate(isoString: String?): String {
//    if (isoString.isNullOrBlank()) return ""
//    return try {
//        val odt = OffsetDateTime.parse(isoString)
//        val localDateTime = odt.atZoneSameInstant(ZoneId.systemDefault())
//
//        localDateTime.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
//    } catch (_: Exception) {
//        isoString
//    }
//}
//
//private fun formatTime(isoString: String?): String {
//    if (isoString.isNullOrBlank()) return ""
//    return try {
//        val odt = OffsetDateTime.parse(isoString)
//
//        val localDateTime = odt.atZoneSameInstant(ZoneId.systemDefault())
//
//        localDateTime.format(DateTimeFormatter.ofPattern("HH:mm"))
//    } catch (_: Exception) {
//        ""
//    }
//}
//

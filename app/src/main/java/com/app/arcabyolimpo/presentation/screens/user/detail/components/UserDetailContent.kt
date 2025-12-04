package com.app.arcabyolimpo.presentation.screens.user.detail.components

import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.PaddingValues
import com.app.arcabyolimpo.presentation.theme.Poppins
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.DeleteButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ModifyButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.ActiveStatus
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.InactiveStatus

@Composable
fun UserDetailContent(
    collab: UserDto,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onAttendanceClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        // Profile Photo
        if (collab.foto != null) {
            AsyncImage(
                model = collab.foto,
                contentDescription = "Profile Photo",
                modifier =
                    Modifier
                        .size(120.dp)
                        .clip(CircleShape),
                contentScale = ContentScale.Crop,
            )
        } else {
            // Placeholder if no photo
            Box(
                modifier =
                    Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E293B)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = "${collab.nombre?.firstOrNull() ?: ""}${collab.apellidoPaterno?.firstOrNull() ?: ""}",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Full Name
        Text(
            text = "${collab.nombre} ${collab.apellidoPaterno} ${collab.apellidoMaterno}",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Status Badge
        if (collab.estatus == 1) {
            ActiveStatus()
        } else {
            InactiveStatus()
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Information Cards
        InfoCard(label = "Correo Electrónico", value = collab.correoElectronico)
        InfoCard(label = "Teléfono", value = collab.telefono)
        InfoCard(label = "Carrera", value = collab.carrera)
        InfoCard(label = "Fecha de Nacimiento", value = formatDate(collab.fechaNacimiento))
        InfoCard(label = "Rol", value = getRoleName(collab.idRol))

        Spacer(modifier = Modifier.height(32.dp))

        // Botón para ver asistencias
        if (collab.idRol == "3") {
            Box(
                modifier = Modifier
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center,
            ) {
                Button(
                    onClick = onAttendanceClick,
                    modifier = Modifier
                        .fillMaxWidth(0.76f)
                        .height(48.dp),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF2844AE),
                        contentColor = Color(0xFFFFF7EB),
                    )
                ) {
                    Text(
                        text = "Ver asistencias",
                        fontFamily = Poppins,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 14.sp,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // 🟣 Acciones secundarias: Modificar / Eliminar
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                ModifyButton(
                    onClick = onEditClick,
                )
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center,
            ) {
                val isEnabled = collab.estatus == 1

                Box(
                    modifier = if (!isEnabled) Modifier.alpha(0.4f) else Modifier
                ) {
                    DeleteButton(
                        onClick = {
                            if (isEnabled) onDeleteClick()
                        },
                        enabled = true,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

    }
}

/**
 * Formats an ISO 8601 date string to a human-readable DD/MM/YYYY format.
 *
 * This helper function transforms date strings from the backend's ISO format
 * (YYYY-MM-DDTHH:mm:ss or YYYY-MM-DD) into a more user-friendly format suitable
 * for display in the UI. It handles the common ISO timestamp format by extracting
 * only the date portion before the 'T' separator.
 *
 * The function includes error handling to gracefully manage malformed date strings
 * by returning the original input, ensuring the UI doesn't break due to unexpected
 * date formats.
 *
 * @param dateString The ISO 8601 formatted date string to format, or null if no date is available.
 * @return A formatted date string in DD/MM/YYYY format, the original string if formatting
 *         fails, or null if the input was null.
 */
fun formatDate(dateString: String?): String? =
    try {
        val parts = dateString?.split("T")[0]?.split("-")
        "${parts?.get(2)}/${parts?.get(1)}/${parts?.get(0)}"
    } catch (e: Exception) {
        dateString
    }

/**
 * Converts a role ID to its corresponding human-readable Spanish name.
 *
 * This helper function maps the numeric role identifiers used in the backend
 * to user-friendly role names displayed throughout the UI. It provides a centralized
 * location for role name mapping, ensuring consistency across the application.
 *
 * The function supports the three main role types in the system:
 * - Role ID "1": Coordinador (Coordinator) - Administrative oversight role
 * - Role ID "2": Asistente (Assistant) - Support staff role
 * - Role ID "3": Becario (Volunteer/Intern) - Volunteer or intern role with attendance tracking
 *
 * @param roleId The role identifier string from the backend, or null if unavailable.
 * @return The Spanish name of the role, or "Desconocido" (Unknown) if the role ID
 *         doesn't match any known roles or is null.
 */
fun getRoleName(roleId: String?): String =
    when (roleId) {
        "1" -> "Coordinador"
        "2" -> "Asistente"
        "3" -> "Becario"
        else -> "Desconocido"
    }

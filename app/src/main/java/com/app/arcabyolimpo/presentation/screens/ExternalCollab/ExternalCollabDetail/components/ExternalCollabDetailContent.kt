package com.app.arcabyolimpo.presentation.screens.ExternalCollab.ExternalCollabDetail.components

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
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.DeleteButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ModifyButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.ActiveStatus
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.InactiveStatus

@Composable
fun ExternalCollabDetailContent(
    collab: com.app.arcabyolimpo.domain.model.ExternalCollaborator.ExternalCollab,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Profile Photo
        if (collab.photoUrl != null) {
            AsyncImage(
                model = collab.photoUrl,
                contentDescription = "Profile Photo",
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        } else {
            // Placeholder if no photo
            Box(
                modifier = Modifier
                    .size(120.dp)
                    .clip(CircleShape)
                    .background(Color(0xFF1E293B)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "${collab.firstName.firstOrNull() ?: ""}${collab.lastName.firstOrNull() ?: ""}",
                    color = Color.White,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Full Name
        Text(
            text = "${collab.firstName} ${collab.lastName} ${collab.secondLastName}",
            color = Color.White,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Status Badge
        if (collab.isActive) {
            ActiveStatus()
        } else {
            InactiveStatus()
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Information Cards
        InfoCard(label = "Correo Electrónico", value = collab.email)
        InfoCard(label = "Teléfono", value = collab.phone)
        InfoCard(label = "Carrera", value = collab.degree)
        InfoCard(label = "Fecha de Nacimiento", value = formatDate(collab.birthDate))
        InfoCard(label = "ID de Rol", value = getRoleName(collab.roleId))

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                ModifyButton(
                    onClick = onEditClick,
                )
            }

            Box(
                modifier = Modifier.weight(1f),
                contentAlignment = Alignment.Center
            ) {
                DeleteButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(width = 140.dp, height = 40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
    }
}

// Helper function to format date
fun formatDate(dateString: String): String {
    return try {
        val parts = dateString.split("T")[0].split("-")
        "${parts[2]}/${parts[1]}/${parts[0]}"
    } catch (e: Exception) {
        dateString
    }
}

// Helper function to get role name from ID
fun getRoleName(roleId: Int): String {
    return when (roleId) {
        1 -> "Coordinador"
        2 -> "Asistente"
        3 -> "Becario"
        else -> "Desconocido"
    }
}
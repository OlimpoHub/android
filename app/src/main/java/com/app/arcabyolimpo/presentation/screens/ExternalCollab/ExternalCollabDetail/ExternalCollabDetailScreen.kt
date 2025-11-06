package com.app.arcabyolimpo.presentation.screens.ExternalCollab.ExternalCollabDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.ActiveStatus
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.InactiveStatus

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExternalCollabDetailScreen(
    viewModel: ExternalCollabDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        containerColor = Color(0xFF040610),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalles del Usuario",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF040610)
                ),
                actions = {
                    uiState.collab?.id?.let { id ->
                        IconButton(onClick = { onEditClick(id) }) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = "Edit",
                                tint = Color.White
                            )
                        }
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }

                uiState.error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: ${uiState.error}",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadCollabDetail() }) {
                            Text("Reintentar")
                        }
                    }
                }

                uiState.collab != null -> {
                    ExternalCollabDetailContent(collab = uiState.collab!!)
                }
            }
        }
    }
}

@Composable
fun ExternalCollabDetailContent(collab: com.app.arcabyolimpo.domain.model.ExternalCollaborator.ExternalCollab) {
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
        InfoCard(label = "ID de Rol", value = collab.roleId.toString())
    }
}

@Composable
fun InfoCard(label: String, value: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF1E293B)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = label,
                color = Color(0xFF94A3B8),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = value,
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Normal
            )
        }
    }
}

// Helper function to format date
fun formatDate(dateString: String): String {
    return try {
        // Parse the ISO date and format it nicely
        val parts = dateString.split("T")[0].split("-")
        "${parts[2]}/${parts[1]}/${parts[0]}"
    } catch (e: Exception) {
        dateString
    }
}
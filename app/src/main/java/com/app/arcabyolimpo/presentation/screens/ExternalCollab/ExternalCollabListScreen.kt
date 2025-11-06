package com.app.arcabyolimpo.presentation.screens.ExternalCollab

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.ActiveStatus
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.InactiveStatus
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.SearchIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SearchInput

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExternalCollabListScreen(
    viewModel: ExternalCollabListViewModel = hiltViewModel(),
    onCollabClick: (String) -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Color(0xFF040610),
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // First row: Title and Notification
                TopAppBar(
                    title = {
                        Text(
                            "Usuarios",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF040610)
                    ),
                    actions = {
                        IconButton(onClick = { /* TODO: Notifications action */ }) {
                            NotificationIcon()
                        }
                    }
                )

                // Second row: Search input and Filter button
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    SearchInput(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = "Buscar",
                        modifier = Modifier.weight(1f),
                        trailingIcon = {
                            SearchIcon(tint = Color.White)
                        }
                    )

                    IconButton(
                        onClick = { /* TODO: Filter action */ },
                        modifier = Modifier.size(30.dp)
                    ) {
                        FilterIcon()
                    }
                }
            }
        },
        bottomBar = {
            NavBar()
        },
        floatingActionButton = {
            AddButton(
                onClick = { /* TODO: Navigate to add collab screen */ }
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
                        modifier = Modifier.align(Alignment.Center)
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
                        Button(onClick = { viewModel.loadCollabs() }) {
                            Text("Retry")
                        }
                    }
                }

                uiState.collabs.isEmpty() -> {
                    Text(
                        text = "No collaborators found",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        // "Voluntarios" header
                        item {
                            Text(
                                text = "Voluntarios",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        // List of collaborators
                        items(uiState.collabs) { collab ->
                            ExternalCollabCard(
                                collab = collab,
                                onClick = { collab.id?.let { onCollabClick(it) } }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ExternalCollabCard(
    collab: com.app.arcabyolimpo.domain.model.ExternalCollaborator.ExternalCollab,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF040610)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Name
            Text(
                text = "${collab.firstName} ${collab.lastName} ${collab.secondLastName}",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )

            // Status badge - Active or Inactive
            if (collab.isActive) {
                ActiveStatus(
                    modifier = Modifier.padding(0.dp)
                )
            } else {
                InactiveStatus(
                    modifier = Modifier.padding(0.dp)
                )
            }
        }
    }
}
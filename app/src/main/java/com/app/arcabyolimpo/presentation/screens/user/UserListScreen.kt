package com.app.arcabyolimpo.presentation.screens.user

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.HorizontalDivider
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.SearchIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SearchInput
import com.app.arcabyolimpo.presentation.screens.user.components.UserCard
import com.app.arcabyolimpo.presentation.screens.user.register.UserRegisterScreen
import com.app.arcabyolimpo.presentation.ui.components.organisms.Filter
import com.app.arcabyolimpo.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    viewModel: UserListViewModel = hiltViewModel(),
    onCollabClick: (String) -> Unit,
    onAddClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showRegisterModal by remember { mutableStateOf(false) }
    var showFilter by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.getUsers()
    }

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
                        onClick = { showFilter = true },
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
                onClick = { showRegisterModal = true }
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
                        Button(onClick = { viewModel.getUsers() }) {
                            Text("Reintentar")
                        }
                    }
                }

                uiState.users.isEmpty() -> {
                    Text(
                        text = "No usuarios encontrados",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center),
                        style = MaterialTheme.typography.bodyLarge
                    )
                }

                searchQuery.isEmpty() -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        // "Asistentes" header
                        item {
                            Text(
                                text = "Asistentes",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(vertical = 8.dp)
                            )
                        }

                        // List of collaborators
                        items(uiState.users) { user ->
                            if (user.idRol == "2") {
                                UserCard (
                                    user = user,
                                    onClick = { user.idUsuario?.let { onCollabClick(it) } }
                                )
                            }
                        }

                        item {
                            HorizontalDivider(
                                thickness = 0.3.dp,
                                color = White,
                                modifier =
                                    Modifier
                                    .padding(vertical = 8.dp)
                                    .padding(end = 8.dp)
                            )
                        }

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
                        items(uiState.users) { user ->
                            if (user.idRol == "3") {
                                UserCard (
                                    user = user,
                                    onClick = { user.idUsuario?.let { onCollabClick(it) } }
                                )
                            }
                        }
                    }
                }

                else -> {

                }
            }
        }
        if (showRegisterModal) {
            UserRegisterScreen(
                onDismiss = { showRegisterModal = false },
                onSuccess = {
                    showRegisterModal = false
                    viewModel.getUsers()  // Refresh list
                }
            )
        }
    }
    // SupplyListScreen
    if (showFilter) {
        Filter(
            data = uiState.filterData,
            initialSelected = uiState.selectedFilters,
            onApply = { dto ->
                showFilter = false
                viewModel.applyFilters(dto)
            },
            onDismiss = { showFilter = false },
            onClearFilters = {
                viewModel.clearFilters()
                showFilter = false
            },
        )
    }
}
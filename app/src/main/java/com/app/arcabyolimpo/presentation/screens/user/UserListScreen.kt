@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.app.arcabyolimpo.presentation.screens.user

import androidx.compose.foundation.background
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
import androidx.compose.ui.unit.max
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
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.PlaceholderGray
import com.app.arcabyolimpo.ui.theme.White

/**
 * Composable screen that displays the list of users (asistentes and voluntarios).
 *
 * This screen handles:
 * - Fetching and displaying users through [UserListViewModel].
 * - Searching, filtering, and categorizing users using tabs.
 * - Showing loading and error states.
 * - Allowing registration of new users via a modal.
 *
 * UI features:
 * - A top bar with title, notification, and search/filter controls.
 * - A tab layout to switch between user roles.
 * - A lazy column that lists user cards with dividers.
 * - A floating action button to add a new user.
 * - A bottom navigation bar.
 *
 * @param viewModel The [UserListViewModel] that manages the UI state and user data.
 * @param onCollabClick Callback triggered when a user card is clicked, receiving the user ID.
 * @param onAddClick Callback triggered when the add button is pressed.
 */

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    viewModel: UserListViewModel = hiltViewModel(),
    onCollabClick: (String) -> Unit,
    onAddClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsState()
    var searchQuery by remember { mutableStateOf("") }
    var showRegisterModal by remember { mutableStateOf(false) }
    var showFilter by remember { mutableStateOf(false) }
    val tabs = listOf("Asistentes", "Voluntarios")
    var selectedTabIndex by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel.getUsers()
    }

    Scaffold(
        containerColor = Color(0xFF040610),
        topBar = {
            Column(
                modifier = Modifier.fillMaxWidth(),
            ) {
                // First row: Title and Notification
                TopAppBar(
                    title = {
                        Text(
                            "Usuarios",
                            color = Color.White,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                        )
                    },
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = Color(0xFF040610),
                        ),
                    actions = {
                        IconButton(onClick = { /* TODO: Notifications action */ }) {
                            NotificationIcon()
                        }
                    },
                )

                // Second row: Search input and Filter button
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    SearchInput(
                        value = searchQuery,
                        onValueChange = {
                            searchQuery = it
                            viewModel.searchUsers(searchQuery)
                        },
                        placeholder = "Buscar",
                        modifier = Modifier.weight(1f),
                        trailingIcon = {
                            SearchIcon(tint = Color.White)
                        },
                    )

                    IconButton(
                        onClick = { showFilter = true },
                        modifier = Modifier.size(30.dp),
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
                onClick = { showRegisterModal = true },
            )
        },
    ) { padding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                    )
                }

                uiState.error != null -> {
                    Column(
                        modifier =
                            Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Error: ${uiState.error}",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge,
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
                        style = MaterialTheme.typography.bodyLarge,
                    )
                }

                else -> {
                    Column(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(Background),
                    ) {
                        PrimaryTabRow(
                            selectedTabIndex = selectedTabIndex,
                            containerColor = Background,
                            divider = {
                                HorizontalDivider(
                                    thickness = 2.dp,
                                    color = PlaceholderGray,
                                )
                            },
                            indicator = {
                                TabRowDefaults.PrimaryIndicator(
                                    modifier =
                                        Modifier
                                            .tabIndicatorOffset(selectedTabIndex),
                                    width = 220.dp,
                                    height = 2.dp,
                                    color = White,
                                )
                            },
                        ) {
                            tabs.forEachIndexed { index, title ->
                                Tab(
                                    text = {
                                        if (selectedTabIndex == index) {
                                            Text(
                                                text = title,
                                                color = White,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(vertical = 8.dp),
                                            )
                                        } else {
                                            Text(
                                                text = title,
                                                color = PlaceholderGray,
                                                fontSize = 20.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.padding(vertical = 8.dp),
                                            )
                                        }
                                    },
                                    selected = selectedTabIndex == index,
                                    onClick = { selectedTabIndex = index },
                                )
                            }
                        }
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            contentPadding = PaddingValues(16.dp),
                        ) {
                            when (selectedTabIndex) {
                                0 -> {
                                    // List of collaborators
                                    items(uiState.users) { user ->
                                        if (user.idRol == "2") {
                                            UserCard (
                                                user = user,
                                                onClick = { user.idUsuario?.let { onCollabClick(it) } }
                                            )

                                            Box(
                                                modifier = Modifier.fillMaxWidth(),
                                                contentAlignment = Alignment.Center,
                                            ) {
                                                HorizontalDivider(
                                                    thickness = 0.3.dp,
                                                    color = White,
                                                    modifier =
                                                        Modifier
                                                            .padding(vertical = 8.dp)
                                                            .fillMaxWidth(0.95f),
                                                )
                                            }
                                        }
                                    }
                                }

                                1 -> {
                                    // Colaboradores
                                    items(uiState.users) { user ->
                                        if (user.idRol == "3") {
                                            UserCard (
                                                user = user,
                                                onClick = { user.idUsuario?.let { onCollabClick(it) } }
                                            )
                                            Box(
                                                modifier = Modifier.fillMaxWidth(),
                                                contentAlignment = Alignment.Center,
                                            ) {
                                                HorizontalDivider(
                                                    thickness = 0.3.dp,
                                                    color = White,
                                                    modifier =
                                                        Modifier
                                                            .padding(vertical = 8.dp)
                                                            .fillMaxWidth(0.95f),
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        if (showRegisterModal) {
            UserRegisterScreen(
                onDismiss = { showRegisterModal = false },
                onSuccess = {
                    showRegisterModal = false
                    viewModel.getUsers() // Refresh list
                },
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
                viewModel.searchUsers(searchQuery)
            },
            onDismiss = { showFilter = false },
            onClearFilters = {
                viewModel.clearFilters()
                showFilter = false
            },
        )
    }
}

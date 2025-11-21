package com.app.arcabyolimpo.presentation.screens.workshop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.presentation.navigation.Screen
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.Snackbarcustom
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SearchInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.WorkshopCard
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.presentation.ui.components.organisms.Filter
import com.app.arcabyolimpo.ui.theme.Background
import kotlinx.coroutines.launch
import com.app.arcabyolimpo.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkshopsListScreen(
    navController: NavHostController,
    workshopClick: (String) -> Unit,
    viewModel: WorkshopsListViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val searchText by viewModel.searchQuery.collectAsState()
    var showFilter by remember { mutableStateOf(false) }

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val snackbarMessage = savedStateHandle?.get<String>("snackbarMessage")
    val snackbarSuccess = savedStateHandle?.get<Boolean>("snackbarSuccess") ?: true

    LaunchedEffect(Unit) {
        viewModel.loadWorkshopsList()
    }

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
            }
            savedStateHandle.remove<String>("snackbarMessage")
            savedStateHandle.remove<Boolean>("snackbarSuccess")
        }
    }

    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                containerColor = Background,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Talleres",
                                style = MaterialTheme.typography.headlineLarge,
                                color = White,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    )
                },
                floatingActionButton = {
                    AddButton(
                        onClick = { navController.navigate(Screen.AddNewWorkshop.route) }
                    )
                }
            ) { padding ->

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = padding.calculateTopPadding(),
                            bottom = padding.calculateBottomPadding()
                        ),
                    verticalArrangement = Arrangement.spacedBy(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    item {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            SearchInput(
                                value = searchText,
                                onValueChange = { viewModel.onSearchQueryChange(it) },
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(end = 20.dp)
                            )

                            IconButton(
                                onClick = { showFilter = true },
                                modifier = Modifier.size(30.dp)
                            ) {
                                FilterIcon()
                            }
                        }
                    }

                    when {
                        uiState.isLoading -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        uiState.error != null -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = uiState.error ?: "Error desconocido",
                                        color = MaterialTheme.colorScheme.error
                                    )
                                }
                            }
                        }

                        uiState.workshopsList.isNullOrEmpty() -> {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 40.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "No hay talleres registrados")
                                }
                            }
                        }

                        else -> {
                            items(uiState.workshopsList) { workshop ->
                                WorkshopCard(
                                    name = workshop.nameWorkshop.toString(),
                                    onClick = { workshopClick(workshop.id.toString()) }
                                )
                            }
                        }
                    }
                }
            }

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

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp)
                    .fillMaxWidth()
            ) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) { data ->
                    Snackbarcustom(
                        title = data.visuals.message,
                        modifier = Modifier.fillMaxWidth(),
                        ifSucces = snackbarSuccess
                    )
                }
            }
        }
    }
}

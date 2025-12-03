@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.app.arcabyolimpo.presentation.screens.qr.workshopselection

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ReturnIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SearchInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.presentation.ui.components.molecules.WorkshopCard
import com.app.arcabyolimpo.presentation.ui.components.organisms.Filter
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background

/**
 * Screen that displays the list of available workshops and allows the user to select
 * one in order to proceed with the QR-based attendance flow.
 *
 * This screen connects to [QrWorkshopsListViewModel] to fetch and observe workshop data,
 * applies search-based filtering, and renders the results in a scrollable list. It includes
 * a top bar with navigation controls and uses theming consistent with the app’s dark mode.
 *
 * ## Atomic Design Level
 * **Organism** — Composed of multiple UI units:
 * - Atoms: `ReturnIcon`, `SearchInput`, `FilterIcon`, text labels
 * - Molecules: Search bar row, individual `WorkshopCard` items
 * - Organism: Full workshop selection flow (search → list → action)
 *
 * ## Behavior
 * - Loads the list of workshops on initialization via the ViewModel.
 * - Allows text-based filtering through the search input.
 * - Displays loading, empty, and error states with appropriate UI feedback.
 * - Emits the selected workshop through [workshopClick], providing its ID and name.
 *
 * @param onBackClick Action executed when the back button in the top app bar is pressed.
 * @param workshopClick Callback invoked when the user taps a workshop card (workshopId, workshopName).
 * @param viewModel Injected [QrWorkshopsListViewModel] providing workshop data and search state.
 */

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrWorkshopsListScreen(
    onBackClick: () -> Unit,
    workshopClick: (String, String) -> Unit,
    viewModel: QrWorkshopsListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchText by viewModel.searchQuery.collectAsState()
    var showFilter by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadWorkshopsList()
    }

    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                containerColor = Background,
                topBar = {
                    TopAppBar(
                        title = {
                            Column(
                                modifier =
                                    Modifier
                                        .padding(vertical = 15.dp)
                                        .padding(end = 20.dp),
                            ) {
                                Text(
                                    text = "Selecciona un taller para la asistencia",
                                    style = Typography.headlineLarge,
                                    color = Color.White,
                                )
                            }
                        },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                ReturnIcon()
                            }
                        },
                    )
                },
            ) { padding ->
                LazyColumn(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                top = padding.calculateTopPadding(),
                                bottom = padding.calculateBottomPadding(),
                            ),
                    verticalArrangement = Arrangement.spacedBy(30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    item {
                        Row(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 30.dp)
                                    .padding(top = 20.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            SearchInput(
                                value = searchText,
                                onValueChange = { viewModel.onSearchQueryChange(it) },
                                modifier =
                                    Modifier
                                        .weight(1f)
                                        .padding(end = 20.dp),
                            )

                            IconButton(
                                onClick = { showFilter = true },
                                modifier = Modifier.size(30.dp),
                            ) {
                                FilterIcon()
                            }
                        }
                    }

                    when {
                        uiState.isLoading -> {
                            item {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(top = 40.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    CircularProgressIndicator()
                                }
                            }
                        }

                        uiState.error != null -> {
                            item {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(top = 40.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(
                                        text = uiState.error ?: "Error desconocido",
                                        color = MaterialTheme.colorScheme.error,
                                    )
                                }
                            }
                        }

                        uiState.workshopsList.isNullOrEmpty() -> {
                            item {
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(top = 40.dp),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Text(text = "No hay talleres registrados")
                                }
                            }
                        }

                        else -> {
                            items(uiState.workshopsList) { workshop ->
                                WorkshopCard(
                                    name = workshop.nameWorkshop.toString(),
                                    imageUrl = workshop.url?.toString(),
                                    onClick = { workshopClick(workshop.id.toString(), workshop.nameWorkshop.toString()) },
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
        }
    }
}

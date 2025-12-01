package com.app.arcabyolimpo.presentation.screens.beneficiary

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.data.remote.dto.filter.FilterDto
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SearchInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.BeneficiaryCard
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.presentation.ui.components.organisms.Filter
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.presentation.navigation.Screen
import com.app.arcabyolimpo.presentation.screens.session.SessionViewModel
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.Snackbarcustom
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ReturnIcon
import com.app.arcabyolimpo.ui.theme.White
import kotlinx.coroutines.launch

/**
 * This composable acts as the main screen, inecting the ViewModel
 * and collecting the UI state to pass to the stateless UI.
 */
@Composable
fun BeneficiaryListScreen(
    navController: NavHostController,
    onBeneficiaryClick: (String) -> Unit,
    onFilterClick: () -> Unit,
    onNotificationClick: () -> Unit,
    viewModel: BeneficiaryListViewModel = hiltViewModel(),
) {

    val state by viewModel.uiState.collectAsState()
    val filterState by viewModel.uiFiltersState.collectAsState()

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val successMessage = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<String>("success_message")

    LaunchedEffect(Unit) {
        viewModel.getBeneficiaries()
    }

    LaunchedEffect(successMessage) {
        successMessage?.let { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<String>("success_message")
        }
    }

    val errorMessage = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<String>("error_message")

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<String>("error_message")
        }
    }
    var selectedOption by rememberSaveable { mutableStateOf<String?>(null) }

    BeneficiaryList(
        navController = navController,
        state = state,
        filterState = filterState,
        snackbarHostState = snackbarHostState,
        onSearchTextChange = viewModel::onSearchTextChange,
        onBeneficiaryClick = onBeneficiaryClick,
        onFilterClick = onFilterClick,
        onApplyFilters = viewModel::filterBeneficiary,
        onClearFilters = {
            viewModel.clearFilters()
            viewModel.getBeneficiaries()
        },
        onNotificationClick = onNotificationClick,
        onBackClick = {
            navController.navigate(Screen.CoordinatorHome.route)
        },
        onAddBeneficiaryClick = {
            navController.navigate(Screen.AddNewBeneficiary.route)
        },

    )
}

/**
 * This is the stateless UI composable for the Beneficiary List.
 * It only receives state and hoists events.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiaryList(
    navController: NavHostController,
    state: BeneficiaryListUiState,
    filterState: BeneficiaryFilterUiState,
    snackbarHostState: SnackbarHostState,
    onSearchTextChange: (String) -> Unit,
    onBeneficiaryClick: (String) -> Unit,
    onFilterClick: () -> Unit,
    onNotificationClick: () -> Unit,
    onApplyFilters: (FilterDto) -> Unit,
    onClearFilters: () -> Unit,
    onAddBeneficiaryClick: () -> Unit,
    onBackClick: () -> Unit,
    sessionViewModel: SessionViewModel = hiltViewModel(),
) {
    val role by sessionViewModel.role.collectAsState()

    var showFilter by remember { mutableStateOf(false) }

    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                containerColor = Background,

                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Beneficiarios",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                            )
                        }
                    )
                },
                floatingActionButton = {
                    if (role == "COORDINADOR") {
                        AddButton(onClick = onAddBeneficiaryClick)
                    }
                }
            ) { padding ->
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                top = padding.calculateTopPadding(),
                                bottom = padding.calculateBottomPadding(),
                            ),
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    start = 30.dp,
                                    end = 30.dp,
                                    bottom = 12.dp,
                                ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        SearchInput(
                            value = state.searchText,
                            onValueChange = onSearchTextChange,
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .padding(end = 20.dp),
                        )

                        FilterIcon(
                            modifier =
                                Modifier
                                    .padding(start = 16.dp)
                                    .clickable { showFilter = true },
                        )
                    }

                    val hasActiveFilters = filterState.selectedFilters.filters.any { it.value.isNotEmpty() }

                    val isDefaultOrder =
                        filterState.selectedFilters.order.isNullOrBlank() ||
                            filterState.selectedFilters.order == "ASC"

                    val hasOrderOnly = !hasActiveFilters && !isDefaultOrder

                    val listToShow =
                        when {
                            hasActiveFilters -> filterState.result ?: emptyList()
                            hasOrderOnly -> filterState.result ?: state.beneficiaries
                            else -> state.beneficiaries
                        }

                    // println("ACTIVE FILTERS: " + hasActiveFilters)

                    if (state.isLoading || state.error != null) {
                        // Loading o error
                        CircularProgressIndicator(
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally),
                        )
                    } else if (listToShow.isEmpty()) {
                        // Lista vacía
                        Text(
                            text = "No se encontraron beneficiarios",
                            color = Color.Red,
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally),
                        )
                    } else {
                        // Lista de tarjetas
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(vertical = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxSize().weight(1f),
                        ) {
                            items(listToShow, key = { it.id }) { beneficiary ->
                                BeneficiaryCard(
                                    name = beneficiary.name,
                                    onClick = { onBeneficiaryClick(beneficiary.id) },
                                    cardModifier =
                                        Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp)
                                            .height(160.dp),
                                    contentPadding =
                                        PaddingValues(
                                            vertical = 20.dp,
                                            horizontal = 40.dp,
                                        ),
                                )
                            }
                        }
                    }

                    /** when {
                     state.isLoading || state.isLoading -> {
                     CircularProgressIndicator(
                     modifier =
                     Modifier
                     .fillMaxWidth()
                     .padding(top = 20.dp)
                     .wrapContentWidth(Alignment.CenterHorizontally),
                     )
                     }
                     state.error != null -> {
                     Text(
                     text = state.error ?: "Error",
                     color = Color.Red,
                     modifier =
                     Modifier
                     .fillMaxWidth()
                     .wrapContentWidth(Alignment.CenterHorizontally),
                     )
                     }
                     else -> {
                     LazyVerticalGrid(
                     columns = GridCells.Fixed(2),
                     contentPadding = PaddingValues(vertical = 16.dp),
                     horizontalArrangement = Arrangement.spacedBy(8.dp),
                     verticalArrangement = Arrangement.spacedBy(8.dp),
                     modifier =
                     Modifier
                     .fillMaxSize()
                     .weight(1f),
                     ) {
                     items(
                     items = filteredList,
                     key = { it.id },
                     ) { beneficiary ->
                     BeneficiaryCard(
                     name = beneficiary.name,
                     onClick = { onBeneficiaryClick(beneficiary.id) },
                     cardModifier =
                     Modifier
                     .fillMaxWidth()
                     .padding(horizontal = 8.dp)
                     .height(160.dp),
                     contentPadding =
                     PaddingValues(
                     vertical = 20.dp,
                     horizontal = 40.dp,
                     ),
                     )
                     }
                     }
                     }
                     }*/
                }
            }

            if (showFilter && filterState.filterData != null) {
                println("ENtro al filter despues" + filterState.filterData)
                Filter(
                    data = filterState.filterData!!,
                    initialSelected = filterState.selectedFilters,
                    onApply = { dto ->
                        onApplyFilters(dto)
                        showFilter = false
                    },
                    onDismiss = { showFilter = false },
                    onClearFilters = {
                        onClearFilters()
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
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}

/*
@Preview(showBackground = true, backgroundColor = 0xFF1C1B1F)
@Composable
fun BeneficiaryListPreview() {
    val navController = androidx.navigation.compose.rememberNavController()

    // Mock state
    val previewState =
        BeneficiaryListUiState(
            beneficiaries =
                listOf(
                    Beneficiary("1", "John Smith 1", "", "", "", "", "", "", "", "", 1),
                    Beneficiary("2", "John Smith 2", "", "", "", "", "", "", "", "", 1),
                    Beneficiary("3", "John Smith 3", "", "", "", "", "", "", "", "", 1),
                    Beneficiary("4", "John Smith 4", "", "", "", "", "", "", "", "", 1),
                ),
        )

    ArcaByOlimpoTheme {
        BeneficiaryList(
            navController = navController,
            state = previewState,
            onSearchTextChange = {},
            onBeneficiaryClick = {},
            onFilterClick = {},
        ),
            onNotificationClick = {},
            onAddBeneficiaryClick = {}
        )
    }
}*/

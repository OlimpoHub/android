package com.app.arcabyolimpo.presentation.screens.supply

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.domain.model.supplies.FilterData
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.SearchIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardIconInput
import com.app.arcabyolimpo.presentation.ui.components.organisms.Filter
import com.app.arcabyolimpo.presentation.ui.components.organisms.SupplyListContent
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White

/**
 * Composable screen that displays the list of supplies.
 *
 * This screen is responsible for showing a list of available supplies
 * retrieved from the [SuppliesListViewModel]. It provides:
 * - A top bar with the title and notification icon.
 * - A floating action button for adding new supplies (action pending).
 * - A content area showing the supply list with loading and error states.
 *
 * @param onSupplyClick Callback triggered when a supply item is clicked.
 * Receives the supply ID as a parameter.
 * @param viewModel The [SuppliesListViewModel] used to manage the UI state.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplyListScreen(
    onSupplyClick: (String) -> Unit,
    viewModel: SuppliesListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var showFilter by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Background,
        floatingActionButton = {
            AddButton(
                onClick = { /* Action for add button */ },
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Insumos",
                        color = White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                    )
                },
                actions = {
                    NotificationIcon(
                        modifier =
                            Modifier
                                .padding(horizontal = 24.dp)
                                .size(28.dp),
                    )
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Background,
                    ),
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
                    StandardIconInput(
                        label = "",
                        placeholder = "Buscar",
                        value = "",
                        onValueChange = { },
                        trailingIcon = { SearchIcon() },
                        modifier =
                            Modifier
                                .weight(1f)
                                .height(74.dp),
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                FilterIcon(
                    modifier =
                        Modifier
                            .size(40.dp)
                            .padding(top = 16.dp)
                            .clickable {
                                showFilter = true
                            },
                )
            }

            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                SupplyListContent(
                    suppliesList = uiState.suppliesList,
                    isLoading = uiState.isLoading,
                    error = uiState.error,
                    onSupplyClick = onSupplyClick,
                    onRetry = { viewModel.loadSuppliesList() },
                    modifier = Modifier.fillMaxSize(),
                )
            }
        }
    }
    // SupplyListScreen
    if (showFilter && uiState.filterData != null) {
        Filter(
            data = uiState.filterData!!,
            onApply = { dto ->
                viewModel.filterSupplies(dto)
                showFilter = false
            },
            onDismiss = { showFilter = false },
            onClearFilters = {
                viewModel.loadSuppliesList() // recarga todos los insumos
                showFilter = false
            },
        )
    }
}

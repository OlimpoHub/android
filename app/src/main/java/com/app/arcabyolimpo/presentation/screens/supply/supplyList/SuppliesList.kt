package com.app.arcabyolimpo.presentation.screens.supply.supplyList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ReturnIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.SearchIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardIconInput
import com.app.arcabyolimpo.presentation.ui.components.organisms.Filter
import com.app.arcabyolimpo.presentation.ui.components.organisms.SupplyListContent
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.ErrorRed
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
 * @param onAddSupplyClick Callback triggered when the add button is clicked.
 * @param viewModel The [SuppliesListViewModel] used to manage the UI state.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplyListScreen(
    onSupplyClick: (String) -> Unit,
    onBackClick: () -> Unit,
    onAddSupplyClick: () -> Unit,
    viewModel: SuppliesListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val lifecycleOwner = LocalLifecycleOwner.current

    // Reload data when view is on
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadSuppliesList()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // State variable to track whether the filter modal is visible
    var showFilter by remember { mutableStateOf(false) }

    // State variable to track the current text entered by the user in the search field
    var searchQuery by remember { mutableStateOf("") }

    /**
     * List of supplies filtered based on the user's search input.
     *
     * @param searchQuery Text entered by the user in the search field.
     * @param uiState.suppliesList Complete list of supplies loaded in the UI.
     */
    val filteredSupplies =
        remember(searchQuery, uiState.suppliesList) {
            if (searchQuery.isEmpty()) {
                uiState.suppliesList
            } else {
                uiState.suppliesList.filter { supply ->
                    supply.name.contains(searchQuery, ignoreCase = true) ||
                        supply.id.contains(searchQuery)
                }
            }
        }

    Scaffold(
        containerColor = Background,
        floatingActionButton = {
            AddButton(
                onClick = onAddSupplyClick,
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
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Back",
                            tint = White,
                        )
                    }
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
                    /**
                     * Text input field with an icon, used to search supplies.
                     *
                     * This component allows the user to type text to filter the list of supplies
                     * on the screen. It updates automatically via the searchQuery state.
                     *
                     * Main parameters:
                     * @param label Optional label text for the field (empty here).
                     * @param placeholder Text shown when the field is empty ("Buscar").
                     * @param value Current value of the field, bound to searchQuery.
                     * @param onValueChange Callback executed whenever the user types something.
                     *                      Updates the [searchQuery] state.
                     * @param trailingIcon Icon displayed at the end of the text field (search icon).
                     * @param modifier Compose modifier to define size, weight, padding, etc.
                     */
                    StandardIconInput(
                        label = "",
                        placeholder = "Buscar",
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        trailingIcon = { SearchIcon() },
                        modifier =
                            Modifier
                                .weight(1f)
                                .height(74.dp),
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                /**
                 * Icon button used to open the filter panel for supplies.
                 * When clicked, it sets showFilter to `true`, triggering the display
                 * of the filter UI.
                 *
                 * @param modifier Compose Modifier used to define size, padding, and click behavior.
                 */
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
                /**
                 * Displays the main content of the supply list screen based on the current UI state.
                 *
                 * The content is shown according to the following conditions:
                 * 1. If the data is loading or there is an error,
                 * 2. If the filtered list is empty
                 * 3. Otherwise, the composable displays the filtered list normally,
                 *
                 * @param filteredSupplies List of supplies filtered by the search query.
                 * @param uiState Current UI state of the supply list screen.
                 * @param onSupplyClick Callback triggered when a supply item is clicked.
                 * @param viewModel ViewModel used to reload the supply list on retry.
                 */

                if (uiState.isLoading || uiState.error != null) {
                    SupplyListContent(
                        suppliesList = filteredSupplies,
                        isLoading = uiState.isLoading,
                        error = uiState.error,
                        onSupplyClick = onSupplyClick,
                        onRetry = { viewModel.loadSuppliesList() },
                        modifier = Modifier.fillMaxSize(),
                    )
                } else if (filteredSupplies.isEmpty()) {
                    Text(
                        text = "No se encontraron insumos",
                        color = ErrorRed,
                        style = Typography.headlineMedium,
                        modifier = Modifier.align(Alignment.Center),
                    )
                } else {
                    SupplyListContent(
                        suppliesList = filteredSupplies,
                        isLoading = false,
                        error = null,
                        onSupplyClick = onSupplyClick,
                        onRetry = { },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }
        }
    }

    /**
     * Displays the filter panel when showFilter is true and filter data is available.
     *
     * This composable allows the user to apply, dismiss, or clear filters on the supply list.
     *
     * Behavior:
     * - onApply: Called when the user applies a filter. Passes the selected filter DTO
     *   to the ViewModel via viewModel.filterSupplies and closes the filter panel.
     * - onDismiss: Called when the user dismisses the filter panel without applying changes.
     *   Simply closes the panel by setting showFilter to false.
     * - onClearFilters: Clears all selected filters in the ViewModel, reloads the full supply list,
     *   and closes the filter panel.
     *
     * Preconditions:
     * - uiState.filterData must not be null; otherwise, the filter panel is not displayed.
     */
    if (showFilter && uiState.filterData != null) {
        Filter(
            data = uiState.filterData!!,
            initialSelected = uiState.selectedFilters,
            onApply = { dto ->
                viewModel.filterSupplies(dto)
                showFilter = false
            },
            onDismiss = { showFilter = false },
            onClearFilters = {
                viewModel.clearFilters()
                viewModel.loadSuppliesList()
                showFilter = false
            },
        )
    }
}

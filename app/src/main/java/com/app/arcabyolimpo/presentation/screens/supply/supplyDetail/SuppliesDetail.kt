package com.app.arcabyolimpo.presentation.screens.supply.supplyDetail

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.domain.model.supplies.Batch
import com.app.arcabyolimpo.presentation.common.components.ErrorView
import com.app.arcabyolimpo.presentation.common.components.LoadingShimmer
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.DecisionDialog
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.SnackbarVisualsWithError
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.Snackbarcustom
import com.app.arcabyolimpo.presentation.ui.components.organisms.SupplyDetailContent
import com.app.arcabyolimpo.presentation.ui.components.organisms.Filter
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White
import kotlinx.coroutines.launch

/** ---------------------------------------------------------------------------------------------- *
 * SuppliesDeatilScreen -> receibes the id of the supply, then calls the view model tu update its
 * view, this is in charge of displaying the data fetched into the view. Also handles different
 * states.
 *
 * @param idInsumo: String -> ID of the supply
 * @param onBackClick: () -> Unit -> function when you want to go back
 * @param onClickAddSupplyBatch: () -> Unit -> function when you want to ADD A NEW SUPPLY
 * @param onClickDelete: () -> Unit -> function when you want to DELETE THE SUPPLY
 * @param onClickModify: () -> Unit -> function when you want to MODIFY THE SUPPLY
 * @param watchSupplyBatch: () -> Unit -> function when you want to MODIFY A SUPPLY BATCH (removed)
 * @param deleteSupplyBatch: () -> function when you want to DELETE A SUPPLY BATCH
 * @param viewModel: SuppliesDetailViewModel -> view model that gathers the data and updates the view
 * ---------------------------------------------------------------------------------------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuppliesDetailScreen(
    navController: NavHostController,
    idInsumo: String,
    onBackClick: () -> Unit,
    onClickAddSupplyBatch: () -> Unit,
    onClickDelete: () -> Unit,
    onClickModify: () -> Unit,
    onViewBatches: (String, String) -> Unit,
    deleteSupplyBatch: () -> Unit,
    viewModel: SuppliesDetailViewModel = hiltViewModel(),
) {
    fun toSqlDate(dateStr: String): String {
        if (dateStr.isBlank()) return ""
        return try {
            if (dateStr.contains('/')) {
                val parts = dateStr.split('/')
                if (parts.size == 3) {
                    val d = parts[0].toIntOrNull() ?: return dateStr
                    val m = parts[1].toIntOrNull() ?: return dateStr
                    val y = parts[2].toIntOrNull() ?: return dateStr
                    String.format("%04d-%02d-%02d", y, m, d)
                } else {
                    dateStr
                }
            } else {
                dateStr
            }
        } catch (e: Exception) {
            dateStr
        }
    }
    val state by viewModel.uiState.collectAsState()
    val filterState by viewModel.uiFiltersState.collectAsState()

    // State of the snackbar that will handle all messages on the screen.
    val snackbarHostState = remember { SnackbarHostState() }
    // We use it to launch the coroutine that displays the snackbar.
    val scope = rememberCoroutineScope()
    // We collect the state exposed by the ViewModel, reacting to changes
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    var showFilter by remember { mutableStateOf(false) }

    // In this case, if there is no error in the uiState,
    // we consider it a success.
    val haserror =
        if (uiState.error == null) {
            true
        } else {
            false
        }

    LaunchedEffect(idInsumo) {
        viewModel.getSupply(idInsumo)
    }

    // Effect triggered when the uiState.snackbarVisible flag changes.
    // If true, the snackbar is displayed and the system waits to dismiss it.
    LaunchedEffect(uiState.snackbarVisible) {

        if (uiState.snackbarVisible){
            // show snackbar as a suspend function
            scope.launch {
                val result =
                    snackbarHostState.showSnackbar(
                        SnackbarVisualsWithError(
                            uiState.snackbarMessage ?: "Operación completada",
                            isError = haserror,
                        ),
                    )
                // When the snack bar ends, we act according to the result
                when (result) {
                    SnackbarResult.Dismissed -> {
                        // Tell the VM that the visible snackbar is finished
                        viewModel.onSnackbarShown()

                        // Navigate back as soon as the snack bar is finished
                        if (uiState.deletionType == DeletionType.SUPPLY) {
                            onBackClick()
                        }
                    }
                    SnackbarResult.ActionPerformed -> {}
                }
            }
        }
    }

    // Read snackbar message passed from other screens via savedStateHandle (e.g., after register/modify)
    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val snackbarMessageFromNav = savedStateHandle?.get<String>("snackbarMessage")
    val snackbarSuccessFromNav = savedStateHandle?.get<Boolean>("snackbarSuccess") ?: true

    LaunchedEffect(snackbarMessageFromNav) {
        snackbarMessageFromNav?.let { msg ->
            scope.launch {
                snackbarHostState.showSnackbar(
                    SnackbarVisualsWithError(
                        message = msg,
                        isError = !snackbarSuccessFromNav,
                    ),
                )
            }
            savedStateHandle?.remove<String>("snackbarMessage")
            savedStateHandle?.remove<Boolean>("snackbarSuccess")
        }
    }

    // Displays a confirmation dialog when the user wants to delete an item.
    if (uiState.decisionDialogVisible) {
        val (title, text) = when (uiState.deletionType) {
            DeletionType.SUPPLY ->
                "¿Estás seguro de eliminar este Insumo?" to
                        "Esta acción no podrá revertirse y eliminará todos sus lotes"

                DeletionType.BATCH ->
                    "¿Estás seguro de eliminar este Lote?" to
                        "Esta acción no podrá revertirse"

                else ->
                    "¿Estás seguro?" to "Esta acción no podrá revertirse"
            }
        DecisionDialog(
            onDismissRequest = {
                viewModel.toggledecisionDialog(showdecisionDialog = false)
            },
            onConfirmation = {
                viewModel.confirmDeletion()
            },
            dialogTitle = title,
            dialogText = text,
            confirmText = "Confirmar",
            dismissText = "Cancelar",
        )
    }

    Scaffold(
        // Host del snackbar para la pantalla.
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                // Every time a snackbar is displayed, our custom component is called
                Snackbarcustom(
                    data.visuals.message,
                    modifier = Modifier,
                    // the type of snack bar (whether it's red or blue).
                    ifSucces = haserror,
                )
            }
        },
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text =
                            uiState
                                .supplyBatchList
                                ?.name ?: "Insumo",
                        color = White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
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
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
        ) {
            when {
                uiState.isLoading -> {
                    LoadingShimmer(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                    )
                }
                uiState.error != null -> {
                    ErrorView(
                        message = uiState.error ?: "Unknown Error",
                        onRetry = { viewModel.getSupply(idInsumo) },
                        modifier = Modifier.align(Alignment.Center),
                    )
                }
                uiState.supplyBatchList != null -> {
                    fun applyOrder(list: List<Batch>, order: String?): List<Batch> {
                        return when (order?.uppercase()) {
                            "ASC" -> list.sortedBy { it.quantity }
                            "DESC" -> list.sortedByDescending { it.quantity }
                            else -> list
                        }
                    }

                    val hasActiveFilters =
                        filterState.selectedFilters.filters.any { entry -> entry.value.isNotEmpty() }

                    val isDefaultOrder =
                        filterState.selectedFilters.order.isNullOrBlank() ||
                                filterState.selectedFilters.order == "ASC"

                    val hasOrderOnly = !hasActiveFilters && !isDefaultOrder

                    val supplyBatches = state.supplyBatchList?.batch ?: emptyList()
                    val filteredBatches = filterState.result

                    val listToShow = when {
                        hasActiveFilters -> applyOrder(filteredBatches, filterState.selectedFilters.order)
                        hasOrderOnly -> applyOrder(supplyBatches, filterState.selectedFilters.order)
                        else -> supplyBatches
                    }

                    val filteredShown =
                        if (hasActiveFilters || hasOrderOnly) listToShow else null

                    SupplyDetailContent(
                        supply = uiState.supplyBatchList!!,
                        onClickAddSupplyBatch = onClickAddSupplyBatch,
                        filteredBatches = filteredShown,
                        onClickDelete = {
                            // When you press delete in the details, we display the dialog
                            viewModel.toggledecisionDialog(
                                showdecisionDialog = true,
                                deletionType = DeletionType.SUPPLY,
                            )
                        },
                        onClickModify = onClickModify,
                        viewAllBatches = { date, supplyId ->
                            // convert incoming date (dd/MM/yyyy or other) to SQL yyyy-MM-dd before forwarding
                            val sqlDate = toSqlDate(date)
                            onViewBatches(sqlDate, supplyId)
                        },
                        deleteSupplyBatch = { batchId ->
                            viewModel.selectedBatchExpirationDate = batchId
                            viewModel.toggledecisionDialog(
                                showdecisionDialog = true,
                                deletionType = DeletionType.BATCH,
                            )
                        },
                        onFilterClick = { showFilter = true },
                    )
                }
            }
        }
    }

    if (showFilter && filterState.filterData != null) {
        Filter(
            data = filterState.filterData!!,
            initialSelected = filterState.selectedFilters,
            onApply = { dto ->
                viewModel.filterSupplyBatch(dto)
                showFilter = false
            },
            onDismiss = { showFilter = false },
            onClearFilters = {
                viewModel.clearFilters()
                showFilter = false
            },
        )
    }
}
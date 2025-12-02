@file:Suppress("ktlint:standard:import-ordering")

package com.app.arcabyolimpo.presentation.screens.supply.supplybatchregister

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
// Card removed as requested; layout uses plain Column now
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.ui.theme.ErrorRed
import com.app.arcabyolimpo.ui.theme.PrimaryBlue
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.CancelButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SaveButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SquareAddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SquareMinusButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SelectInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.SnackbarVisualsWithError
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.Snackbarcustom
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.DateInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.presentation.ui.components.organisms.SupplyBatchRegisterContent
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun SupplyBatchRegisterScreen(
    supplyId: String,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: SupplyBatchRegisterViewModel = hiltViewModel(),
) {
    // Use lifecycle-aware state collection (same pattern as SuppliesListScreen)
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(supplyId) {
        if (supplyId.isNotEmpty()) {
            viewModel.onSelectSupply(supplyId)
        }
    }
    LaunchedEffect(state.registerSuccess) {
        if (state.registerSuccess) {
            // Navigate back; previous screen will display snackbar using savedStateHandle
            onRegisterClick()
            viewModel.clearRegisterStatus()
        }
    }
    // Match the outer structure/style of SuppliesListScreen: Scaffold with TopAppBar
    Scaffold(
        containerColor = Background,
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbarcustom(
                    data.visuals.message.toString(),
                    modifier = Modifier,
                    ifSucces = true,
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    // Show selected supply name in the top bar when available
                    val supplyName = state.suppliesList.firstOrNull { it.id == state.selectedSupplyId }?.name
                    Text(
                        text = supplyName?.let { "Registrar Lotes de $it" } ?: "Registrar Lotes",
                        color = White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
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
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background),
            )
        },
        bottomBar = {
            Column {
                SupplyBatchRegisterBottomBar(
                    uiState = state,
                    onRegisterClick = {
                        // Only trigger ViewModel action; navigation happens on registerSuccess
                        viewModel.registerBatch()
                    },
                    onBackClick = {
                        viewModel.clearRegisterStatus()
                        onBackClick()
                    },
                )
            }
        },
    ) { padding ->
        // Box to respect the scaffold content padding and mirror SuppliesListScreen layout
        val configuration = LocalConfiguration.current
        val isLandscape = configuration.screenWidthDp > configuration.screenHeightDp
        val scrollModifier = if (isLandscape) Modifier.verticalScroll(rememberScrollState()) else Modifier

        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .padding(8.dp)
                    .then(scrollModifier),
        ) {
            // Reuse the content composable so we keep single-source-of-truth for UI
            SupplyBatchRegisterContent(
                uiState = state,
                onSelectSupply = { viewModel.onSelectSupply(it) },
                onQuantityChanged = { viewModel.onQuantityChanged(it) },
                onExpirationDateChanged = { viewModel.onExpirationDateChanged(it) },
                onBoughtDateChanged = { viewModel.onBoughtDateChanged(it) },
                onIncrementQuantity = { viewModel.onIncrementQuantity() },
                onDecrementQuantity = { viewModel.onDecrementQuantity() },
                onAcquisitionTypeSelected = { viewModel.onAcquisitionTypeSelected(it) },
            )
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun SupplyBatchRegisterBottomBar(
    uiState: SupplyBatchRegisterUiState,
    onRegisterClick: () -> Unit,
    onBackClick: () -> Unit,
) {
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(Background)
                .padding(horizontal = 28.dp, vertical = 12.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        CancelButton(onClick = onBackClick)

        Spacer(modifier = Modifier.width(16.dp))

        if (uiState.registerLoading) {
            CircularProgressIndicator(modifier = Modifier.padding(end = 8.dp))
        }

        SaveButton(onClick = {
            if (!uiState.registerLoading && uiState.selectedSupplyId != null) onRegisterClick()
        })
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Suppress("ktlint:standard:function-naming")
@Composable
fun SupplyBatchRegisterPreview() {
    val sampleSupplies =
        listOf(
            Supply(id = "i1", name = "Tornillos", imageUrl = "", unitMeasure = "pz", batch = emptyList()),
            Supply(id = "i2", name = "Clavos", imageUrl = "", unitMeasure = "pz", batch = emptyList()),
        )

    val sampleState =
        SupplyBatchRegisterUiState(
            suppliesList = sampleSupplies,
            isLoading = false,
            error = null,
            quantityInput = "",
            expirationDateInput = "",
            boughtDateInput = "",
            registerLoading = false,
            registerError = null,
            registerSuccess = false,
        )

    // *** MODIFICACIÓN APLICADA AQUÍ ***
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        // Replicamos la estructura del Scaffold de la pantalla
        Scaffold(
            containerColor = Background,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Registrar Lotes",
                            color = White,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    },
                    colors =
                        TopAppBarDefaults.topAppBarColors(
                            containerColor = Background,
                            titleContentColor = PrimaryBlue,
                        ),
                )
            },
            bottomBar = {
                Column {
                    SupplyBatchRegisterBottomBar(
                        uiState = sampleState,
                        onRegisterClick = {}, // Acciones de mock
                        onBackClick = {}, // Acciones de mock
                    )
                }
            },
        ) { padding ->
            Box(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(padding)
                        .padding(8.dp),
            ) {
                SupplyBatchRegisterContent(
                    uiState = sampleState,
                    onSelectSupply = {},
                    onQuantityChanged = {},
                    onExpirationDateChanged = {},
                    onBoughtDateChanged = {},
                    onIncrementQuantity = { },
                    onDecrementQuantity = { },
                    onAcquisitionTypeSelected = {},
                )
            }
        }
    }
}

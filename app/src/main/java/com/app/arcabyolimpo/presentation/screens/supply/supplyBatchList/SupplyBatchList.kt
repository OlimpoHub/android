package com.app.arcabyolimpo.presentation.screens.supply.supplyBatchList

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.AlertDialogDefaults.containerColor
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchList
import com.app.arcabyolimpo.presentation.screens.supply.supplybatchregister.SupplyBatchRegisterViewModel
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.Snackbarcustom
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.presentation.ui.components.organisms.SupplyBatchListContent
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplyBatchListScreen(
    supplyId: String,
    supplyName: String,
    date: String,
    onModifyClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: SupplyBatchListViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // Load the batches for this supply when the screen is shown
    LaunchedEffect(supplyId) {
        // use provided date (sql format yyyy-MM-dd) to fetch batches
        viewModel.getSupplyBatch(date, supplyId)
        // also fetch supply details (name) to display in the title
        viewModel.getSupplyDetails(supplyId)
    }

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
                    val titleName = state.supplyName ?: supplyName
                    Text(
                        text = "Lotes de ${titleName.ifBlank { supplyName }}",
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
    ) { padding ->
        Box(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(padding)
                    .padding(8.dp),
        ) {
            SupplyBatchListContent(
                uiState = state,
                date = date,
                onBackClick = {
                    // no-op: keep behavior simple
                    onBackClick()
                },
                onModifyClick = { id ->
                    onModifyClick(id)
                },
            )
        }
    }
}

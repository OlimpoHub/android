package com.app.arcabyolimpo.presentation.screens.supply.supplyDetail

import android.app.Activity
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import com.app.arcabyolimpo.presentation.common.components.ErrorView
import com.app.arcabyolimpo.presentation.common.components.LoadingShimmer
import com.app.arcabyolimpo.presentation.ui.components.organisms.SupplyDetailContent
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.DecisionDialog
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.SnackbarVisualsWithError
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.Snackbarcustom
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
 * @param modifySupplyBatch: () -> Unit -> function when you want to MODIFY A SUPPLY BATCH
 * @param deleteSupplyBatch: () -> function when you want to DELETE A SUPPLY BATCH
 * @param viewModel: SuppliesDetailViewModel -> view model that gathers the data and updates the view
 * ---------------------------------------------------------------------------------------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuppliesDetailScreen(
    idInsumo: String,
    onBackClick: () -> Unit,
    onClickAddSupplyBatch: () -> Unit,
    onClickDelete: () -> Unit,
    onClickModify: () -> Unit,
    modifySupplyBatch: () -> Unit,
    deleteSupplyBatch: () -> Unit,
    viewModel: SuppliesDetailViewModel = hiltViewModel(),
) {
    // State of the snackbar that will handle all messages on the screen.
    val snackbarHostState = remember { SnackbarHostState() }
    // We use it to launch the coroutine that displays the snackbar.
    val scope = rememberCoroutineScope()
    // We collect the state exposed by the ViewModel, reacting to changes
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // In this case, if there is no error in the uiState,
    // we consider it a success.
    val haserror = if (uiState.error == null){
        true
    }else {
        false
    }

    LaunchedEffect(idInsumo) {
        viewModel.getSupply(idInsumo)
    }

    // Effect triggered when the uiState.snackbarVisible flag changes.
    // If true, the snackbar is displayed and the system waits to dismiss it.
    LaunchedEffect(uiState.snackbarVisible) {

        if (uiState.snackbarVisible == true){
            // show snackbar as a suspend function
            scope.launch {

                val result =
                    snackbarHostState.showSnackbar(
                    SnackbarVisualsWithError(
                        "Insumo Borrado Correctamente",
                        // isError controls the visual style (red/blue)
                        isError = true,
                    ),
                )
                // When the snack bar ends, we act according to the result
                when (result){
                    SnackbarResult.Dismissed -> {
                        //Tell the VM that the visible snackbar is finished
                        viewModel.onSnackbarShown()

                        //Navigate back as soon as the snack bar is finished
                        onBackClick()

                    }
                    SnackbarResult.ActionPerformed -> {}
                }
            }
        }
    }

    // Displays a confirmation dialog when the user wants to delete an item.
    if (uiState.decisionDialogVisible == true){
        DecisionDialog(
            // The dialogue closes without any action being taken.
            onDismissRequest = {
                viewModel.toggledecisionDialog(showdecisionDialog = false)
            },
            // If the user confirms, the ViewModel is called to delete the input
            onConfirmation = {
                viewModel.deleteOneSupply(idInsumo)
            },
            dialogTitle = "¿Estas seguro de eliminar este Insumo?",
            dialogText = "Esta accion no podra revertirce",
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
                    data.visuals.message.toString(),
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
                    SupplyDetailContent(
                        supply = uiState.supplyBatchList!!,
                        onClickAddSupplyBatch = onClickAddSupplyBatch,
                        onClickDelete = {
                            // When you press delete in the details, we display the dialog
                            viewModel.toggledecisionDialog(showdecisionDialog = true)
                        },
                        onClickModify = onClickModify,
                        modifySupplyBatch = modifySupplyBatch,
                        deleteSupplyBatch = { idBatch ->
                            viewModel.selectedBatchId = idBatch
                            viewModel.toggledecisionDialog(showdecisionDialog = true)
                        },
                    )
                }
            }
        }
    }
}

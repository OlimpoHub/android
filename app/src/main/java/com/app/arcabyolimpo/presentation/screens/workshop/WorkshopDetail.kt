package com.app.arcabyolimpo.presentation.screens.workshop

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.presentation.screens.home.InventoryScreen
import com.app.arcabyolimpo.presentation.navigation.Screen
import com.app.arcabyolimpo.presentation.screens.session.SessionViewModel
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.DecisionDialog
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.SnackbarVisualsWithError
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.Snackbarcustom
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.DeleteButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ModifyButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.videoView
import com.app.arcabyolimpo.presentation.ui.components.molecules.FunctionalNavBar
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background
import kotlinx.coroutines.launch 

/**
 *
 * This composable acts as the screen for an individual Workshop.
 */

/**
 *
 * This composable acts as the screen for an individual Workshop.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkshopDetailScreen(
    navController: NavHostController,
    workshopId: String,
    onModifyClick: (String) -> Unit,
    viewModel: WorkshopDetailViewModel = hiltViewModel(),
    sessionViewModel: SessionViewModel = hiltViewModel(),
) {
    val role by sessionViewModel.role.collectAsState()
    val workshop by viewModel.workshop.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val formattedDate by viewModel.formattedDate.collectAsState()
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    val haserror = if (uiState.error == null){
        true
    }else {
        false
    }

    LaunchedEffect(uiState.snackbarVisible) {

        if (uiState.snackbarVisible == true){
            scope.launch {

                val result =
                    snackbarHostState.showSnackbar(
                        SnackbarVisualsWithError(
                            "Taller Borrado Correctamente",
                            isError = true,
                        ),
                    )
                when (result){
                    SnackbarResult.Dismissed -> {
                        viewModel.onSnackbarShown()
                        navController.popBackStack()

                    }
                    SnackbarResult.ActionPerformed -> {}
                }
            }
        }
    }
        LaunchedEffect(Unit) {
            viewModel.loadWorkshop()
        }

    if (uiState.decisionDialogVisible == true){
        DecisionDialog(
            onDismissRequest = {
                viewModel.toggledecisionDialog(showdecisionDialog = false)
            },
            onConfirmation = {
                viewModel.deleteWorkshops(workshopId)
            },
            dialogTitle = "¿Estas seguro de eliminar este Taller?",
            dialogText = "Esta accion no podra revertirce",
            confirmText = "Confirmar",
            dismissText = "Cancelar",
        )
    }

        Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbarcustom(
                    data.visuals.message.toString(),
                    modifier = Modifier,
                    ifSucces = haserror,
                )
            }
        },


        containerColor = Background,
            topBar = {
                TopAppBar(
                    title = {
                        when{
                            workshop != null -> {
                                Text(
                                    text = "${workshop?.nameWorkshop}",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            errorMessage != null ->
                                Text(
                                    text = "Error",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                        }

                            },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                        }
                    }
                )
            },

        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.Start
                )
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        when {
                            isLoading -> CircularProgressIndicator()
                            errorMessage != null -> Text(text = errorMessage ?: "")
                            workshop != null -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalAlignment = Alignment.Start

                                ) {
                                    Text(
                                        text = "Descripción:",
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text= workshop?.description ?: "Cargando descripción...",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(
                                        text= "Sobre el Taller:",
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "• Hora: ${workshop?.startHour} - ${workshop?.finishHour}",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "• Fecha: $formattedDate",
                                        style = MaterialTheme.typography.bodyLarge
                                    )


                                }
                            }

                            else -> Text("No se encontró el taller")
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 16.dp,
                            top = 12.dp
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (role != "BECARIO") {
                        DeleteButton(
                            modifier = Modifier.size(width = 112.dp, height = 40.dp),
                            onClick = {
                                // When you press delete in the details, we display the dialog
                                viewModel.toggledecisionDialog(showdecisionDialog = true)
                                //Log.d("ButtonDelete", "Click ")
                            }
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    if (role != "BECARIO") {
                        ModifyButton(
                            modifier = Modifier.size(width = 112.dp, height = 40.dp),
                            onClick = {
                                onModifyClick(workshopId)
                            }
                        )
                    }
                }
            }

        }
    }
}
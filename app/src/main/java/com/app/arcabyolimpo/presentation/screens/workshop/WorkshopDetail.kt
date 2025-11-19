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
    viewModel: WorkshopDetailViewModel = hiltViewModel()
) {
    val workshop by viewModel.workshop.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    val formattedDate by viewModel.formattedDate.collectAsState()
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
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

    // Effect triggered when the uiState.snackbarVisible flag changes.
    // If true, the snackbar is displayed and the system waits to dismiss it.
    LaunchedEffect(uiState.snackbarVisible) {

        if (uiState.snackbarVisible == true){
            // show snackbar as a suspend function
            scope.launch {

                val result =
                    snackbarHostState.showSnackbar(
                        SnackbarVisualsWithError(
                            "Taller Borrado Correctamente",
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
                        navController.popBackStack()

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
                viewModel.deleteWorkshops(workshopId)
            },
            dialogTitle = "¿Estas seguro de eliminar este Taller?",
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
            bottomBar = {
                NavBar()
            }

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
                                    if (workshop?.videoTraining != null) {
                                        videoView(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .height(200.dp),// Dale una altura fija
                                            videoUrl = workshop!!.videoTraining!!
                                        )
                                    }
                                    else{
                                        Text(
                                            text= "No hay video disponible",
                                            style = MaterialTheme.typography.bodyLarge
                                        )
                                    }
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
                    DeleteButton(
                        modifier = Modifier.size(width = 112.dp, height = 40.dp),
                        onClick = {
                        // When you press delete in the details, we display the dialog
                        viewModel.toggledecisionDialog(showdecisionDialog = true)
                        //Log.d("ButtonDelete", "Click ")
                        }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

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
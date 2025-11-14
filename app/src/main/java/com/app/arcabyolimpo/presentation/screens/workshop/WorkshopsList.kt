package com.app.arcabyolimpo.presentation.screens.workshop

<<<<<<< HEAD
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
=======
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
>>>>>>> e29f30cf4a8bed0e5adf733def3ca65ec5893679
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.presentation.navigation.Screen
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.Snackbarcustom
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SearchInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.presentation.ui.components.molecules.WorkshopCard
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background
<<<<<<< HEAD
import kotlinx.coroutines.launch
=======
import com.app.arcabyolimpo.ui.theme.White
>>>>>>> e29f30cf4a8bed0e5adf733def3ca65ec5893679

/**
 * Composable screen that displays the list of workshops.
 *
 * This screen is responsible for showing a list of all the workshops.
 * retrieved from the [WorkshopsListViewModel]. It provides:
 * - A list of cards with the name of each Workshop. Image PENDING.
 * - A button to move to [AddNewWorkshop] to add a new Workshop.
 * - A searchbar. Functionality PENDING.
 * - A Filter icon. Functionality PENDING.
 * - A Notificarion icon. Functionality PENDING.
 * - A navbar at the button of the screen.
 *
 * @param navController The way to go through different screens that are in the [NavGraph]
 * @param workshopClick Callback triggered when a workshop item is clicked.
 * @param viewModel The [WorkshopsListViewModel] used to manage the UI state.
 */

@Composable
fun WorkshopsListScreen(
    navController: NavHostController,
    workshopClick: (String) -> Unit,
    viewModel: WorkshopsListViewModel = hiltViewModel()
<<<<<<< HEAD
)  {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val savedStateHandle = navController.currentBackStackEntry?.savedStateHandle
    val snackbarMessage = savedStateHandle?.get<String>("snackbarMessage")
    val snackbarSuccess = savedStateHandle?.get<Boolean>("snackbarSuccess") ?: true

    LaunchedEffect(snackbarMessage) {
        snackbarMessage?.let {
            scope.launch {
                snackbarHostState.showSnackbar(it)
            }
            savedStateHandle.remove<String>("snackbarMessage")
            savedStateHandle.remove<Boolean>("snackbarSuccess")
        }
    }

    Scaffold(
        containerColor = Background,
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                Snackbarcustom(
                    title = data.visuals.message,
                    modifier = Modifier.padding(16.dp),
                    ifSucces = snackbarSuccess
                )
            }
        },
        floatingActionButton = {
            AddButton(
                onClick = {
=======
) {
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()

        Scaffold(
            containerColor = Background,
            floatingActionButton = {
                AddButton(onClick = {
>>>>>>> e29f30cf4a8bed0e5adf733def3ca65ec5893679
                    navController.navigate(Screen.AddNewWorkshop.route)
                })
            },
            bottomBar = { NavBar() }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(top = 24.dp)
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ){
                    /** Title of the page */
                    Text(
                        text = "Talleres",
                        style = MaterialTheme.typography.headlineLarge,
                        color = White,
                        modifier = Modifier.padding(bottom = 16.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(200.dp))
                    /** Notification Icon. NON FUNCTIONAL */
                    NotificationIcon()
                }
<<<<<<< HEAD
            )
        }
    ){ padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
=======
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){
                    /** Search input. NON FUNCTIONAL */
                    SearchInput(
                        "",
                        onValueChange = {},
                        modifier = Modifier.weight(1f)
                    )
                    Spacer(modifier = Modifier.width(16.dp))
                    /** Filter icon. NON FUNCTIONAL */
                    FilterIcon()
                }
                when {
                    uiState.isLoading -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            /** Progress indicator */
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }

                    uiState.error != null -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            /** Error text*/
                            Text(
                                text = uiState.error ?: "Error desconocido",
                                modifier = Modifier.align(Alignment.Center),
                                color = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                    uiState.workshopsList.isNullOrEmpty() -> {
                        Box(modifier = Modifier.fillMaxSize()) {
                            /** No Workshops Text */
                            Text(
                                text = "No hay talleres registrados",
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    }

                    else -> {
                        LazyColumn(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 16.dp, vertical = 12.dp),
                            verticalArrangement = Arrangement.spacedBy(50.dp),
                            horizontalAlignment = Alignment.CenterHorizontally

                        ) {
                            items(uiState.workshopsList) { workshop ->
                                /** A workshop card for each item*/
                                WorkshopCard(
                                    name = workshop.nameWorkshop.toString(),
                                    onClick = {
                                        workshopClick(workshop.id.toString())
                                    }
                                )
                            }
                        }
                    }
                }
            }
>>>>>>> e29f30cf4a8bed0e5adf733def3ca65ec5893679
        }
    }
}

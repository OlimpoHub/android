package com.app.arcabyolimpo.presentation.screens.workshop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.presentation.navigation.Screen
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SearchInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.presentation.ui.components.molecules.WorkshopCard
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White

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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkshopsListScreen(
    navController: NavHostController,
    workshopClick: (String) -> Unit,
    viewModel: WorkshopsListViewModel = hiltViewModel()
) {
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        val uiState by viewModel.uiState.collectAsStateWithLifecycle()
        val searchText by viewModel.searchQuery.collectAsState()

        Scaffold(
            containerColor = Background,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "Talleres",
                            style = MaterialTheme.typography.headlineLarge,
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate(Screen.CoordinatorHome.route) }) {
                            Icon(
                                imageVector = Icons.Default.ArrowBack,
                                contentDescription = "Regresar",
                                tint = Color.White
                            )
                        }
                    },
                    actions = {
                        Box(modifier = Modifier.padding(end = 30.dp)) {
                            NotificationIcon()
                        }
                    }
                )
            },
            floatingActionButton = {
                AddButton(
                    onClick = {
                        navController.navigate(Screen.AddNewWorkshop.route)
                    }
                )
            },
            bottomBar = { NavBar() }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(top = 8.dp)
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 35.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ){

                    SearchInput(
                        value = searchText,
                        onValueChange = { viewModel.onSearchQueryChange(it) },
                        modifier = Modifier.weight(1f)
                    )

                    Spacer(modifier = Modifier.width(28.dp))
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
        }
    }
}

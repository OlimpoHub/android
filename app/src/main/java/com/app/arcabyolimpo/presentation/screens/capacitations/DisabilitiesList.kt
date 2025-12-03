package com.app.arcabyolimpo.presentation.screens.capacitations

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.presentation.navigation.Screen
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SearchInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.BeneficiaryCard
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.Snackbarcustom
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background
import kotlinx.coroutines.launch

@Composable
fun DisabilitiesListScreen(
    navController: NavHostController,
    onDisabilityClick: (String) -> Unit,
    onBackClick: () -> Unit,
    viewModel: DisabilitiesListViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val successMessage = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<String>("success_message")

    LaunchedEffect(Unit) {
        viewModel.getDisabilities()
    }

    LaunchedEffect(successMessage) {
        successMessage?.let { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<String>("success_message")
        }
    }

    val errorMessage = navController.currentBackStackEntry
        ?.savedStateHandle
        ?.get<String>("error_message")

    LaunchedEffect(errorMessage) {
        errorMessage?.let { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
            navController.currentBackStackEntry
                ?.savedStateHandle
                ?.remove<String>("error_message")
        }
    }

    DisabilitiesList(
        state = state,
        snackbarHostState = snackbarHostState,
        onSearchTextChange = viewModel::onSearchTextChange,
        beneficiaryImage = state.beneficiary?.image,
        onDisabilityClick = onDisabilityClick,
        onBackClick = onBackClick,
        navController = navController
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisabilitiesList(
    state: DisabilitiesListUiState,
    snackbarHostState: SnackbarHostState,
    onSearchTextChange: (String) -> Unit,
    onDisabilityClick: (String) -> Unit,
    beneficiaryImage: String?,
    onBackClick: () -> Unit,
    navController: NavHostController
) {
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                containerColor = Background,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Capacitaciones",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Regresar",
                                    tint = Color.White
                                )
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = Background
                        )
                    )
                },
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = padding.calculateTopPadding(),
                            bottom = padding.calculateBottomPadding(),
                        ),
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 30.dp,
                                end = 30.dp,
                                bottom = 12.dp,
                            ),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        SearchInput(
                            value = state.searchText,
                            onValueChange = onSearchTextChange,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 20.dp),
                        )
                    }

                    when {
                        state.isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally),
                            )
                        }

                        state.error != null -> {
                            Text(
                                text = state.error ?: "Error",
                                color = Color.Red,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally),
                            )
                        }

                        state.disabilities.isEmpty() -> {
                            Text(
                                text = "No se encontraron discapacidades",
                                color = Color.Red,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally),
                            )
                        }

                        else -> {
                            LazyVerticalGrid(
                                columns = GridCells.Fixed(2),
                                contentPadding = PaddingValues(vertical = 16.dp),
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(1f),
                            ) {
                                items(state.disabilities, key = { it.id }) { disability ->
                                    BeneficiaryCard(
                                        name = disability.name,
                                        onClick = {
                                            navController.navigate(Screen.DisabilityDetail.createRoute(disability.id))},
                                        cardModifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp)
                                            .height(160.dp),
                                        contentPadding = PaddingValues(
                                            vertical = 20.dp,
                                            horizontal = 40.dp,
                                        ),
                                    )
                                }
                            }
                        }
                    }
                }
            }

            Box(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 100.dp)
                    .fillMaxWidth()
            ) {
                SnackbarHost(
                    hostState = snackbarHostState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) { data ->
                    Snackbarcustom(
                        title = data.visuals.message,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}
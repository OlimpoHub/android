package com.app.arcabyolimpo.presentation.screens.beneficiary

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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.presentation.ui.components.molecules.BeneficiaryCard
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.presentation.navigation.Screen
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SearchInput

/**
 * This composable acts as the main screen, inecting the ViewModel
 * and collecting the UI state to pass to the stateless UI.
 */
@Composable
fun BeneficiaryListScreen(
    navController: NavHostController,
    onBeneficiaryClick: (String) -> Unit,
    onFilterClick: () -> Unit,
    onNotificationClick: () -> Unit,
    viewModel: BeneficiaryListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    BeneficiaryList(
        navController = navController,
        state = state,
        onSearchTextChange = viewModel::onSearchTextChange,
        onBeneficiaryClick = onBeneficiaryClick,
        onFilterClick = onFilterClick,
        onNotificationClick = onNotificationClick
    )
}

/**
 * This is the stateless UI composable for the Beneficiary List.
 * It only receives state and hoists events.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiaryList(
    navController: NavHostController,
    state: BeneficiaryListUiState,
    onSearchTextChange: (String) -> Unit,
    onBeneficiaryClick: (String) -> Unit,
    onFilterClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                containerColor = Background,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Beneficiarios",
                                style = MaterialTheme.typography.headlineLarge,
                                color = Color.White,
                                fontWeight = FontWeight.Bold
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = {navController.navigate(Screen.CoordinatorHome.route)}) {
                                Icon(
                                    imageVector = Icons.Default.ArrowBack,
                                    contentDescription = "Regresar",
                                    tint = Color.White
                                )
                            }
                        },
                        actions = {
                            Box(modifier = Modifier.padding(end = 28.dp)) {
                                NotificationIcon()
                            }
                        }
                    )
                },
                floatingActionButton = {
                    AddButton(
                        onClick = { /* TODO: agregar beneficiario */ }
                    )
                },
                bottomBar = {
                    Box(modifier = Modifier.padding(bottom = 8.dp)) {
                        NavBar()
                    }
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = padding.calculateTopPadding(),
                            bottom = padding.calculateBottomPadding()
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(
                                start = 30.dp,
                                end = 30.dp,
                                bottom = 12.dp
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        SearchInput(
                            value = state.searchText,
                            onValueChange = onSearchTextChange,
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 20.dp)
                        )

                        FilterIcon(
                            modifier = Modifier
                                .padding(start = 16.dp)
                        )
                    }

                    when {
                        state.isLoading -> {
                            CircularProgressIndicator(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 20.dp)
                                    .wrapContentWidth(Alignment.CenterHorizontally)
                            )

                        }

                        state.error != null -> {
                            Text(
                                text = state.error,
                                color = Color.Red,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .wrapContentWidth(Alignment.CenterHorizontally)
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
                                    .weight(1f)
                            ) {
                                items(
                                    items = state.beneficiaries,
                                    key = { beneficiary -> beneficiary.id }
                                ) { beneficiary ->
                                    BeneficiaryCard(
                                        name = beneficiary.name,
                                        onClick = { onBeneficiaryClick(beneficiary.id) },
                                        cardModifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 8.dp)
                                            .height(160.dp),
                                        contentPadding = PaddingValues(
                                            vertical = 20.dp,
                                            horizontal = 40.dp
                                        )
                                    )
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1B1F)
@Composable
fun BeneficiaryListPreview() {
    val navController = androidx.navigation.compose.rememberNavController()

    // Mock state
    val previewState = BeneficiaryListUiState(
        beneficiaries = listOf(
            Beneficiary("1", "John Smith 1", "", "", "", "", "", "", "", "", 1),
            Beneficiary("2", "John Smith 2", "", "", "", "", "", "", "", "", 1),
            Beneficiary("3", "John Smith 3", "", "", "", "", "", "", "", "", 1),
            Beneficiary("4", "John Smith 4", "", "", "", "", "", "", "", "", 1)
        )
    )

    ArcaByOlimpoTheme {
        BeneficiaryList(
            navController = navController,
            state = previewState,
            onSearchTextChange = {},
            onBeneficiaryClick = {},
            onFilterClick = {},
            onNotificationClick = {}
        )
    }
}

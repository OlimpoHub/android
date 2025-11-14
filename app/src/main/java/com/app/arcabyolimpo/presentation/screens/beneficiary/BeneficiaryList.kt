package com.app.arcabyolimpo.presentation.screens.beneficiary

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import com.app.arcabyolimpo.R
import com.app.arcabyolimpo.presentation.theme.Typography
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.presentation.ui.components.molecules.BeneficiaryCard
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary

/**
 * This composable acts as the main screen, inecting the ViewModel
 * and collecting the UI state to pass to the stateless UI.
 */
@Composable
fun BeneficiaryListScreen(
    onBeneficiaryClick: (String) -> Unit,
    onFilterClick: () -> Unit,
    onNotificationClick: () -> Unit,
    viewModel: BeneficiaryListViewModel = hiltViewModel()
) {
    val state by viewModel.uiState.collectAsState()

    BeneficiaryList(
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
    state: BeneficiaryListUiState,
    onSearchTextChange: (String) -> Unit,
    onBeneficiaryClick: (String) -> Unit,
    onFilterClick: () -> Unit,
    onNotificationClick: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Beneficiarios", style = Typography.headlineMedium.copy()) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    actionIconContentColor = Color.White
                ),
                actions = {
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_notification_icon),
                            contentDescription = "Notificaciones",
                            tint = Color.White.copy()
                        )
                    }
                }
            )
        },
        containerColor = Color(0xFF1C1B1F)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(horizontal = 16.dp)
                .fillMaxSize()
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = state.searchText,
                    onValueChange = onSearchTextChange,
                    label = { Text("Buscar") },
                    leadingIcon = {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_search_icon),
                            contentDescription = null
                        )
                    },
                    modifier = Modifier.weight(1f)
                )

                IconButton(onClick = onFilterClick) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_filter_icon),
                        contentDescription = "Filtrar",
                        tint = Color.White.copy()
                    )
                }
            }
            // Content Box: Handles Loading, Error, and Success states
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 16.dp)
            ) {
                when {
                    state.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    state.error != null -> {
                        Text(
                            text = state.error,
                            color = Color.Red,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                    else -> {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(vertical = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(
                                items = state.beneficiaries,
                                key = { beneficiary -> beneficiary.id }
                            ) { beneficiary ->
                                BeneficiaryCard(
                                    name = beneficiary.name,
                                    onClick = { onBeneficiaryClick(beneficiary.id) }
                                )
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
    // Create a mock state for the preview
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
            state = previewState,
            onSearchTextChange = {},
            onBeneficiaryClick = {},
            onFilterClick = {},
            onNotificationClick = {}
        )
    }
}
package com.app.arcabyolimpo.presentation.screens.supply

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.presentation.screens.supply.components.SupplyListContent
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplyListScreen(
    onSupplyClick: (String) -> Unit,
    viewModel: SuppliesListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Background,
        floatingActionButton = {
            AddButton(
                onClick = { /* acción al presionar + */ }
            )
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Insumos",
                        color = White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 24.dp)
                    )
                },
                actions = {
                    NotificationIcon(
                        modifier = Modifier.padding(horizontal = 24.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background
                )
            )
        },
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            SupplyListContent(
                suppliesList = uiState.suppliesList,
                isLoading = uiState.isLoading,
                error = uiState.error,
                onSupplyClick = onSupplyClick,
                onRetry = { viewModel.loadSuppliesList() },
                modifier = Modifier.fillMaxSize()
            )
        }
    }
}
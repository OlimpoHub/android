package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchesList

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import com.app.arcabyolimpo.presentation.screens.productbatches.model.ProductBatchUiModel
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.SearchIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SearchInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.presentation.ui.components.molecules.ProductBatchItem
import com.app.arcabyolimpo.presentation.ui.components.organisms.Filter
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White

/** ProductBatchesListScreen: Composable screen displaying a list of product batches.
 *
 * @param modifier Modifier = Modifier -> allows layout customization (padding, width, etc.)
 * @param onBackClick () -> Unit -> callback for back navigation
 * @param onDetailClick (String) -> Unit -> callback when a product batch is selected for detail view
 * @param onAddClick () -> Unit -> callback to add a new product batch
 * @param viewModel ProductBatchesListViewModel = hiltViewModel() -> ViewModel for managing UI state
 */

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun ProductBatchesListScreen(
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit,
    onDetailClick: (String) -> Unit,
    onAddClick: () -> Unit,
    viewModel: ProductBatchesListViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    var text by remember { mutableStateOf("") }
    var showFilter by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        viewModel.loadData()
    }

    Scaffold(
        containerColor = Background,
        bottomBar = {
            NavBar()
        },
        floatingActionButton = {
            AddButton(onClick = { onAddClick() })
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lotes de Productos",
                        color = White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier =
                            Modifier
                                .fillMaxWidth()
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
                actions = {
                    NotificationIcon(
                        modifier =
                            Modifier
                                .padding(horizontal = 24.dp)
                                .size(24.dp),
                    )
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Background
                    ),
            )
        },
    ) { padding ->
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SearchInput(
                    value = text,
                    onValueChange = { text = it },
                    trailingIcon = {
                        IconButton(onClick = {
                            viewModel.searchProductBatch(text)
                        }) {
                            SearchIcon()
                        }
                    },
                    modifier = Modifier.weight(1f),
                )

                IconButton(onClick = {
                    showFilter = true
                }) {
                    FilterIcon(modifier = Modifier.size(32.dp))
                }
            }

            when {
                state.isLoading -> {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(padding),
                        contentAlignment = Alignment.Center,
                    ) {
                        CircularProgressIndicator()
                    }
                }

                state.error != null -> {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(padding),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "Error: ${state.error}",
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.error,
                        )
                    }
                }
                state.items.isEmpty() -> {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(padding),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text("No product batches found")
                    }
                }

                else -> {
                    LazyColumn(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(0.dp),
                    ) {
                        items(state.items) { batch ->
                            ProductBatchItem(
                                batch = batch,
                                onClick = { onDetailClick(batch.idInventario) },
                            )
                        }
                    }
                }
            }
        }
    }

    // Filtro modal
    if (showFilter && state.filterData != null) {
        Filter(
            data = state.filterData!!,
            initialSelected = state.filters,
            onApply = { dto ->
                viewModel.filterProductBatch(dto)
                showFilter = false
            },
            onDismiss = { showFilter = false },
            onClearFilters = {
                viewModel.clearFilters()
                viewModel.loadData()
                showFilter = false
            }
        )
    }

}

package com.app.arcabyolimpo.presentation.screens.product.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.presentation.screens.product.list.compose.ProductListContent
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.SearchIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardIconInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ReturnIcon
import com.app.arcabyolimpo.presentation.ui.components.organisms.Filter
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.ErrorRed
import com.app.arcabyolimpo.ui.theme.White
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver

/**
 * Composable screen that displays the list of products.
 *
 * It provides:
 * - Top bar with title and notification icon.
 * - Search input.
 * - Filter & order modal (by workshop and alphabetical order).
 * - Floating action button to add new products.
 * - Product list with loading / error states.
 *
 * @param onProductClick Callback triggered when a product item is clicked.
 * Receives the product ID as parameter.
 * @param onAddProductClick Callback triggered when the add button is clicked.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onProductClick: (String) -> Unit,
    onAddProductClick: () -> Unit,
    onBackClick: () -> Unit,
    viewModel: ProductListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val lifecycleOwner = LocalLifecycleOwner.current

    // Reload when screen becomes visible
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadProductsList()
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // Controls visibility of the filter modal
    var showFilter by remember { mutableStateOf(false) }

    // Text entered by the user in the search field
    var searchQuery by remember { mutableStateOf("") }

    /**
     * Products filtered according to the search text.
     * The filter is applied over the already-processed list in uiState.productsList
     * (which may also be filtered/ordered by the modal).
     */
    val filteredProducts =
        remember(searchQuery, uiState.productsList) {
            if (searchQuery.isEmpty()) {
                uiState.productsList
            } else {
                uiState.productsList.filter { product ->
                    product.name.contains(searchQuery, ignoreCase = true) ||
                            product.id.contains(searchQuery, ignoreCase = true) ||
                            product.workshopName?.contains(searchQuery, ignoreCase = true) == true
                }
            }
        }

    Scaffold(
        containerColor = Background,
        floatingActionButton = {
            AddButton(
                onClick = onAddProductClick,
            )
        },
        topBar = {
            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Back",
                            tint = White,
                        )
                    }
                },
                title = {
                    Text(
                        text = "Productos",
                        color = White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                    )
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Background,
                    ),
            )
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 24.dp),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp, bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
                    // Search input with trailing search icon
                    StandardIconInput(
                        label = "",
                        placeholder = "Buscar",
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        trailingIcon = { SearchIcon() },
                        modifier =
                            Modifier
                                .weight(1f)
                                .height(74.dp),
                    )
                }

                Spacer(modifier = Modifier.size(16.dp))

                // Filter icon that opens the filter & order modal
                FilterIcon(
                    modifier =
                        Modifier
                            .size(40.dp)
                            .padding(top = 16.dp)
                            .clickable {
                                showFilter = true
                            },
                )
            }

            Box(
                modifier = Modifier.fillMaxSize(),
            ) {
                when {
                    uiState.isLoading || uiState.error != null -> {
                        ProductListContent(
                            productsList = filteredProducts,
                            isLoading = uiState.isLoading,
                            error = uiState.error,
                            onProductClick = onProductClick,
                            onRetry = { viewModel.loadProductsList() },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }

                    filteredProducts.isEmpty() -> {
                        Text(
                            text = "No se encontraron productos",
                            color = ErrorRed,
                            style = Typography.headlineMedium,
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }

                    else -> {
                        ProductListContent(
                            productsList = filteredProducts,
                            isLoading = false,
                            error = null,
                            onProductClick = onProductClick,
                            onRetry = { },
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }
    }

    // Filter & order modal (bottom sheet)
    if (showFilter) {
        Filter(
            data = uiState.filterData,
            initialSelected = uiState.selectedFilters,
            onApply = { dto ->
                showFilter = false
                viewModel.applyFilters(dto)
            },
            onDismiss = { showFilter = false },
            onClearFilters = {
                viewModel.clearFilters()
                showFilter = false
            },
        )
    }
}

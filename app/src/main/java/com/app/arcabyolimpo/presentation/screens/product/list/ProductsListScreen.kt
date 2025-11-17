package com.app.arcabyolimpo.presentation.screens.product.list

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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.arcabyolimpo.domain.model.product.Product
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.SearchIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SearchInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.presentation.ui.components.molecules.ProductItem
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White

@Suppress("ktlint:standard:function-naming")
@Composable
fun ProductsListRoute(
    onProductClick: (String) -> Unit,
    onBackClick: () -> Unit = {},
    viewModel: ProductsViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    ProductsListScreen(
        state = uiState,
        onSearchChange = viewModel::onSearchQueryChange,
        onBackClick = onBackClick,
        onDetailClick = onProductClick,
        onAddClick = { /* TODO: navegar a agregar producto si quieres */ },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun ProductsListScreen(
    state: ProductsUiState,
    onSearchChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onDetailClick: (String) -> Unit,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val filteredProducts = remember(state.searchQuery, state.products) {
        val q = state.searchQuery.trim()
        if (q.isEmpty()) {
            state.products
        } else {
            state.products.filter { product ->
                product.name.contains(q, ignoreCase = true) ||
                        (product.description?.contains(q, ignoreCase = true) == true) ||
                        product.id.contains(q, ignoreCase = true)
            }
        }
    }

    Scaffold(
        containerColor = Background,
        bottomBar = {
            NavBar()
        },
        floatingActionButton = {
            AddButton(onClick = onAddClick)
        },
        topBar = {
            TopAppBar(
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
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            contentDescription = "Back",
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
                        containerColor = Background,
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
                    value = state.searchQuery,
                    onValueChange = onSearchChange,
                    trailingIcon = { SearchIcon() },
                    modifier = Modifier.weight(1f),
                )

                IconButton(onClick = { /* TODO: filtros luego */ }) {
                    FilterIcon(
                        Modifier.size(32.dp),
                    )
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

                filteredProducts.isEmpty() -> {
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .padding(padding),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = "No se encontraron productos",
                            color = White,
                        )
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
                        items(filteredProducts) { product ->
                            ProductItem(
                                name = product.name,
                                unitaryPrice = product.unitaryPrice.toString(),
                                workshopName = product.workshopName,
                                onClick = { onDetailClick(product.id) },
                            )
                        }
                    }
                }
            }
        }
    }
}

// ---------- ITEM DE LISTA (estilo parecido a las cards del mock) ----------
@Composable
fun ProductItem(
    product: Product,
    onClick: () -> Unit,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant,
        ),
        shape = MaterialTheme.shapes.large,
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.titleMedium,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = "Precio unitario: ${product.unitaryPrice}",
                    style = MaterialTheme.typography.bodyMedium,
                )
                product.workshopName?.let {
                    Text(
                        text = "Taller: $it",
                        style = MaterialTheme.typography.bodySmall,
                    )
                }
            }

            Button(
                onClick = onClick,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = White,
                ),
            ) {
                Text(text = "Ver")
            }
        }
    }
}

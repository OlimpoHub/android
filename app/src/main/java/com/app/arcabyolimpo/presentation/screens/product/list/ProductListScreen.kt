package com.app.arcabyolimpo.presentation.screens.product.list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import com.app.arcabyolimpo.presentation.ui.components.organisms.Filter
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.ErrorRed
import com.app.arcabyolimpo.ui.theme.White

/**
 * Composable screen that displays the list of products.
 *
 * This screen is responsible for showing a list of available products
 * retrieved from the [ProductListViewModel]. It provides:
 * - A top bar with the title and notification icon.
 * - A floating action button for adding new products.
 * - A content area showing the product list with loading and error states.
 *
 * @param onProductClick Callback triggered when a product item is clicked.
 * Receives the product ID as a parameter.
 * @param onAddProductClick Callback triggered when the add button is clicked.
 * @param viewModel The [ProductListViewModel] used to manage the UI state.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
    onProductClick: (String) -> Unit,
    onAddProductClick: () -> Unit,
    viewModel: ProductListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // State variable to track whether the filter modal is visible
    var showFilter by remember { mutableStateOf(false) }

    // State variable to track the current text entered by the user in the search field
    var searchQuery by remember { mutableStateOf("") }

    /**
     * List of products filtered based on the user's search input.
     *
     * @param searchQuery Text entered by the user in the search field.
     * @param uiState.productsList Complete list of products loaded in the UI.
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
                actions = {
                    NotificationIcon(
                        modifier =
                            Modifier
                                .padding(horizontal = 24.dp)
                                .size(28.dp),
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
                    /**
                     * Text input field with an icon, used to search products.
                     *
                     * This component allows the user to type text to filter the list of products
                     * on the screen. It updates automatically via the searchQuery state.
                     *
                     * Main parameters:
                     * @param label Optional label text for the field (empty here).
                     * @param placeholder Text shown when the field is empty ("Buscar").
                     * @param value Current value of the field, bound to searchQuery.
                     * @param onValueChange Callback executed whenever the user types something.
                     *                      Updates the [searchQuery] state.
                     * @param trailingIcon Icon displayed at the end of the text field (search icon).
                     * @param modifier Compose modifier to define size, weight, padding, etc.
                     */
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

                Spacer(modifier = Modifier.width(16.dp))

                /**
                 * Icon button used to open the filter panel for products.
                 * When clicked, it sets showFilter to `true`, triggering the display
                 * of the filter UI.
                 *
                 * @param modifier Compose Modifier used to define size, padding, and click behavior.
                 */
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
                /**
                 * Displays the main content of the product list screen based on the current UI state.
                 *
                 * The content is shown according to the following conditions:
                 * 1. If the data is loading or there is an error,
                 * 2. If the filtered list is empty
                 * 3. Otherwise, the composable displays the filtered list normally,
                 *
                 * @param filteredProducts List of products filtered by the search query.
                 * @param uiState Current UI state of the product list screen.
                 * @param onProductClick Callback triggered when a product item is clicked.
                 * @param viewModel ViewModel used to reload the product list on retry.
                 */

                if (uiState.isLoading || uiState.error != null) {
                    ProductListContent(
                        productsList = filteredProducts,
                        isLoading = uiState.isLoading,
                        error = uiState.error,
                        onProductClick = onProductClick,
                        onRetry = { viewModel.loadProductsList() },
                        modifier = Modifier.fillMaxSize(),
                    )
                } else if (filteredProducts.isEmpty()) {
                    Text(
                        text = "No se encontraron productos",
                        color = ErrorRed,
                        style = Typography.headlineMedium,
                        modifier = Modifier.align(Alignment.Center),
                    )
                } else {
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
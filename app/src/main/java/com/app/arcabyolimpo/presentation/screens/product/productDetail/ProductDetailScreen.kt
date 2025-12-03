package com.app.arcabyolimpo.presentation.screens.product.productDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.domain.model.product.Product
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.DeleteButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ModifyButton
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.ButtonBlue
import com.app.arcabyolimpo.ui.theme.DangerGray
import com.app.arcabyolimpo.ui.theme.ErrorRed
import com.app.arcabyolimpo.ui.theme.White

/**
 * Product detail screen that displays comprehensive information about a product.
 *
 * Layout structure:
 * - Top: Image (left) + Product info (right): name, price, workshop, category, availability
 * - Bottom: Description and active lots sections
 *
 * @param productId The ID of the product to display
 * @param onBackClick Callback when back button is pressed
 * @param onEditClick Callback when edit button is pressed
 * @param onDeleteClick Callback when delete finishes successfully (navegar hacia atrás)
 * @param viewModel The ViewModel managing the product detail state
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
    productId: String,
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit,
    onDeleteClick: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    // Cargar producto al entrar
    LaunchedEffect(productId) {
        viewModel.loadProduct(productId)
    }

    LaunchedEffect(uiState.snackbarVisible, uiState.error) {
        if (uiState.snackbarVisible) {
            val message =
                if (uiState.error == null) {
                    "Producto eliminado correctamente"
                } else {
                    "Este producto esta relacionado con algun lote de producto, no se puede eliminar: ${uiState.error}"
                }

            snackbarHostState.showSnackbar(message)
            viewModel.onSnackbarShown()
        }
    }

    LaunchedEffect(uiState.shouldNavigateBack) {
        if (uiState.shouldNavigateBack) {
            onDeleteClick()
            viewModel.onNavigatedBackHandled()
        }
    }

    Scaffold(
        containerColor = Background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detalle del Producto",
                        color = White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineLarge,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Volver",
                            tint = White,
                        )
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Background,
                    ),
            )
        },
    ) { padding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = White,
                    )
                }

                uiState.error != null && uiState.product == null -> {
                    Column(
                        modifier =
                            Modifier
                                .align(Alignment.Center)
                                .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = uiState.error ?: "Error desconocido",
                            color = ErrorRed,
                            style = Typography.headlineMedium,
                            modifier = Modifier.padding(bottom = 16.dp),
                        )
                        Button(
                            onClick = { viewModel.loadProduct(productId) },
                            colors =
                                ButtonDefaults.buttonColors(
                                    containerColor = White,
                                ),
                        ) {
                            Text("Reintentar", color = Background)
                        }
                    }
                }

                uiState.product != null -> {
                    ProductDetailContent(
                        product = uiState.product!!,
                        isDeleted = uiState.deleted,
                        onEditClick = { onEditClick(productId) },
                        onDeleteClick = { viewModel.toggleDecisionDialog(true) },
                        modifier = Modifier.fillMaxSize(),
                    )
                }
            }

            if (uiState.decisionDialogVisible && uiState.product != null) {
                val productIdDialog = uiState.product!!.id

                DeleteConfirmationDialog(
                    onConfirm = { viewModel.deleteProduct(id = productIdDialog) },
                    onDismiss = { viewModel.toggleDecisionDialog(false) },
                )
            }
        }
    }
}

@Composable
private fun ProductDetailContent(
    product: Product,
    isDeleted: Boolean = false,
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier =
            modifier
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(28.dp),
    ) {
        // Top section: Image + Product Info
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
        ) {
            // Product image placeholder (bigger)
            Box(
                modifier =
                    Modifier
                        .size(150.dp)
                        .background(
                            color = ButtonBlue.copy(alpha = 0.1f),
                            shape = CircleShape,
                        ),
            )

            // Product information
            Column(
                modifier =
                    Modifier
                        .weight(1f)
                        .align(Alignment.CenterVertically),
                verticalArrangement = Arrangement.spacedBy(10.dp),
            ) {
                // Product name
                Text(
                    text = product.name,
                    color = White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 22.sp,
                    fontFamily = Poppins,
                )

                // Price
                Text(
                    text = "Precio: $${product.unitaryPrice}",
                    color = White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Poppins,
                )

                // Workshop
                if (product.workshopName != null) {
                    Text(
                        text = "Taller: ${product.workshopName}",
                        color = White.copy(alpha = 0.8f),
                        fontSize = 15.sp,
                        fontFamily = Poppins,
                    )
                }

                // Category (si existe en tu modelo)
                Text(
                    text = "Categoría: ${product.category}",
                    color = White.copy(alpha = 0.8f),
                    fontSize = 15.sp,
                    fontFamily = Poppins,
                )

                // Availability status
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    Text(
                        text = "Estado:",
                        color = White.copy(alpha = 0.8f),
                        fontSize = 15.sp,
                        fontFamily = Poppins,
                    )
                    Surface(
                        color =
                            if (product.available) {
                                ButtonBlue.copy(alpha = 0.3f)
                            } else {
                                ErrorRed.copy(alpha = 0.3f)
                            },
                        shape = MaterialTheme.shapes.small,
                    ) {
                        Text(
                            text = if (product.available) "Disponible" else "No disponible",
                            color = if (product.available) White else ErrorRed,
                            fontSize = 12.sp,
                            fontFamily = Poppins,
                            modifier =
                                Modifier.padding(
                                    horizontal = 12.dp,
                                    vertical = 6.dp,
                                ),
                        )
                    }
                }
            }
        }

        Divider(
            color = DangerGray.copy(alpha = 0.3f),
            thickness = 1.dp,
        )

        // Description section
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
        ) {
            Text(
                text = "Descripción",
                color = White,
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                fontFamily = Poppins,
            )
            Text(
                text = product.description ?: "Sin descripción disponible",
                color = White.copy(alpha = 0.8f),
                fontSize = 15.sp,
                fontFamily = Poppins,
                lineHeight = 22.sp,
            )
        }

        Divider(
            color = DangerGray.copy(alpha = 0.3f),
            thickness = 1.dp,
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Action buttons
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                if (!isDeleted) {
                    DeleteButton(
                        modifier = Modifier.size(width = 140.dp, height = 40.dp),
                        onClick = onDeleteClick,
                    )
                }
                ModifyButton(
                    onClick = onEditClick,
                    modifier = Modifier.size(width = 140.dp, height = 40.dp),
                )
            }
        }
    }
}

@Composable
private fun DeleteConfirmationDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Eliminar producto",
                color = White,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
            )
        },
        text = {
            Text(
                text = "¿Estás seguro de que deseas eliminar este producto? Esta acción no se puede deshacer.",
                color = White.copy(alpha = 0.8f),
                fontFamily = Poppins,
            )
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text(
                    text = "Eliminar",
                    color = ErrorRed,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = "Cancelar",
                    color = White,
                    fontFamily = Poppins,
                )
            }
        },
        containerColor = Background,
    )
}

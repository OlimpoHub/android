package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchDetail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.app.arcabyolimpo.presentation.common.components.ErrorView
import com.app.arcabyolimpo.presentation.common.components.LoadingShimmer
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.DecisionDialog
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.DeleteButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ModifyButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.ActiveStatus
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.ExpiredStatus
import com.app.arcabyolimpo.presentation.ui.components.molecules.InfoRow
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White
import kotlinx.coroutines.launch
import org.jetbrains.annotations.Async

/**
 * A Composable screen that displays detailed information about a single product batch.
 *
 * This screen fetches and shows data like the product image, price, status, and dates.
 * It provides options to navigate back, delete the batch (with confirmation), or navigate
 * to a modification screen. It also handles loading and error states.
 *
 * @param batchId The unique identifier of the product batch to be displayed.
 * @param onBackClick A callback function to be invoked when the user taps the back button.
 * @param onModifyClick A callback function that triggers navigation to the modification screen,
 * passing the batch ID.
 * @param viewModel The ViewModel instance for this screen, provided by Hilt, which manages
 * state and business logic.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun ProductBatchDetailScreen(
    batchId: String,
    onBackClick: () -> Unit,
    onModifyClick: (String) -> Unit,
    viewModel: ProductBatchDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsState()
    val batch = state.batch

    LaunchedEffect(batchId) {
        viewModel.loadBatch(batchId)
    }

    var showConfirmDialog by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = batch?.nombre ?: "Detalle de Lote",
                        color = Color.White,
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
                            "Back",
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
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
        ) {
            when {
                state.isLoading -> LoadingShimmer()
                state.error != null ->
                    Box(
                        modifier =
                            Modifier
                                .fillMaxSize()
                                .background(Background),
                        contentAlignment = Alignment.Center,
                    ) {
                        ErrorView(
                            message = state.error,
                            onRetry = { viewModel.loadBatch(batchId) },
                        )
                    }
                else -> {
                    Column(
                        modifier =
                            Modifier
                                .weight(1f)
                                .verticalScroll(rememberScrollState())
                                .padding(horizontal = 16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Spacer(modifier = Modifier.height(16.dp))

                        Box(
                            modifier =
                                Modifier
                                    .fillMaxWidth(0.8f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.surface),
                        ) {
                            AsyncImage(
                                model = "http://74.208.78.8:8080/" + batch?.imagen,
                                contentDescription = "imagen de ${batch?.nombre}",
                                contentScale = ContentScale.Fit,
                                alignment = Alignment.Center,
                                modifier =
                                    Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFF040710)),
                            )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Column(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                        ) {
                            InfoRow(label = "Precio de venta:", value = batch?.precioVentaFormatted ?: "$00 MXN")
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                            ) {
                                Text(
                                    text = "Estatus:",
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White,
                                    style = MaterialTheme.typography.bodyLarge.copy(fontSize = 18.sp),
                                )

                                StatusBadge(status = batch?.disponible ?: "Disponible")
                            }

                            InfoRow(label = "Cantidad Producida:", value = "${batch?.cantidadProducida ?: "0000"}")
                            InfoRow(label = "Precio Unitario:", value = batch?.precioUnitario ?: "$00 MXN")
                            InfoRow(label = "Descripción:", value = batch?.descripcion ?: "Lote de ${batch?.nombre ?: "Producto"}")
                            InfoRow(label = "Fecha de Caducidad:", value = batch?.fechaCaducidadFormatted ?: "00/00/00")
                            InfoRow(label = "Fecha de Elaboración:", value = batch?.fechaRealizacionFormatted ?: "00/00/00")
                        }

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                    ) {
                        DeleteButton(
                            onClick = { showConfirmDialog = true },
                            modifier = Modifier.weight(1f),
                        )

                        if (showConfirmDialog) {
                            DecisionDialog(
                                onDismissRequest = {
                                    showConfirmDialog = false
                                    scope.launch {
                                        snackbarHostState.showSnackbar("Lote no eliminado")
                                    }
                                },
                                onConfirmation = {
                                    showConfirmDialog = false
                                    viewModel.deleteBatch(batchId)
                                    onBackClick()
                                },
                                dialogTitle = "Confirmar eliminación",
                                dialogText = "¿Deseas eliminar este lote de producto?",
                                confirmText = "Confirmar",
                                dismissText = "Cancelar",
                            )
                        }

                        ModifyButton(
                            onClick = {
                                onModifyClick(batch?.idInventario ?: "")
                            },
                            modifier = Modifier.weight(1f),
                        )
                    }
                }
            }
        }
    }
}

/**
 * A private helper Composable that displays a status badge.
 *
 * This function determines which visual style to use for the badge based on the status text.
 * For example, it will show an "ExpiredStatus" badge if the status is "Caducado".
 *
 * @param status The text string representing the batch's status (e.g., "Disponible", "Caducado").
 */
@Suppress("ktlint:standard:function-naming")
@Composable
private fun StatusBadge(status: String) {
    when (status.equals("Caducado", ignoreCase = true)) {
        true -> {
            ExpiredStatus(text = status)
        }
        false -> {
            ActiveStatus(text = status)
        }
    }
}

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
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import org.jetbrains.annotations.Async

/** ProductBatchDetailScreen: Composable screen displaying detailed information about a product batch.
 *
 * @param batchId String -> ID of the product batch to display
 * @param onBackClick () -> Unit -> callback for back navigation
 * @param viewModel ProductBatchDetailViewModel = hiltViewModel() -> ViewModel for managing UI state
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
                            AsyncImage(model = batch?.imagen, contentDescription = null)
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
                            InfoRow(label = "Precio de Venta:", value = batch?.precioUnitario ?: "$00 MXN")
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
                            onClick = {
                                viewModel.toggledecisionDialog(
                                    showdecisionDialog = true,
                                )
                            },
                            modifier = Modifier.weight(1f),
                        )
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
        if (state.decisionDialogVisible) {
            DecisionDialog(
                onDismissRequest = {
                    viewModel.toggledecisionDialog(false)
                },
                onConfirmation = {
                    viewModel.deleteBatch(batchId)
                    viewModel.toggledecisionDialog(false)
                    onBackClick()
                },
                dialogTitle = "¿Estás seguro de eliminar este Lote?",
                dialogText = "Esta accion no podrá revertirse",
                confirmText = "Confirmar",
                dismissText = "Cancelar",
            )
        }
    }
}

/**
 * Helper Composable para el "badge" de estatus (ej. "Caducado")
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

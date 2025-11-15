package com.app.arcabyolimpo.presentation.screens.product.productDetail

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.DecisionDialog
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.DeleteButton

@Composable
fun ProductDeleteAction(
    productId: String,
    snackbarHostState: SnackbarHostState,
    onDeleted: () -> Unit,
    viewModel: ProductDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.snackbarVisible, uiState.error) {
        if (uiState.snackbarVisible) {
            val message = uiState.error ?: "Producto eliminado correctamente"

            snackbarHostState.showSnackbar(message)
            viewModel.onSnackbarShown()

            if (uiState.error == null) {
                onDeleted()
            }
        }
    }

    Column {
        DeleteButton(
            onClick = {
                viewModel.toggleDecisionDialog(true)
            },
        )

        if (uiState.decisionDialogVisible) {
            DecisionDialog(
                onDismissRequest = {
                    viewModel.toggleDecisionDialog(false)
                },
                onConfirmation = {
                    viewModel.deleteProduct(productId)
                },
                dialogTitle = "¿Estas seguro de eliminar este Producto?",
                dialogText = "Esta acción no podrá revertirse",
                confirmText = "Confirmar",
                dismissText = "Cancelar",
            )
        }
    }
}

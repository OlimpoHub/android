package com.app.arcabyolimpo.presentation.screens.product.updateProduct

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.CancelButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SaveButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.DescriptionInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.ImageUploadInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.SelectObjectInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.StatusSelector
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White

/**
 * ProductUpdateScreen -> The main composable view for adding a new product.
 *
 * It displays all necessary input fields driven by the [UpdateProductViewModel] state.
 * It handles side effects like navigation and showing [Toast] messages based on the
 * success or error status from the UI state.
 *
 * @param viewModel The Hilt-injected [UpdateProductViewModel] providing UI state and handling input events.
 * @param onSaveSuccess Lambda function executed when the product is successfully added.
 * @param onCancel Lambda function executed when the user cancels the operation.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductUpdateScreen(
    viewModel: UpdateProductViewModel = hiltViewModel(),
    onSaveSuccess: () -> Unit,
    onCancel: () -> Unit,
) {
    val uiState by viewModel.state.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(
        key1 = uiState.saveSuccess,
        key2 = uiState.error,
    ) {
        when {
            uiState.saveSuccess -> {
                Toast.makeText(context, "Producto agregado con éxito", Toast.LENGTH_SHORT).show()
                onSaveSuccess()
            }
            uiState.error != null -> {
                Toast.makeText(context, uiState.error, Toast.LENGTH_LONG).show()
            }
        }
    }

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Modificar Producto",
                        color = White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background
                )
            )
        }
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(paddingValues = padding)
                    .padding(horizontal = 16.dp)
                    .verticalScroll(rememberScrollState())
        ){
            StandardInput(
                label = "Nombre",
                value = uiState.name,
                onValueChange = viewModel::onNameChange,
                placeholder = uiState.productDetail?.name ?: "Galletas de chocolate",
                isError = uiState.error?.contains("Nombre") == true
            )

            ImageUploadInput(
                label = "Imagen del producto",
                value = uiState.selectedImageUri,
                onValueChange = viewModel::onImageSelected,
                isError = uiState.error?.contains("Imagen") == true
            )

            StandardInput(
                label = "Precio Unitario",
                value = uiState.unitaryPrice,
                onValueChange = viewModel::onUnitaryPriceChange,
                placeholder = "pesos"
            )

            SelectObjectInput(
                label = "Selecciona taller",
                options = uiState.workshops,
                selectedId = uiState.selectedWorkshopId,
                onOptionSelected = viewModel::onWorkshopSelected,
                getItemName = { it.name },
                getItemId = { it.idWorkshop },
                isError = uiState.error?.contains("Taller") == true
            )

            SelectObjectInput(
                label = "Selecciona la categoría del producto",
                options = uiState.categories,
                selectedId = uiState.selectedCategoryId,
                onOptionSelected = viewModel::onCategorySelected,
                getItemName = { it.type },
                getItemId = { it.idCategory },
                isError = uiState.error?.contains("Categoría") == true
            )

            StatusSelector(
                status = uiState.status,
                onStatusChange = viewModel::onStatusChange,
                modifier = Modifier.padding(vertical = 4.dp)
            )


            DescriptionInput(
                value = uiState.description,
                onValueChange =viewModel::onDescriptionChange,
                widthFraction = 0.92f,
                maxWidthDp = 560.dp
            )

            Spacer(Modifier.height(16.dp))
            Spacer(Modifier.weight(1f))

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically,

            ) {
                if (uiState.isLoading) {
                    CircularProgressIndicator()
                } else {
                    CancelButton(onClick = onCancel)
                    SaveButton(onClick = viewModel::onSaveClick)
                }

            }
        }
    }

}
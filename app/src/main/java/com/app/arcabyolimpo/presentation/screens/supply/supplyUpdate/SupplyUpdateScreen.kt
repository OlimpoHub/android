package com.app.arcabyolimpo.presentation.screens.supply.supplyUpdate

import android.net.Uri
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.CancelButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ModifyButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.ImageUploadInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.SelectObjectInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.StatusSelector
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White
/** ---------------------------------------------------------------------------------------------- *
 * SupplyUpdateScreen -> view where all the inputs, buttons and selects are shown with the
 * information collected by the view model, handles different states and errors.
 *
 * @param viewModel: SupplyUpdateViewModel -> vm for the view in charge of placing the data
 * @param onModifyClick: () -> Unit -> function when the user modifies the supply
 * @param onCancel: () -> Unit -> function when the user cancels the operation
 * @param onBackClick: () -> Unit -> function when the user clicks the back button
 * ---------------------------------------------------------------------------------------------- */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SupplyUpdateScreen(
    viewModel: SupplyUpdateViewModel = hiltViewModel(),
    onModifyClick: () -> Unit,
    onCancel: () -> Unit,
    onBackClick: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    LaunchedEffect(
        key1 = uiState.success,
        key2 = uiState.error,
    ) {
        when {
            uiState.success -> {
                Toast.makeText(context, "Insumo modificado", Toast.LENGTH_SHORT)
                    .show()
                onModifyClick()
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
                        text = "Modificar Insumo",
                        color = White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
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
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Background // O el color que prefieras para la barra
                )
            )
        }
    ) { paddingValues ->

        if (uiState.isLoading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center,
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
            ) {
                StandardInput(
                    label = "Nombre",
                    value = uiState.name,
                    onValueChange = viewModel::onNameChange,
                    placeholder = "Harina de trigo",
                    isError = uiState.nameError != null,
                    errorMessage = uiState.nameError,
                )

                val imageUri = uiState.selectedImageUrl ?: uiState.currentImageUrl
                    ?.takeIf { it.isNotBlank() }
                    ?.let{ Uri.parse(it)}

                ImageUploadInput(
                    label = "Imagen del insumo",
                    value = imageUri,
                    onValueChange = viewModel::onImageSelected,
                )

                SelectObjectInput(
                    label = "Selecciona Taller",
                    options = uiState.workshops,
                    selectedId = uiState.selectedIdWorkshop,
                    onOptionSelected = viewModel::onWorkshopSelected,
                    getItemName = { it.name },
                    getItemId = { it.idWorkshop },
                    isError = uiState.noWorkshop != null,
                    errorMessage = uiState.noWorkshop,
                )

                SelectObjectInput(
                    label = "Selecciona la categoria del insumo",
                    options = uiState.categories,
                    selectedId = uiState.selectedIdCategory,
                    onOptionSelected = viewModel::onCategorySelected,
                    getItemName = { it.type },
                    getItemId = { it.idCategory },
                    isError = uiState.noCategory != null,
                    errorMessage = uiState.noCategory,
                )

                StandardInput(
                    label = "Unidad de medida",
                    value = uiState.measureUnit,
                    onValueChange = viewModel::onUnitMeasureChange,
                    placeholder = "Gramos",
                    isError = uiState.measureUnitError != null,
                    errorMessage = uiState.measureUnitError,
                )

                StatusSelector(
                    status = uiState.status,
                    onStatusChange = viewModel::onStatusChange,
                    modifier = Modifier.padding(vertical = 4.dp)
                )

                Spacer(Modifier.height(16.dp))
                Spacer(Modifier.weight(1f))

                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = 16.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (uiState.isSaving) {
                        CircularProgressIndicator()
                    } else {
                        CancelButton(onClick = onCancel)

                        ModifyButton(
                            onClick = viewModel::onModifyClick,
                            enabled = uiState.hadChanged
                        )
                    }
                }
            }
        }

    }
}
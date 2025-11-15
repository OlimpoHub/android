package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchRegister

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.Placeholder
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.CancelButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ModifyButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SaveButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SquareAddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SquareMinusButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.DateInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.ImageUploadInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SelectInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.presentation.ui.components.molecules.NumberStepper
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White

/** ProductBatchRegisterScreen: Composable screen for registering a new product batch.
 *
 * @param onBackClick () -> Unit -> callback for back navigation
 * @param onCreated () -> Unit -> callback when the product batch is successfully created
 * @param viewModel ProductBatchRegisterViewModel = hiltViewModel() -> ViewModel for managing UI state
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun ProductBatchRegisterScreen(
    onBackClick: () -> Unit,
    onCreated: () -> Unit,
    viewModel: ProductBatchRegisterViewModel = hiltViewModel(),
) {
    val state = viewModel.uiState

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Registrar Lote",
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
                            "Back",
                            tint = White,
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Background),
            )
        },
        bottomBar = {
            NavBar()
        },
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            SelectInput(
                label = "Selecciona el producto",
                selectedOption = "",
                options = listOf(),
                onOptionSelected = {},
            )

            StandardInput(
                label = "Precio de venta",
                value = "",
                onValueChange = {},
                placeholder = "0.00",
            )

            NumberStepper(
                label = "Cantidad producida",
                value = "",
                onValueChange = {},
                onIncrement = {},
                onDecrement = {},
            )

            NumberStepper(
                label = "Cantidad vendida",
                value = "",
                onValueChange = {},
                onIncrement = {},
                onDecrement = {},
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    DateInput(
                        label = "Fecha de Elaboración",
                        value = "",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    DateInput(
                        label = "Fecha de Caducidad",
                        value = "",
                        onValueChange = {},
                        modifier = Modifier.fillMaxWidth(),
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 16.dp), // Padding
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            CancelButton(
                onClick = onBackClick,
                modifier = Modifier.weight(1f),
            )

            SaveButton(
                onClick = onCreated,
            )
        }
    }
}

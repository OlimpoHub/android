package com.app.arcabyolimpo.presentation.screens.capacitations.disabilitiesRegister

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.CancelButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SaveButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.DescriptionInput
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White

/**
 * A Composable screen for registering a new disability.
 *
 * This screen provides a form with fields for the disability's name and its
 * characteristics. It includes input validation and communicates with a ViewModel
 * to handle the business logic of saving the data.
 *
 * @param onBackClick A callback function to be invoked when the user taps the back button,
 *                    typically used for navigation.
 * @param onCreated A callback function to be invoked upon the successful creation of a new
 *                  disability, often used to navigate away from the screen.
 * @param viewModel The ViewModel instance for this screen, provided by Hilt, which manages
 *                  the UI state and registration logic.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun DisabilitiesRegisterScreen(
    onBackClick: () -> Unit,
    onCreated: () -> Unit,
    viewModel: DisabilitiesRegisterViewModel = hiltViewModel(),
) {
    val state = viewModel.uiState

    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Registrar Discapacidad",
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
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            Row(modifier = Modifier.padding(vertical = 40.dp)) {}

            StandardInput(
                label = "Nombre",
                value = state.nombre,
                onValueChange = { viewModel.onFieldChange("nombre", it) },
                placeholder = "Nombre de la discapacidad...",
                isError = state.isNombreError,
                errorMessage = if (state.isNombreError) "Ingresa el nombre de la discapacidad" else "",
            )

            DescriptionInput(
                label = "Características",
                value = state.caracteristicas,
                onValueChange = { viewModel.onFieldChange("caracteristicas", it) },
                placeholder = "Describe la discapacidad...",
                isError = state.isCaracteristicasError,
                errorMessage = if (state.isCaracteristicasError) "Ingresa las características de la discapacidad" else "",
            )

            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 24.dp, vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    CancelButton(
                        onClick = onBackClick,
                    )
                }
                Column(
                    modifier =
                        Modifier
                            .weight(1f)
                            .padding(horizontal = 8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    SaveButton(
                        onClick = {
                            viewModel.validateAndRegister(onSuccess = onCreated)
                        },
                    )
                }
            }
        }
    }
}

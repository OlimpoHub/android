package com.app.arcabyolimpo.presentation.screens.qr.scanresult

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
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
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ReturnIcon
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.ErrorRed
import com.app.arcabyolimpo.ui.theme.White

/**
 * Composable screen displayed when the token verification process fails.
 *
 * This screen informs the user that the provided token is invalid and suggests retrying the process.
 * It includes a top app bar with a back navigation icon and displays error feedback
 * using appropriate typography and colors.
 *
 * @param onBackClick Lambda function triggered when the user taps the back button.
 */

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun ScanResultScreen(
    onBackClick: () -> Unit,
    qrValue: String?,
    viewModel: ScanResultViewModel = hiltViewModel(),
) {
    val uiStateState = viewModel.uiState.collectAsStateWithLifecycle()
    val uiState = uiStateState.value
    val readTime = System.currentTimeMillis()

    LaunchedEffect(Unit) {
        viewModel.postScanResult(qrValue, readTime)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        ReturnIcon(size = 16.dp)
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Background,
                    ),
            )
        },
        containerColor = Background,
    ) { padding ->
        Column(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 32.dp, vertical = 8.dp),
        ) {
            val response = uiState.response
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                    )
                }
                uiState.error != null -> {
                    var textError = "Error\n" + "desconocido"
                    if (uiState.error == "The attendance was already registered for the day") {
                        textError = "Ya hayas\n" +
                            "registrado\n" +
                            "tu asistencia"
                    } else if (uiState.error == "The code wasn\'t readed within the 15 first minutes of its creation.") {
                        textError = "El código QR\n" +
                            "ya se haya\n" +
                            "vencido"
                    } else if (uiState.error == "This user cannot create a QR.") {
                        textError = "No tienes\n" +
                            "acceso"
                    }
                    Text(
                        text = textError,
                        style = Typography.headlineLarge,
                        color = ErrorRed,
                    )
                }
                response != null -> {
                    Text(
                        text =
                            "Asistencia \n" +
                                "registrada\n" +
                                "exitosamente",
                        style = Typography.headlineLarge,
                        color = White,
                    )
                }
            }
        }
    }
}

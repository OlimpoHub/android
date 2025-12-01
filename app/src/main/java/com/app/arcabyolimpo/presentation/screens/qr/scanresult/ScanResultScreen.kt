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
 * Screen responsible for validating the scanned QR code and displaying the result
 * of the attendance registration attempt.
 *
 * This screen sends the scanned QR value to [ScanResultViewModel] for verification and
 * reacts to its UI state by showing loading indicators, contextual error messages, or
 * a success confirmation when the attendance is properly registered.
 *
 * ## Atomic Design Level
 * **Organism** — Composed of:
 * - Atoms: `ReturnIcon`, text blocks, loading indicator
 * - Molecules: Error and success result layouts
 * - Organism: Full result-handling workflow (request → feedback)
 *
 * ## Behavior
 * - Automatically triggers verification through `postScanResult()` when the screen loads.
 * - Handles loading, error, and success states with clear visual feedback.
 * - Maps backend error messages to user-friendly text variants.
 * - Provides navigation via the top bar back button.
 *
 * @param onBackClick Action executed when the user taps the back button.
 * @param qrValue The decoded QR content received from the scanner screen.
 * @param viewModel Injected [ScanResultViewModel] managing verification logic and UI state.
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

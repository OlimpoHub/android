package com.app.arcabyolimpo.presentation.screens.qr.scanqr

import android.Manifest
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ReturnIcon
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.ErrorRed
import com.app.arcabyolimpo.ui.theme.SelectInputBlue
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun ScanQrScreen(
    onBackClick: () -> Unit,
    onScanSuccess: (String) -> Unit,
    viewModel: ScanQrViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    var hasScanned by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val cameraPermission = Manifest.permission.CAMERA
    val previewView = remember { PreviewView(context) }
    val lifecycleOwner = LocalLifecycleOwner.current
    val onScan: () -> Unit

    val launcher =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.RequestPermission(),
        ) { granted ->
            if (granted) {
                viewModel.startCamera(previewView, lifecycleOwner)
                viewModel.postScanQr(context)
            }
        }

    LaunchedEffect(Unit) {
        viewModel.clearScanResult()
        launcher.launch(cameraPermission)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Registro de asistencia",
                        style = Typography.headlineLarge,
                        color = Color.White,
                    )
                },
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
            AndroidView(
                factory = { previewView },
                modifier =
                    Modifier
                        .height(650.dp),
            )
            Text(
                text = "Escanea el código QR",
                style = Typography.headlineLarge,
                color = Color.White,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
            )

            when {
                uiState.response?.value != null -> {
                    val qr = uiState.response!!.value
                    LaunchedEffect(qr) {
                        if (!hasScanned) {
                            hasScanned = true
                            viewModel.consumeResponse()
                            onScanSuccess(qr)
                        }
                    }
                }

                uiState.error != null -> {
                    val msg = uiState.error
                    Text(
                        text = "Error: $msg",
                        color = ErrorRed,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 2.dp).fillMaxWidth(),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

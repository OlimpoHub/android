package com.app.arcabyolimpo.presentation.screens.qr.qr

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.SetPasswordButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.KeyIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ReturnIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.StandardInput
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White
import com.app.arcabyolimpo.ui.theme.gradientBrush
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun QrScreen(
    onBackClick: () -> Unit,
    workshopId: String,
    workshopName: String,
    viewModel: QrViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.postCreateQr(workshopId)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "QR de asistencia",
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
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.TopCenter,
            ) {
                when {
                    uiState.isLoading -> {
                        CircularProgressIndicator(
                            modifier = Modifier.align(Alignment.Center),
                        )
                    }
                    uiState.qrBitmap != null -> {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(top = 24.dp),
                        ) {
                            Column(
                                modifier =
                                    Modifier
                                        .clip(RoundedCornerShape(24.dp))
                                        .background(brush = gradientBrush)
                                        .padding(20.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                Image(
                                    bitmap = uiState.qrBitmap!!.asImageBitmap(),
                                    contentDescription = "QR Code",
                                    modifier = Modifier.size(280.dp),
                                )
                                Spacer(modifier = Modifier.height(30.dp))
                                Text(
                                    text = workshopName,
                                    style = Typography.headlineLarge,
                                    color = White,
                                )
                                Spacer(modifier = Modifier.height(70.dp))
                                Box(
                                    modifier =
                                        Modifier
                                            .fillMaxWidth(),
                                    contentAlignment = Alignment.CenterStart,
                                ) {
                                    Text(
                                        modifier = Modifier.padding(start = 10.dp),
                                        text =
                                            "Hora de generación \n" +
                                                SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date()) + " " +
                                                SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date()),
                                        style = Typography.headlineSmall,
                                        color = White,
                                    )
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                            }
                            Spacer(modifier = Modifier.height(30.dp))
                            Box {
                                Text(
                                    text = "*Este código QR solo tiene 15 minutos de duración",
                                    style = Typography.headlineSmall,
                                    color = White,
                                )
                            }
                        }
                    }
                    uiState.error != null -> {
                        Text("Error: ${uiState.error}")
                    }
                }
            }
        }
    }
}

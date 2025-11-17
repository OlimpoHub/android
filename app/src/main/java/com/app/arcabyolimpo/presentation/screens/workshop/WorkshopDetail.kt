package com.app.arcabyolimpo.presentation.screens.workshop

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.domain.model.beneficiaries.Beneficiary
import com.app.arcabyolimpo.presentation.navigation.Screen
import com.app.arcabyolimpo.presentation.screens.beneficiary.BeneficiaryDetailContent
import com.app.arcabyolimpo.presentation.screens.beneficiary.BeneficiaryDetailUiState
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.DeleteButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ModifyButton
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkshopDetailScreen(
    navController: NavHostController,
    workshopId: String,
    viewModel: WorkshopDetailViewModel = hiltViewModel()
) {
    val workshop by viewModel.workshop.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        Scaffold(
            containerColor = Background,
            topBar = {
                TopAppBar(
                    title = {
                        when{
                            workshop != null -> {
                                Text(
                                    text = "${workshop?.nameWorkshop}",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            errorMessage != null ->
                                Text(
                                    text = "Error",
                                    style = MaterialTheme.typography.headlineLarge,
                                    color = Color.White,
                                    fontWeight = FontWeight.Bold
                                )
                        }

                            },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Regresar")
                        }
                    }
                )
            },
            bottomBar = { NavBar() }
        ) { padding ->

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                        .verticalScroll(rememberScrollState())
                        .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.Start
                )
                {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        when {
                            isLoading -> CircularProgressIndicator()
                            errorMessage != null -> Text(text = errorMessage ?: "")
                            workshop != null -> {
                                Column(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp),
                                    horizontalAlignment = Alignment.Start

                                ) {
                                    Text(
                                        text = "Descripción:",
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text= workshop?.description ?: "Cargando descripción...",
                                        style = MaterialTheme.typography.bodyLarge
                                    )
                                    Spacer(modifier = Modifier.height(24.dp))
                                    Text(
                                        text= "Sobre el Taller:",
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "• Hora: ${workshop?.startHour} - ${workshop?.finishHour}",
                                        style = MaterialTheme.typography.headlineMedium
                                    )
                                    Spacer(modifier = Modifier.height(12.dp))
                                    Text(
                                        text = "• Fecha: ${workshop?.date}",
                                        style = MaterialTheme.typography.headlineMedium
                                    )


                                }
                            }

                            else -> Text("No se encontró el taller")
                        }
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = 16.dp,
                            top = 12.dp
                        ),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    DeleteButton(
                        modifier = Modifier.size(width = 112.dp, height = 40.dp),
                        onClick = {

                        }
                    )

                    Spacer(modifier = Modifier.width(16.dp))

                    ModifyButton(
                        modifier = Modifier.size(width = 112.dp, height = 40.dp),
                        onClick = {
                        }
                    )
                }
            }

        }
    }
}
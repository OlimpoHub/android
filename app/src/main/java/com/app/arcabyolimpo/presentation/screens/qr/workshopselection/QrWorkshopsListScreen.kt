@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.app.arcabyolimpo.presentation.screens.qr.workshopselection

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ReturnIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SearchInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.presentation.ui.components.molecules.WorkshopCard
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QrWorkshopsListScreen(
    onBackClick: () -> Unit,
    workshopClick: (String, String) -> Unit,
    viewModel: QrWorkshopsListViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val searchText by viewModel.searchQuery.collectAsState()

    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                containerColor = Background,
                topBar = {
                    TopAppBar(
                        title = {
                            Text(
                                text = "Elige un taller",
                                style = Typography.headlineLarge,
                                color = Color.White,
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                ReturnIcon()
                            }
                        },
                        actions = {
                            Box(modifier = Modifier.padding(end = 28.dp)) {
                                NotificationIcon()
                            }
                        },
                    )
                },
                bottomBar = {
                    Box(modifier = Modifier.padding(bottom = 8.dp)) {
                        NavBar()
                    }
                },
            ) { padding ->
                Column(
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .padding(
                                top = padding.calculateTopPadding(),
                                bottom = padding.calculateBottomPadding(),
                            ),
                ) {
                    Row(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 30.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        SearchInput(
                            value = searchText,
                            onValueChange = { viewModel.onSearchQueryChange(it) },
                            modifier =
                                Modifier
                                    .weight(1f)
                                    .padding(end = 20.dp),
                        )

                        FilterIcon(
                            modifier =
                                Modifier
                                    .padding(start = 16.dp),
                        )
                    }

                    Spacer(modifier = Modifier.padding(vertical = 10.dp))

                    when {
                        uiState.isLoading -> {
                            Box(Modifier.fillMaxSize()) {
                                CircularProgressIndicator(
                                    modifier = Modifier.align(Alignment.Center),
                                )
                            }
                        }

                        uiState.error != null -> {
                            Box(Modifier.fillMaxSize()) {
                                Text(
                                    text = uiState.error ?: "Error desconocido",
                                    modifier = Modifier.align(Alignment.Center),
                                    color = MaterialTheme.colorScheme.error,
                                )
                            }
                        }

                        uiState.workshopsList.isNullOrEmpty() -> {
                            Box(Modifier.fillMaxSize()) {
                                Text(
                                    text = "No hay talleres registrados",
                                    modifier = Modifier.align(Alignment.Center),
                                )
                            }
                        }

                        else -> {
                            LazyColumn(
                                modifier =
                                    Modifier
                                        .fillMaxSize()
                                        .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalArrangement = Arrangement.spacedBy(40.dp),
                                horizontalAlignment = Alignment.CenterHorizontally,
                            ) {
                                items(uiState.workshopsList) { workshop ->
                                    WorkshopCard(
                                        name = workshop.nameWorkshop.toString(),
                                        onClick = { workshopClick(workshop.id.toString(), workshop.nameWorkshop.toString()) },
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

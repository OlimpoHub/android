package com.app.arcabyolimpo.presentation.screens.capacitations

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.R
import com.app.arcabyolimpo.presentation.screens.session.SessionViewModel
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.DecisionDialog
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.DeleteButton
import com.app.arcabyolimpo.presentation.ui.components.molecules.TextValue
import com.app.arcabyolimpo.ui.theme.Background
import kotlinx.coroutines.launch

@Composable
fun DisabilityDetailScreen(
    onBackClick: () -> Unit,
    viewModel: DisabilityDetailViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var showDeleteDialog by remember { mutableStateOf(false) }

    LaunchedEffect(uiState.deleteSuccess, uiState.deleteError) {
        if (uiState.deleteSuccess) {
            scope.launch {
                snackbarHostState.showSnackbar("Discapacidad eliminada correctamente")
            }
        }

        val error = uiState.deleteError
        if (error != null) {
            scope.launch {
                snackbarHostState.showSnackbar(error)
            }
            viewModel.onDeletionErrorShown()
        }
    }

    DisabilityDetailContent(
        uiState = uiState,
        snackbarHostState = snackbarHostState,
        showDeleteDialog = showDeleteDialog,
        onBackClick = onBackClick,
        onDeleteClick = viewModel::onDeleteClicked,
        onShowDialog = { showDeleteDialog = true },
        onDismissDialog = { showDeleteDialog = false },
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisabilityDetailContent(
    uiState: DisabilityDetailUiState,
    snackbarHostState: SnackbarHostState,
    showDeleteDialog: Boolean,
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onShowDialog: () -> Unit,
    onDismissDialog: () -> Unit,
    sessionViewModel: SessionViewModel = hiltViewModel(),
) {
    val role by sessionViewModel.role.collectAsState()

    if (showDeleteDialog) {
        DecisionDialog(
            dialogTitle = "¿Seguro que quieres eliminar a ${uiState.disability?.name ?: "..."}?",
            dialogText = "No podrá recuperarse una vez se haya eliminado.",
            onDismissRequest = onDismissDialog,
            onConfirmation = {
                onDismissDialog()
                onDeleteClick()
                onBackClick()
            },
            confirmText = "Confirmar",
            dismissText = "Cancelar"
        )
    }

    Scaffold(
        containerColor = Background,
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = uiState.disability?.name ?: "Nombre no encontrado",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.Transparent,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_return_icon),
                            contentDescription = "Volver"
                        )
                    }
                }
            )
        },
    ) { paddingValues ->
        when {
            uiState.isScreenLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            uiState.screenError != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = uiState.screenError!!, color = Color.Red)
                }
            }

            uiState.disability != null -> {
                val disability = uiState.disability

                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(horizontal = 24.dp)
                        .fillMaxSize()
                ) {
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .verticalScroll(rememberScrollState()),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Spacer(modifier = Modifier.height(24.dp))

                        TextValue(
                            label = "Detalles de la discapacidad",
                            value = disability?.characteristics ?: ""
                        )

                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (role == "COORDINADOR") {
                            DeleteButton(
                                modifier = Modifier.size(width = 112.dp, height = 40.dp),
                                onClick = { onShowDialog() }
                            )
                        }
                    }
                }
            }
        }
    }
}
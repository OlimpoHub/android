package com.app.arcabyolimpo.presentation.screens.user.detail

import androidx.compose.foundation.layout.*
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.arcabyolimpo.presentation.screens.user.detail.components.UserDetailContent
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.DecisionDialog
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ReturnIcon
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner




@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    viewModel: UserDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {},
) {
    val uiState by viewModel.uiState.collectAsState()
    val scaffoldState = rememberScaffoldState()
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var showConfirmDialog by remember { mutableStateOf(false) }

    val lifecycleOwner = LocalLifecycleOwner.current

    // Cada vez que la pantalla entra en ON_RESUME, recargamos el detalle
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                viewModel.loadCollabDetail()
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }


    LaunchedEffect(uiState.deleted, uiState.deleteError) {
        if (uiState.deleted) {
            snackbarHostState.showSnackbar("El colaborador fue eliminado correctamente")
            onDeleteClick()
            viewModel.resetDeleteState()
        } else if (uiState.deleteError != null) {
            snackbarHostState.showSnackbar("Error al eliminar: ${uiState.deleteError}")
            viewModel.resetDeleteState()
        }
    }

    Scaffold(
        containerColor = Color(0xFF040610),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Detalles del Usuario",
                        color = Color.White,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        ReturnIcon(tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF040610)
                )
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White
                    )
                }

                uiState.error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: ${uiState.error}",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { viewModel.loadCollabDetail() }) {
                            Text("Reintentar")
                        }
                    }
                }

                uiState.collab != null -> {
                    UserDetailContent(
                        collab = uiState.collab!!,
                        onEditClick = { uiState.collab?.idUsuario?.let { onEditClick(it.toString()) } },
                        onDeleteClick = { showConfirmDialog = true }
                    )
                }
            }

            if (showConfirmDialog) {
                DecisionDialog(
                    onDismissRequest = { showConfirmDialog = false },
                    onConfirmation = {
                        showConfirmDialog = false
                        // lanzar delete
                        uiState.collab?.idUsuario?.toString()?.let { idStr ->
                            viewModel.deleteCollabById(idStr)
                        }
                    },
                    dialogTitle = "Eliminar colaborador",
                    dialogText = "¿Estás seguro que deseas eliminar este colaborador? Esta acción no se puede deshacer.",
                    confirmText = "Eliminar",
                    dismissText = "Cancelar",
                )
            }

            if (uiState.deleteLoading) {
                // un pequeño overlay o progress en centro
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}
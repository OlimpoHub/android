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
import com.app.arcabyolimpo.presentation.ui.components.atoms.alerts.Snackbarcustom
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ReturnIcon
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner

/**
 * Displays detailed information about a specific user with edit and delete capabilities.
 *
 * This composable screen presents a comprehensive view of a user's information including
 * their profile photo, personal details, role, status, and associated documents. It provides
 * functionality for editing user information, deleting users (marking them as inactive),
 * and viewing attendance records for volunteer users.
 *
 * The screen implements lifecycle-aware data loading, automatically refreshing the user
 * details whenever the screen resumes (e.g., after returning from the edit screen). This
 * ensures the displayed information is always current without requiring manual refresh actions.
 *
 * User deletion is performed with a confirmation dialog and provides feedback through
 * snackbar notifications. The delete button is visually disabled for users who are already
 * inactive, preventing redundant deletion attempts.
 *
 * @param viewModel The ViewModel managing the user detail state and business logic. Injected
 *                  via Hilt, it handles data fetching, deletion operations, and state management.
 * @param onBackClick Callback invoked when the user taps the back button in the top app bar,
 *                    typically navigating back to the previous screen.
 * @param onEditClick Callback invoked when the user taps the edit button, receiving the user's
 *                    ID as a parameter to navigate to the edit screen with the correct user loaded.
 * @param onDeleteClick Callback invoked after successful user deletion, typically navigating
 *                      back to the user list screen.
 * @param onAttendanceClick Callback invoked when the "Ver asistencias" button is tapped for
 *                          volunteer users, receiving the user's ID to navigate to their attendance records.
 */
@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserDetailScreen(
    viewModel: UserDetailViewModel = hiltViewModel(),
    onBackClick: () -> Unit,
    onEditClick: (String) -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onAttendanceClick: (String) -> Unit = {},
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
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        ReturnIcon(tint = Color.White)
                    }
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF040610),
                    ),
            )
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->

                Box(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                    contentAlignment = Alignment.Center,
                ) {
                    Snackbarcustom(
                        title = data.visuals.message,
                        modifier =
                            Modifier
                                .fillMaxWidth(0.85f),
                        ifSucces = uiState.deleteError == null,
                    )
                }
            }
        },
    ) { padding ->
        Box(
            modifier =
                Modifier
                    .fillMaxSize()
                    .padding(padding),
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center),
                        color = Color.White,
                    )
                }

                uiState.error != null -> {
                    Column(
                        modifier =
                            Modifier
                                .align(Alignment.Center)
                                .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                    ) {
                        Text(
                            text = "Error: ${uiState.error}",
                            color = Color.White,
                            style = MaterialTheme.typography.bodyLarge,
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
                        onDeleteClick = { showConfirmDialog = true },
                        onAttendanceClick = { uiState.collab?.idUsuario?.let { id ->
                            onAttendanceClick(id.toString())
                        } }
                    )
                }
            }

            if (showConfirmDialog) {
                DecisionDialog(
                    onDismissRequest = { showConfirmDialog = false },
                    onConfirmation = {
                        showConfirmDialog = false
                        uiState.collab?.idUsuario?.toString()?.let { idStr ->
                            viewModel.deleteCollabById(idStr)
                        }
                    },
                    dialogTitle = "¿Está seguro de eliminar a este colaborador?",
                    dialogText = "Esta acción marcará al colaborador como inactivo.",
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

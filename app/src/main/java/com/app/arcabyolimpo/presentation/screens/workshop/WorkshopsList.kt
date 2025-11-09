package com.app.arcabyolimpo.presentation.screens.workshop

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.presentation.navigation.Screen
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton
import com.app.arcabyolimpo.ui.theme.Background

@Suppress("ktlint:standard:function-naming")
@Composable
fun WorkshopsListScreen(
    navController: NavHostController,
    workshopClick: (String) -> Unit,
    viewModel: WorkshopsListViewModel = hiltViewModel()
)  {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Background,
        floatingActionButton = {
            AddButton(
                onClick = {
                    navController.navigate(Screen.AddNewWorkshop.route)
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // 👉 Aquí colocarás la lista de talleres (LazyColumn, etc.)
        }
    }
}

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

/**
 * Composable screen that displays the list of workshops.
 *
 * This screen is responsible for showing a list of available workshops
 * retrieved from the [WorkshopsListViewModel]. It provides:
 * - A floating action button for adding new workshops.
 *
 * @param navController The way to go through different screens that are in the [NavGraph]
 * @param workshopClick Callback triggered when a workshop item is clicked.
 * @param viewModel The [WorkshopsListViewModel] used to manage the UI state.
 */
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
        }
    }
}

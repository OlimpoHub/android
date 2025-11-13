package com.app.arcabyolimpo.presentation.screens.beneficiary
/*
Acalaracion de Ricardo: Muchas de las cosas que se estan haciendo aqui no estan incluyendo muchos de
los atomos creados, pero el motivo es que eso no me toca a mi, yo cree la screen para que se pudiese
trabajar en mi propia HS, como tal arreglar esta screen va al que le toque la historia de consultar
beneficiarios.
 */
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.app.arcabyolimpo.R
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.molecules.BeneficiaryCard
import com.app.arcabyolimpo.presentation.ui.components.organisms.Filter
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme

data class BeneficiaryDemo(
    val id: String,
    val name: String,
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BeneficiaryList(
    onBeneficiaryClick: (String) -> Unit, // Pasa el ID del beneficiario
    onFilterClick: () -> Unit,
    onNotificationClick: () -> Unit,
    viewModel: BeneficiaryDetailViewModel
) {
    // State variable to track whether the filter modal is visible
    var showFilter by remember { mutableStateOf(false) }
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    // Datos de ejemplo (se remplaza con datos del ViewModel)
    val beneficiaries =
        listOf(
            BeneficiaryDemo("0f12b075-bd1d-11f0-b6b8-020161fa237d", "John Smith 1"),
            BeneficiaryDemo("127fd0f3-bd1e-11f0-b6b8-020161fa237d", "John Smith 2"),
            BeneficiaryDemo("73aab7d7-bd1a-11f0-b6b8-020161fa237d", "John Smith 3"),
            BeneficiaryDemo("ad469db9-bd1b-11f0-b6b8-020161fa237d", "John Smith 4"),
        )
    var searchText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Beneficiarios", style = Typography.headlineMedium.copy()) },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Color.Transparent,
                        titleContentColor = Color.White,
                        actionIconContentColor = Color.White,
                    ),
                actions = {
                    IconButton(onClick = onNotificationClick) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_notification_icon),
                            contentDescription = "Notificaciones",
                            tint = Color.White.copy(),
                        )
                    }
                    /**
                     * Icon button used to open the filter panel for supplies.
                     * When clicked, it sets showFilter to `true`, triggering the display
                     * of the filter UI.
                     *
                     * @param modifier Compose Modifier used to define size, padding, and click behavior.
                     */
                    FilterIcon(
                        modifier =
                            Modifier
                                .size(40.dp)
                                .padding(top = 16.dp)
                                .clickable {
                                    showFilter = true
                                },
                    )
                },
            )
        },
        containerColor = Color(0xFF1C1B1F),
    ) { paddingValues ->
        Column(
            modifier =
                Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 16.dp)
                    .fillMaxSize(),
        ) {
            TextField(
                value = searchText,
                onValueChange = { searchText = it },
                label = { Text("Buscar") },
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_search_icon),
                        contentDescription = null,
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                // Aquí se aplican estilo de atoms
            )

            // Cuadricula de Beneficiarios
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(vertical = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                items(beneficiaries.size) { index ->
                    val beneficiary = beneficiaries[index]
                    BeneficiaryCard(
                        name = beneficiary.name,
                        onClick = { onBeneficiaryClick(beneficiary.id) },
                    )
                }
            }
        }
    }
    /**
     * Displays the filter panel when showFilter is true and filter data is available.
     *
     * This composable allows the user to apply, dismiss, or clear filters on the supply list.
     *
     * Behavior:
     * - onApply: Called when the user applies a filter. Passes the selected filter DTO
     *   to the ViewModel via viewModel.filterSupplies and closes the filter panel.
     * - onDismiss: Called when the user dismisses the filter panel without applying changes.
     *   Simply closes the panel by setting showFilter to false.
     * - onClearFilters: Clears all selected filters in the ViewModel, reloads the full supply list,
     *   and closes the filter panel.
     *
     * Preconditions:
     * - uiState.filterData must not be null; otherwise, the filter panel is not displayed.
     */
    if (showFilter && uiState.filterData != null) {
        Filter(
            data = uiState.filterData!!,
            initialSelected = uiState.selectedFilters,
            onApply = { dto ->

                showFilter = false
            },
            onDismiss = { showFilter = false },
            onClearFilters = {
                viewModel.clearFilters()
                viewModel.loadSuppliesList()
                showFilter = false
            },
        )
    }
}

@Preview(showBackground = true, backgroundColor = 0xFF1C1B1F)
@Composable
fun BeneficiaryListPreview() {
    ArcaByOlimpoTheme {
        BeneficiaryList(
            onBeneficiaryClick = {},
            onFilterClick = {},
            onNotificationClick = {},
        )
    }
}

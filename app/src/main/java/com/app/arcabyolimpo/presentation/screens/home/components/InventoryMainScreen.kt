package com.app.arcabyolimpo.presentation.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.R
import com.app.arcabyolimpo.presentation.ui.components.molecules.HomeScreenCard

/**
 * Displays the main inventory menu with cards for Products, Product Batches, and Supplies.
 *
 * @param paddingValues Applied scaffold padding.
 * @param onSelect Callback invoked when a card is selected.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun InventoryMainScreen(
    paddingValues: PaddingValues,
    onSelect: (String) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 24.dp, horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(36.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HomeScreenCard(
            name = "Productos",
            image = painterResource(id = R.drawable.img_product),
            onClick = { onSelect("products") },
        )

        HomeScreenCard(
            name = "Lotes de Productos",
            image = painterResource(id = R.drawable.img_product_batch),
            onClick = { onSelect("product_batches") },
        )

        HomeScreenCard(
            name = "Insumos",
            image = painterResource(id = R.drawable.img_supplies),
            onClick = { onSelect("supplies") },
        )
    }
}

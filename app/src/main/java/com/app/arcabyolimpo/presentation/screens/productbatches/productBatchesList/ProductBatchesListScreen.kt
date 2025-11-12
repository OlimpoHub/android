package com.app.arcabyolimpo.presentation.screens.productbatches.productBatchesList

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.presentation.screens.productbatches.model.ProductBatchUiModel
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.SearchIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.inputs.SearchInput
import com.app.arcabyolimpo.presentation.ui.components.molecules.NavBar
import com.app.arcabyolimpo.presentation.ui.components.molecules.ProductBatchItem
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun ProductBatchesListScreen(modifier: Modifier = Modifier) {
    val mockData =
        List(8) {
            ProductBatchUiModel(
                idProducto = "p${it + 1}",
                nombre = "Galletas de Chocolate",
                precioUnitario = "1.00",
                descripcion = "Galletas",
                imagen = "", // La URL de la imagen
                disponible = true,
                idInventario = "inv${it + 1}",
                precioVenta = "1.50",
                cantidadProducida = 31,
                fechaCaducidad = "2026-01-01T06:00:00.000Z",
                fechaRealizacion = "2025-12-20T06:00:00.000Z", // Fecha de la imagen
            )
        }

    var text by remember { mutableStateOf("") }

    Scaffold(
        containerColor = Background,
        bottomBar = {
            NavBar()
        },
        floatingActionButton = {
            AddButton(onClick = {})
        },
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Lotes de Productos",
                        color = White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        style = MaterialTheme.typography.headlineLarge,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                    )
                },
                actions = {
                    NotificationIcon(
                        modifier =
                            Modifier
                                .padding(horizontal = 24.dp)
                                .size(24.dp),
                    )
                },
                colors =
                    TopAppBarDefaults.topAppBarColors(
                        containerColor = Background,
                    ),
            )
        },
    ) { padding ->
        Column(
            modifier =
                modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(horizontal = 16.dp),
        ) {
            Row(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                SearchInput(
                    value = text,
                    onValueChange = { text = it },
                    trailingIcon = { SearchIcon() },
                    modifier = Modifier.weight(1f),
                )

                IconButton(onClick = {}) {
                    FilterIcon(
                        Modifier.size(32.dp),
                    )
                }
            }

            LazyColumn(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),
                verticalArrangement = Arrangement.spacedBy(0.dp),
            ) {
                items(mockData) { batch ->
                    ProductBatchItem(
                        batch = batch,
                        onClick = {},
                    )
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewProductBatchesListScreen() {
    ArcaByOlimpoTheme(
        darkTheme = true,
        dynamicColor = false,
    ) {
        ProductBatchesListScreen()
    }
}

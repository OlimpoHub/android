@file:OptIn(ExperimentalMaterial3Api::class)

@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.app.arcabyolimpo.presentation.ui.components.organisms

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.ui.theme.PopupBackgroundBlue
import com.app.arcabyolimpo.ui.theme.White
import kotlinx.coroutines.launch

@Suppress("ktlint:standard:function-naming")
@Composable
fun BottomSheetFilterDemo(
    filterOptions: List<FilterOption>,
    onOptionSelected: (String) -> Unit = {},
) {
    var showBottomSheet by remember { mutableStateOf(true) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = PopupBackgroundBlue,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .height(400.dp)
                        .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Filtrar & Ordenar",
                    style = MaterialTheme.typography.titleMedium,
                    color = White,
                )

                Spacer(Modifier.height(16.dp))

                filterOptions.forEach { option ->
                    var expanded by remember { mutableStateOf(false) }

                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                    ) {
                        Button(
                            onClick = {
                                if (option.subOptions.isEmpty()) {
                                    onOptionSelected(option.title)
                                    scope
                                        .launch { sheetState.hide() }
                                        .invokeOnCompletion { showBottomSheet = false }
                                } else {
                                    expanded = !expanded
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(option.title)
                        }

                        DropdownMenu(
                            expanded = expanded,
                            onDismissRequest = { expanded = false },
                        ) {
                            option.subOptions.forEach { sub ->
                                DropdownMenuItem(
                                    text = { Text(sub) },
                                    onClick = {
                                        onOptionSelected(sub)
                                        expanded = false
                                        scope
                                            .launch { sheetState.hide() }
                                            .invokeOnCompletion { showBottomSheet = false }
                                    },
                                )
                            }
                        }
                    }
                }

                Spacer(Modifier.height(16.dp))

                OutlinedButton(
                    onClick = {
                        scope
                            .launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) showBottomSheet = false
                            }
                    },
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    Spacer(Modifier.width(8.dp))
                    Text("Cerrar")
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true, heightDp = 800)
@Composable
fun PreviewBottomSheetFilterDemo() {
    val filters =
        listOf(
            FilterOption("Tipo de categoría", listOf("Producto", "Insumo", "Servicio")),
            FilterOption("Fecha"),
            FilterOption("Estado", listOf("Activo", "Inactivo")),
        )

    MaterialTheme {
        BottomSheetFilterDemo(filterOptions = filters)
    }
}

data class FilterOption(
    val title: String,
    val subOptions: List<String> = emptyList(),
)

/*
@Suppress("ktlint:standard:function-naming")
@Composable
fun BottomSheetPreviewDemo() {
    var showBottomSheet by remember { mutableStateOf(true) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState,
            containerColor = PopupBackgroundBlue,
        ) {
            Column(
                modifier =
                    Modifier
                        // .fillMaxSize()
                        .fillMaxWidth()
                        .height(450.dp)
                        .padding(16.dp),
                // .height(400.dp),
                // .background(Color.LightGray),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = "Filtrar & Ordenar",
                    style = MaterialTheme.typography.titleMedium,
                    color = White,
                )

                Spacer(Modifier.height(16.dp))

                Button(
                    onClick = {
                        scope
                            .launch {
                                sheetState.hide()
                            }.invokeOnCompletion {
                                if (!sheetState.isVisible) {
                                    showBottomSheet = false
                                }
                            }
                    },
                ) {
                    Icon(Icons.Default.Close, contentDescription = "Cerrar")
                    Spacer(Modifier.width(8.dp))
                    Text("Cerrar")
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(
    showBackground = true,
    // showSystemUi = true,
    heightDp = 800,
)
@Composable
fun PreviewBottomSheetDemo() {
    MaterialTheme {
        BottomSheetPreviewDemo()
    }
}
*/

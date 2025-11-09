@file:Suppress("ktlint:standard:no-wildcard-imports")

package com.app.arcabyolimpo.presentation.ui.components.organisms

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.domain.model.supplies.FilterData
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ExitIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.ui.theme.HeaderBackground
import com.app.arcabyolimpo.ui.theme.White
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Filter(data: FilterData) {
    var openBottomSheet by rememberSaveable { mutableStateOf(true) } // Abierto por default para preview
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

    val sections = data.asSections()
    println(data)

    // Estado para guardar selecciones
    val selectedMap =
        remember {
            mutableStateMapOf<String, MutableList<String>>().apply {
                sections.forEach { section ->
                    this[section.title] = mutableStateListOf()
                }
            }
        }

    // Contenido de la pantalla principal
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Button(onClick = { openBottomSheet = true }) {
            Text("Abrir Bottom Sheet")
        }
    }

    // Contenido del Bottom Sheet
    if (openBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { openBottomSheet = false },
            sheetState = bottomSheetState,
            containerColor = HeaderBackground,
            contentColor = White,
        ) {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(start = 40.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth(),
                ) {
                    FilterIcon(tint = White, size = 35.dp)

                    Spacer(
                        modifier =
                            Modifier
                                .width(8.dp)
                                .padding(top = 50.dp),
                    )

                    Text(
                        "Filtrar & Ordenar",
                        style = Typography.headlineSmall,
                        modifier = Modifier.padding(top = 10.dp),
                    )

                    Spacer(
                        modifier =
                            Modifier
                                .width(160.dp),
                    )

                    ExitIcon(
                        modifier =
                            Modifier
                                .padding(top = 5.dp),
                        tint = White,
                        size = 25.dp,
                    )
                }

                Spacer(
                    modifier =
                        Modifier
                            .width(160.dp),
                )

                sections.forEach { (title, items) ->
                    FilterExpandableSection(
                        title = title,
                        items = items,
                        selectedItems = selectedMap[title]!!,
                    )
                }

                // Aquí puedes agregar tus dropdowns, botones, filtros, etc.
                Button(onClick = {
                    scope
                        .launch { bottomSheetState.hide() }
                        .invokeOnCompletion { if (!bottomSheetState.isVisible) openBottomSheet = false }
                }) {
                    Text("Cerrar")
                }

                Button(onClick = {
                    println("SELECTED MAP : $selectedMap")
                    println("CATEGORÍAS seleccionadas: ${selectedMap["Categorías"]}")
                    println("MEDIDAS seleccionadas: ${selectedMap["Medidas"]}")
                    println("TALLERES seleccionados: ${selectedMap["Talleres"]}")

                    scope
                        .launch { bottomSheetState.hide() }
                        .invokeOnCompletion { if (!bottomSheetState.isVisible) openBottomSheet = false }
                }) {
                    Text("Aplicar filtros")
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun FilterExpandableSection(
    title: String,
    items: List<String>,
    selectedItems: MutableList<String>,
) {
    var expanded by remember { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxWidth(),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .clickable { expanded = !expanded },
        ) {
            Text(
                text = title,
                style = Typography.headlineSmall,
            )
        }

        if (expanded) {
            Column(
                modifier = Modifier.padding(start = 10.dp, top = 4.dp),
            ) {
                items.forEach { item ->
                    val checked = item in selectedItems

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    if (checked) {
                                        selectedItems.remove(item)
                                    } else {
                                        selectedItems.add(item)
                                    }
                                }.padding(vertical = 4.dp),
                    ) {
                        Checkbox(
                            checked = checked,
                            onCheckedChange = { isChecked ->
                                if (isChecked) {
                                    selectedItems.add(item)
                                } else {
                                    selectedItems.remove(item)
                                }
                            },
                        )

                        Text(
                            text = item,
                            style = Typography.bodyMedium,
                        )
                    }
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun FilterPreview() {
    Filter(
        data =
            FilterData(
                categories = listOf("Herramientas", "Materiales", "Electrónica"),
                measures = listOf("pieza", "metros", "unidad"),
                workshops = listOf("Taller Carpintería", "Taller Electrónica", "Taller Web"),
            ),
    )
}

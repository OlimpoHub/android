package com.app.arcabyolimpo.presentation.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.ui.theme.HeaderBackground
import com.app.arcabyolimpo.ui.theme.White
import kotlinx.coroutines.launch

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Filter() {
    var openBottomSheet by rememberSaveable { mutableStateOf(true) } // Abierto por default para preview
    val bottomSheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val scope = rememberCoroutineScope()

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

                    Text("Filtrar & Ordenar", style = MaterialTheme.typography.bodyMedium)
                }
                Text("Título del Bottom Sheet", style = MaterialTheme.typography.titleMedium)

                // Aquí puedes agregar tus dropdowns, botones, filtros, etc.
                Button(onClick = {
                    scope
                        .launch { bottomSheetState.hide() }
                        .invokeOnCompletion { if (!bottomSheetState.isVisible) openBottomSheet = false }
                }) {
                    Text("Cerrar")
                }
            }
        }
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun FilterPreview() {
    Filter()
}

package com.app.arcabyolimpo.presentation.ui.components.organisms

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.arcabyolimpo.presentation.screens.supply.supplyBatchList.SupplyBatchListUiState
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.molecules.SupplyBatchListItem
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White

private fun formatDisplayDate(dateStr: String): String {
    if (dateStr.isBlank()) return ""
    return try {
        // if ISO yyyy-MM-dd -> convert to dd/MM/yyyy
        if (dateStr.contains('-')) {
            val parts = dateStr.split('-')
            if (parts.size == 3) {
                val y = parts[0]
                val m = parts[1]
                val d = parts[2]
                "$d/$m/$y"
            } else {
                dateStr
            }
        } else if (dateStr.contains('/')) {
            // already dd/MM/yyyy
            dateStr
        } else {
            dateStr
        }
    } catch (e: Exception) {
        dateStr
    }
}

@Composable
fun SupplyBatchListContent(
    uiState: SupplyBatchListUiState,
    date: String,
    onBackClick: () -> Unit,
    onModifyClick: (String) -> Unit,
) {
    val list = uiState.supplyBatchList?.batch ?: emptyList()
    Log.d("SupplyBatchListContent", "date=$date, batchesSize=${list.size}")

    if (list.isEmpty()) {
        // show small fallback to help debugging when the API returned no items
        Box(modifier = Modifier.fillMaxSize()) {
            Text(
                text = "No hay lotes para la fecha ${if (date.isBlank()) "(todas)" else date}",
                color = White,
                fontFamily = Poppins,
                fontSize = 16.sp,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                textAlign = TextAlign.Center,
            )
        }
        return
    }

    LazyColumn {
        item {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
            ) {
                Text(
                    text = "Fecha de caducidad",
                    color = White,
                    fontFamily = Poppins,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
                Text(
                    text = formatDisplayDate(date),
                    color = White,
                    fontFamily = Poppins,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                )
            }
        }

        itemsIndexed(list) { index, item ->
            SupplyBatchListItem(
                index = index,
                batchId = item.id,
                quantity = item.quantity,
                expirationDate = formatDisplayDate(item.expirationDate),
                adquisition = item.adquisitionType,
                boughtDate = item.boughtDate,
                measure = item.measure,
                onModifyClick = { id -> onModifyClick(id) },
            )
        }
    }
}

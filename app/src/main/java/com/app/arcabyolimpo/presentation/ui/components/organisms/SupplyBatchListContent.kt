package com.app.arcabyolimpo.presentation.ui.components.organisms

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.arcabyolimpo.presentation.screens.supply.supplyBatchList.SupplyBatchListUiState
import com.app.arcabyolimpo.presentation.theme.Poppins
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
            } else dateStr
        } else if (dateStr.contains('/')) {
            // already dd/MM/yyyy
            dateStr
        } else dateStr
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

    LazyColumn {
        itemsIndexed(list) { index, item ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    // Left: index and main info
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "${index + 1}",
                            color = White,
                            fontFamily = Poppins,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                        )
                        Text(
                            text = "Cantidad: ${item.quantity} ${item.measure}",
                            color = White,
                            fontFamily = Poppins,
                            fontSize = 14.sp,
                        )
                        Text(
                            text = "Tipo de adquisición: ${item.adquisitionType}",
                            color = White,
                            fontFamily = Poppins,
                            fontSize = 14.sp,
                        )
                    }

                    // Right: dates and actions
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Fecha de Compra",
                            color = White,
                            fontFamily = Poppins,
                            fontSize = 12.sp,
                        )
                        Text(
                            text = formatDisplayDate(item.boughtDate),
                            color = White,
                            fontFamily = Poppins,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        Text(
                            text = "Caducidad",
                            color = White,
                            fontFamily = Poppins,
                            fontSize = 12.sp,
                        )
                        Text(
                            text = formatDisplayDate(item.expirationDate),
                            color = White,
                            fontFamily = Poppins,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                        )

                        IconButton(onClick = { onModifyClick(item.id) }) {
                            Icon(imageVector = Icons.Default.Create, contentDescription = "modify", tint = White)
                        }
                    }
                }
            }
        }
    }
}

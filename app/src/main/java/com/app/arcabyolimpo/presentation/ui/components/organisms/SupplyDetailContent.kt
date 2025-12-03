package com.app.arcabyolimpo.presentation.ui.components.organisms

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app.arcabyolimpo.domain.model.supplies.Batch
import com.app.arcabyolimpo.domain.model.supplies.SupplyBatchExt
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AgregateButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.DeleteButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ModifyButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.ActiveStatus
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.InactiveStatus
import com.app.arcabyolimpo.presentation.ui.components.molecules.SupplyBatchRow
import com.app.arcabyolimpo.presentation.ui.components.molecules.TextValue
import com.app.arcabyolimpo.ui.theme.White
import formatExpirationDate
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

private fun formatToIso(dateStr: String): String {
    if (dateStr.isBlank()) return ""
    return try {
        // If the date is an ISO datetime like 2025-11-21T06:00:00.000Z, extract the date part
        if (dateStr.contains('T')) {
            val datePart = dateStr.substringBefore('T')
            // basic validation: yyyy-MM-dd
            if (Regex("\\d{4}-\\d{2}-\\d{2}").matches(datePart)) return datePart
            return datePart
        }

        // Expecting dd/MM/yyyy -> convert to yyyy-MM-dd
        if (dateStr.contains("/")) {
            val parts = dateStr.split('/')
            if (parts.size == 3) {
                val d = parts[0].toIntOrNull() ?: return dateStr
                val m = parts[1].toIntOrNull() ?: return dateStr
                val y = parts[2].toIntOrNull() ?: return dateStr
                String.format("%04d-%02d-%02d", y, m, d)
            } else {
                dateStr
            }
        } else {
            // If already looks like yyyy-MM-dd or another format, return as-is
            dateStr
        }
    } catch (e: Exception) {
        dateStr
    }
}

/** ---------------------------------------------------------------------------------------------- *
 * SupplyDetailContent -> Its the container of the view that its just called in the main
 * SuppliesDetailScreen, has the information of the Supply and its batches and its the bridge to
 * other functionalities like modify or delete a Supply
 *
 * @param supply: SupplyBatchExt -> supply fetched
 * @param onClickAddSupplyBatch: () -> Unit -> function when you want to add a new supply
 * @param onClickDelete: () -> Unit -> function when you want to delete the supply
 * @param onClickModify: () -> Unit -> function when you want to modify the supply
 * @param modifySupplyBatch: () -> Unit -> function when you want to modify a SUPPLY BATCH
 * @param deleteSupplyBatch: () -> Unit -> function when you want to delete a SUPPLY BATCH
 * ---------------------------------------------------------------------------------------------- */
@Composable
fun SupplyDetailContent(
    supply: SupplyBatchExt,
    filteredBatches: List<Batch>? = null,
    onClickAddSupplyBatch: () -> Unit,
    onClickDelete: () -> Unit,
    onClickModify: () -> Unit,
    viewAllBatches: (String, String) -> Unit,
    deleteSupplyBatch: (String) -> Unit,
    onFilterClick: () -> Unit,
) {
    val batches = filteredBatches ?: supply.batch
    val scrollState = rememberScrollState()

    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(scrollState),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            modifier =
                Modifier
                    .fillMaxWidth(),
        ) {
            AsyncImage(
                model = "http://74.208.78.8:8080/" + supply.imageUrl,
                contentDescription = "imagen de ${supply.name}",
                modifier =
                    Modifier
                        .size(160.dp)
                        .background(
                            Color(0xFF2A2A2A),
                            shape = RoundedCornerShape(20.dp),
                        ),
            )

            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                TextValue(
                    label = "Taller",
                    value = supply.workshop,
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextValue(
                    label = "Categoría",
                    value = supply.category,
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = "Estatus",
                        color = White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                    )
                    if (supply.status == 1) {
                        ActiveStatus()
                    } else {
                        InactiveStatus()
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        TextValue(
            label = "Unidad de Medida",
            value = supply.unitMeasure,
        )
        Spacer(modifier = Modifier.height(24.dp))
        TextValue(
            label = "Cantidad actual",
            value = "${supply.totalQuantity}",
        )

        Spacer(
            modifier =
                Modifier
                    .height(28.dp),
        )

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier =
                Modifier
                    .fillMaxWidth(),
        ) {
            Text(
                text = "Inventario",
                color = White,
                fontFamily = Poppins,
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp,
            )

            Row(verticalAlignment = Alignment.CenterVertically) {
                if (batches.isNotEmpty() && batches[0].expirationDate.isNotEmpty()) {
                    FilterIcon(
                        modifier = Modifier
                            .size(28.dp)
                            .clickable {
                                onFilterClick()
                            },
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))
                AgregateButton(onClick = onClickAddSupplyBatch)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement =
                Arrangement.spacedBy(0.dp),
        ) {
            if (batches.isEmpty() || batches[0].expirationDate.isEmpty()) {
                Text(
                    text = "No hay lotes del insumo",
                    color = White,
                    fontFamily = Poppins,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(vertical = 8.dp),
                )
            } else {
                batches.forEachIndexed { index, batch ->
                    val isoDate = formatToIso(batch.expirationDate)
                    SupplyBatchRow(
                        batchId = batch.id,
                        quantity = batch.quantity,
                        date = isoDate,
                        adquisition = batch.adquisitionType,
                        onViewClick = { date -> viewAllBatches(date, supply.id) },
                        onDeleteClick = { deleteSupplyBatch(batch.id) },
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            modifier =
                Modifier
                    .fillMaxWidth(),
            horizontalArrangement =
                Arrangement
                    .spacedBy(24.dp, Alignment.CenterHorizontally),
        ) {
            // Only add the delete button if the status is Active
            if (supply.status == 1) {
                DeleteButton(
                    onClick = onClickDelete,
                )
            }
            ModifyButton(
                onClick = onClickModify,
            )
        }
    }
}
package com.app.arcabyolimpo.presentation.screens.supply.supplyDetail.components

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AgregateButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.DeleteButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ModifyButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.ActiveStatus
import com.app.arcabyolimpo.ui.theme.White

@Composable
fun SupplyDetailContent(
    supply: Supply,
    onClickAddSupplyBatch: () -> Unit,
    onClickDelete: () -> Unit,
    onClickModify: () -> Unit,
    modifySupplyBatch: () -> Unit,
    deleteSupplyBatch: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(16.dp),
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
                model = supply.imageUrl,
                contentDescription = "place holder",
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
                    value = "",
                )

                Spacer(modifier = Modifier.height(8.dp))

                TextValue(
                    label = "Categoría",
                    value = "",
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    TextValue(
                        label = "Estatus",
                        value = "",
                    )
                    // Modificar por estados
                    // Placeholder Colocado aqui
                    ActiveStatus()
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
            value = supply.batch.quantity,
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
                FilterIcon(
                    modifier =
                        Modifier
                            .size(28.dp)
                            .clickable { },
                )
                Spacer(modifier = Modifier.width(16.dp))
                AgregateButton(onClick = onClickAddSupplyBatch)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Column(
            verticalArrangement =
                Arrangement.spacedBy(0.dp),
        ) {
            SupplyBatchRow(
                quantity = 5,
                date = "23/Octubre/2025",
                adquisition = "Donación",
                onModifyClick = modifySupplyBatch,
                onDeleteClick = deleteSupplyBatch,
            )
            SupplyBatchRow(
                quantity = 10,
                date = "16/Octubre/2025",
                adquisition = "Compra",
                onModifyClick = modifySupplyBatch,
                onDeleteClick = deleteSupplyBatch,
            )
            SupplyBatchRow(
                quantity = 5,
                date = "23/Octubre/2025",
                adquisition = "Donación",
                onModifyClick = modifySupplyBatch,
                onDeleteClick = deleteSupplyBatch,
            )
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
            DeleteButton(
                onClick = onClickDelete,
            )
            ModifyButton(
                onClick = onClickModify,
            )
        }
    }
}

package com.app.arcabyolimpo.presentation.screens.supply.supplyDetail

import android.R
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app.arcabyolimpo.presentation.screens.supply.supplyDetail.components.SupplyBatchRow
import com.app.arcabyolimpo.presentation.screens.supply.supplyDetail.components.TextValue
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AgregateButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.DeleteButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ModifyButton
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.FilterIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.ActiveStatus
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.White

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SuppliesDetailScreen(
    idInsumo: String,
    onBackClick: () -> Unit,
    onClickAddSupplyBatch: () -> Unit,
    onClickDelete: () -> Unit,
    onClickModify: () -> Unit,
    modifySupplyBatch: () -> Unit,
    deleteSupplyBatch: () -> Unit,
) {
    Scaffold(
        containerColor = Background,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Nombre del insumo MODIFICAR",
                        color = White,
                        fontFamily = Poppins,
                        fontWeight = FontWeight.Bold,
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            Icons.Default.ArrowBack,
                            "Back",
                            tint = White,
                            )
                    }
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
                Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                AsyncImage(
                    model = "",
                    contentDescription = "place holder",
                    modifier = Modifier.size(160.dp),
                )
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    TextValue(
                        label = "Taller",
                        value = "MODIFICAR",
                    )
                    TextValue(
                        label = "Categoria",
                        value = "MODIFICAR",
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        // Modificar por estados
                        // Placeholder Colocado aqui
                        ActiveStatus()
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            TextValue(
                label = "Unidad de medida",
                value = "MODIFICAR",
            )
            TextValue(
                label = "Cantidad actual",
                value = "MODIFICAR",
            )
            TextValue(
                label = "Tipo de adquisición",
                value = "MODIFICAR",
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth(),
            ) {
                Text(
                    text = "Inventario - Caducidad",
                    color = White,
                    fontFamily = Poppins,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 18.sp,
                )

                FilterIcon(
                    modifier = Modifier
                        .size(40.dp)
                        .padding(top = 16.dp)
                        .clickable {
                            // do something
                        }
                )

                AgregateButton(
                    onClick = onClickAddSupplyBatch,
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Column {
                SupplyBatchRow(
                    quantity = 1,
                    date = "07/noviembre/2025",
                    adquisition = "Compra",
                    onModifyClick = modifySupplyBatch,
                    onDeleteClick = deleteSupplyBatch,
                )
                SupplyBatchRow(
                    quantity = 2,
                    date = "07/noviembre/2025",
                    adquisition = "Compra",
                    onModifyClick = modifySupplyBatch,
                    onDeleteClick = deleteSupplyBatch,
                )
                SupplyBatchRow(
                    quantity = 3,
                    date = "07/noviembre/2026",
                    adquisition = "Compra",
                    onModifyClick = modifySupplyBatch,
                    onDeleteClick = deleteSupplyBatch,
                )
                SupplyBatchRow(
                    quantity = 3,
                    date = "07/noviembre/2026",
                    adquisition = "Compra",
                    onModifyClick = modifySupplyBatch,
                    onDeleteClick = deleteSupplyBatch,
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ){
                DeleteButton(
                    onClick = onClickDelete,
                )
                ModifyButton(
                    onClick = onClickModify,
                )
            }
        }
    }
}

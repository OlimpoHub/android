package com.app.arcabyolimpo.presentation.screens.supply.supplyDetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.ui.theme.DangerGray
import com.app.arcabyolimpo.ui.theme.White

@Composable
fun SupplyBatchRow(
    quantity: Int,
    date: String,
    adquisition: String,
    onModifyClick: () -> Unit,
    onDeleteClick: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 6.dp),
    ) {
        Row(
            modifier =
                Modifier
                    .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.weight(1f),
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(32.dp),
                ) {
                    Text(
                        text = "$quantity",
                        color = White,
                        fontFamily = Poppins,
                        fontSize = 16.sp,
                    )
                    Text(
                        text = date,
                        color = White,
                        fontFamily = Poppins,
                        fontSize = 16.sp,
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Adquisición: $adquisition",
                    color = White,
                    fontSize = 16.sp,
                    fontFamily = Poppins,
                )
            }

            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Row {
                    IconButton(
                        onClick = onModifyClick,
                        modifier = Modifier.padding(end = 2.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Default.Create,
                            contentDescription = "modify",
                            tint = White,
                        )
                    }

                    IconButton(
                        onClick = onDeleteClick,
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "delete",
                            tint = White,
                        )
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))

        Divider(
            color = DangerGray.copy(alpha = 0.3f),
            thickness = 0.7.dp,
            modifier =
                Modifier
                    .fillMaxWidth(),
        )
    }
}

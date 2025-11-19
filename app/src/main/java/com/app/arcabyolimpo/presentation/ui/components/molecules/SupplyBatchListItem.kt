package com.app.arcabyolimpo.presentation.ui.components.molecules

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Create
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.ui.theme.DangerGray
import com.app.arcabyolimpo.ui.theme.White

/**
 * Simple row item for the SupplyBatchList screen matching the visual style of
 * `SupplyBatchRow` but showing a "modify" action instead of view/delete.
 */
@Composable
fun SupplyBatchListItem(
    index: Int,
    batchId: String,
    quantity: Int,
    expirationDate: String,
    adquisition: String,
    measure: String,
    boughtDate: String,
    onModifyClick: (String) -> Unit,
) {
    val TAG = "SupplyBatchListItem"

    Column(
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 13.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // 🔹 1. Columna izquierda: index
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(40.dp),
            ) {
                Text(
                    text = "${index + 1}",
                    color = White,
                    fontFamily = Poppins,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                )
            }

            // 🔹 2. Columna central: toda la información
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f),
            ) {
                Text(
                    text = "$quantity $measure",
                    color = White,
                    fontFamily = Poppins,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )

                Text(
                    text = "Fecha de compra: $boughtDate",
                    color = White,
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                )

                Text(
                    text = "Tipo de adquisición: $adquisition",
                    color = White,
                    fontSize = 14.sp,
                    fontFamily = Poppins,
                )
            }

            // 🔹 3. Columna derecha: botón modify
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.width(48.dp),
            ) {
                IconButton(
                    onClick = {
                        Log.d(TAG, "Modify clicked for batchId=$batchId")
                        onModifyClick(batchId)
                    },
                ) {
                    androidx.compose.material.Icon(
                        imageVector = Icons.Default.Create,
                        contentDescription = "modify",
                        tint = White,
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Divider(
            color = DangerGray.copy(alpha = 0.3f),
            thickness = 0.7.dp,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

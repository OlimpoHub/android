package com.app.arcabyolimpo.presentation.screens.supply.supplyDetail.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
) {
    Column(
        modifier = Modifier
            .fillMaxWidth() // 👈 esto es importante
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "$quantity",
                color = White,
                fontSize = 14.sp,
                fontFamily = Poppins
            )
            Text(
                text = date,
                color = White,
                fontSize = 14.sp,
                fontFamily = Poppins
            )
        }

        Divider(
            color = DangerGray.copy(alpha = 0.3f),
            thickness = 0.7.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp)
        )
    }
}

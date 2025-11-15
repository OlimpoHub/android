package com.app.arcabyolimpo.presentation.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.arcabyolimpo.R
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush

@Composable
fun BeneficiaryCard(
    name: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    cardModifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(20.dp),
    cardHeight: Int? = null
) {
    val color1 = Color(0xFF3655C7)
    val color2 = Color(0xFF011560)

    val gradientBrush = Brush.linearGradient(
        colors = listOf(color1, color2),
        start = Offset(0f, 0f),
        end = Offset(Float.POSITIVE_INFINITY, Float.POSITIVE_INFINITY)
    )

    val finalModifier =
        if (cardHeight != null)
            cardModifier.height(cardHeight.dp)
        else
            cardModifier

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.Transparent
        ),
        modifier = finalModifier
            .padding(6.dp)
            .clickable(onClick = onClick),
    ) {
        Column(
            modifier = Modifier
                .background(gradientBrush)
                .padding(contentPadding)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_beneficiary_icon),
                contentDescription = "Beneficiario",
                tint = Color.White,
                modifier = Modifier.size(48.dp),
            )

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = name,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color.White,
            )
        }
    }
}



@Preview(showBackground = true, backgroundColor = 0xFF000000)
@Composable
fun BeneficiaryCardPreview() {
    BeneficiaryCard(name = "John Smith", onClick = {})
}

package com.app.arcabyolimpo.presentation.ui.components.molecules

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.arcabyolimpo.domain.model.supplies.Supply
import com.app.arcabyolimpo.presentation.theme.Poppins
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.ViewButton
import com.app.arcabyolimpo.ui.theme.Background
import com.app.arcabyolimpo.ui.theme.ButtonBlue
import com.app.arcabyolimpo.ui.theme.DangerGray
import com.app.arcabyolimpo.ui.theme.White
import androidx.compose.material3.Divider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue

@Composable
fun SupplyCard(
    supply: Supply,
    onClick: () -> Unit
) {

    var isPressed by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        label = ""
    )
    Card(
        modifier =
            Modifier
                .fillMaxWidth()
                .clickable(onClick = onClick)
                .padding(horizontal = 8.dp, vertical = 6.dp)
                .scale(scale)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onPress = {
                            isPressed = true
                            tryAwaitRelease()
                            isPressed = false
                            onClick()
                        },
                    )
                },
        colors = CardDefaults.cardColors(
            containerColor = Background
        ),
        elevation =
            CardDefaults.cardElevation(defaultElevation = 2.dp),
    ) {
        Column(
            modifier = Modifier.fillMaxWidth()
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ){
                Box(
                    modifier = Modifier
                        .size(74.dp)
                        .background(color = ButtonBlue.copy(alpha = 0.1f), shape = CircleShape)
                )

                Text(
                    text = supply.name,
                    color = White,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 15.sp,
                    fontFamily = Poppins,
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 16.dp)
                )

                ViewButton(onClick = onClick)
            }
            Divider(
                color = DangerGray.copy(alpha = 0.3f),
                thickness = 0.7.dp,
                modifier = Modifier.padding(horizontal = 12.dp)
            )
        }
    }
}
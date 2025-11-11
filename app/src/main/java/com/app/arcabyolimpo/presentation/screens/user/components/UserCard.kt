package com.app.arcabyolimpo.presentation.screens.user.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.app.arcabyolimpo.data.remote.dto.user.UserDto
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.ActiveStatus
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.InactiveStatus

@Composable
fun UserCard(
    user: UserDto,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF040610)
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Profile Picture
            if (user.foto != null) {
                AsyncImage(
                    model = user.foto,
                    contentDescription = "Profile Photo",
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Placeholder if no photo
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(Color(0xFF1E293B)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "${user.apellidoPaterno.firstOrNull() ?: ""}${user.apellidoMaterno.firstOrNull() ?: ""}",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Name
            Text(
                text = "${user.nombre} ${user.apellidoPaterno} ${user.apellidoMaterno}",
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.weight(1f)
            )

            // Status badge - Active or Inactive
            if (user.estatus == 1) {
                ActiveStatus(
                    modifier = Modifier.padding(0.dp)
                )
            } else {
                InactiveStatus(
                    modifier = Modifier.padding(0.dp)
                )
            }
        }
    }
}
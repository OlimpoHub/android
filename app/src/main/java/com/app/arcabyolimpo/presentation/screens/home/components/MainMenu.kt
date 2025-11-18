package com.app.arcabyolimpo.presentation.screens.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.R
import com.app.arcabyolimpo.presentation.ui.components.molecules.HomeScreenCard

/**
 * Displays the main menu of the Home screen with cards to navigate between sections.
 *
 * @param paddingValues Padding provided by the Scaffold.
 * @param onSelect Callback invoked when a menu option is selected.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun MainMenu(
    paddingValues: PaddingValues,
    onSelect: (String) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HomeScreenCard(
            name = "QR Asistencia",
            image = painterResource(id = R.drawable.img_qr),
            onClick = { onSelect("qr") },
        )

        Text(
            text = "Menú principal",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth(),
        )

        HomeScreenCard(
            name = "Usuarios",
            image = painterResource(id = R.drawable.img_users),
            onClick = { onSelect("users") },
        )

        HomeScreenCard(
            name = "Capacitaciones",
            image = painterResource(id = R.drawable.img_trainings),
            onClick = { onSelect("training") },
        )
    }
}

package com.app.arcabyolimpo.presentation.screens.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.arcabyolimpo.presentation.screens.session.SessionViewModel
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ConfigurationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon

@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InventoryTopBar() {
    Column(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(
            title = {
                Text(
                    text = "Inventario",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                )
            },
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF040610),
                ),
            actions = {
                IconButton(onClick = { /* TODO: Notifications action */ }) {
                    NotificationIcon()
                }
            },
        )
    }
}

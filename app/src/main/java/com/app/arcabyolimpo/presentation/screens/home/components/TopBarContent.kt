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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.arcabyolimpo.presentation.screens.session.SessionViewModel
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ConfigurationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon

/**
 * Displays the main top bar for the Home screen, showing a welcome message,
 * user name, and action icons for notifications and settings.
 * Retrieves the SessionViewModel via Hilt to enable logout actions.
 */
@Suppress("ktlint:standard:function-naming")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarContent() {
    val sessionViewModel: SessionViewModel = hiltViewModel()

    Column(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "Bienvenid@, ",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFFCCCCCC),
                        fontWeight = FontWeight.Normal,
                    )
                    Text(
                        text = "Jorge Garzón",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                }
            },
            colors =
                TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF040610),
                ),
            actions = {
                IconButton(onClick = { /* TODO: Notifications action */ }) {
                    NotificationIcon()
                }
                IconButton(onClick = { sessionViewModel.logout() }) {
                    ConfigurationIcon()
                }
            },
        )
    }
}

package com.app.arcabyolimpo.presentation.screens.user.detail.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Displays a labeled information card showing a single user attribute.
 *
 * This composable renders a card component designed to present user information
 * in a clean, consistent format throughout the user detail screen. Each card
 * displays a label (such as "Email" or "Phone") and its corresponding value,
 * with appropriate typography hierarchy and spacing.
 *
 * The card uses a dark background matching the application's theme and includes
 * subtle elevation for visual depth. It handles null values gracefully by only
 * rendering the value text when data is available, preventing empty or "null"
 * text from appearing in the UI.
 *
 * This component follows Material Design 3 principles and is reusable across
 * different detail screens where similar information display patterns are needed.
 *
 * @param label The descriptive label for the information being displayed (e.g., "Email",
 *              "Phone", "Career"). Should be concise and clearly indicate the type of
 *              information shown in the value field.
 * @param value The actual user information to display, or null if the information is
 *              not available. When null, only the label is displayed, allowing the
 *              UI to indicate missing data gracefully.
 */

@Composable
fun InfoCard(label: String, value: String?) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF040610)
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = label,
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (value != null) {
                Text(
                    text = value,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }
    }
}
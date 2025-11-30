package com.app.arcabyolimpo.presentation.ui.components.molecules

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.BeneficiaryIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.HomeIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.InventoryIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.OrderIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.WorkshopIcon
import com.app.arcabyolimpo.ui.theme.HeaderLineBlue

/**
 * Bottom navigation bar used in ScholarHomeScreen.
 * Highlights the selected item and triggers a callback when an item is tapped.
 *
 * @param selectedIndex Index of the currently selected tab.
 * @param onItemSelected Callback invoked when a tab is selected.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun ScholarNavBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit,
) {
    data class NavItem(
        val label: String,
        val icon: @Composable (Color, androidx.compose.ui.unit.Dp) -> Unit,
    )

    val items =
        listOf(
            NavItem("Inicio", { tint, size -> HomeIcon(tint = tint, size = size) }),
            NavItem("Talleres", { tint, size -> WorkshopIcon(tint = tint, size = size) }),
            NavItem("Beneficiarios", { tint, size -> BeneficiaryIcon(tint = tint, size = size) }),
        )

    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .background(HeaderLineBlue)
                .padding(vertical = 15.dp, horizontal = 15.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.Bottom,
    ) {
        items.forEachIndexed { index, item ->
            val isSelected = index == selectedIndex
            val tint = if (isSelected) Color.White else Color.White.copy(alpha = 0.5f)
            val size = 28.dp

            Column(
                modifier =
                    Modifier
                        .clickable { onItemSelected(index) }
                        .padding(5.dp)
                        .weight(1f),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
            ) {
                item.icon(tint, size)
                Spacer(modifier = Modifier.height(3.dp))
                Text(
                    text = item.label,
                    style = Typography.bodySmall.copy(fontSize = 9.sp),
                    color = tint,
                )
            }
        }
    }
}

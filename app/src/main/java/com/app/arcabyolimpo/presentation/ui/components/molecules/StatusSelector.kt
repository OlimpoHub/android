package com.app.arcabyolimpo.presentation.ui.components.molecules

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.presentation.theme.Typography
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.ActiveStatus
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.InactiveStatus
import com.app.arcabyolimpo.presentation.ui.components.atoms.status.Poppins
import com.app.arcabyolimpo.ui.theme.ButtonBlue
import com.app.arcabyolimpo.ui.theme.DangerGray
import com.app.arcabyolimpo.ui.theme.PrimaryBlue
import com.app.arcabyolimpo.ui.theme.White

/** ---------------------------------------------------------------------------------------------- *
 * Molecule Status selector: Takes the active status and inactive status, to make them clickable and
 * updates their status and colors depending which one is pressed
 *
 * @param status: Int -> 1 for active, 0 for inactive
 * @param onStatusChange: (Int) -> Unit -> function to execute when the status is changed
 * @param modifier: Modifier = Modifier -> modifier to apply to the composable
 * ---------------------------------------------------------------------------------------------- */
@Composable
fun StatusSelector(
    status: Int,
    onStatusChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = Modifier) {
        Row {
            Text(
                text = "Estatus",
                style = Typography.bodyMedium,
                fontFamily = Poppins,
                color = White
            )

            Spacer(Modifier.width(8.dp))

            Box(
                modifier =
                    Modifier.clickable { onStatusChange(1) }
            ) {
                ActiveStatus(
                    backgroundColor = if (status == 1) PrimaryBlue else ButtonBlue,
                    textColor = if (status == 1) White else DangerGray
                )
            }

            Spacer(Modifier.width(16.dp))

            Box(
                modifier =
                    Modifier.clickable { onStatusChange(0) }
            ) {
                InactiveStatus(
                    backgroundColor = if (status == 0) White else DangerGray,
                    textColor = PrimaryBlue
                )
            }
        }
    }
}
@Preview(
    showBackground = true,
    backgroundColor = 0xFF040610,
)
@Composable
fun StatusSelectorPreview() {
    MaterialTheme {
        StatusSelector(
            status = 1,
            onStatusChange = { }
        )
    }
}
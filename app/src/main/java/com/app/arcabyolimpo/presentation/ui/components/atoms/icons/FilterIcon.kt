package com.app.arcabyolimpo.presentation.ui.components.atoms.icons

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.R

@Suppress("ktlint:standard:function-naming")
@Composable
fun FilterIcon(
    modifier: Modifier = Modifier,
    tint: Color = Color.Unspecified,
    size: Dp = 24.dp,
) {
    Icon(
        painter = painterResource(id = R.drawable.ic_filter_icon),
        contentDescription = "Filter Icon",
        tint = tint,
        modifier = modifier.size(size),
    )
}

package com.app.arcabyolimpo.presentation.ui.components.atoms.logo

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.R

@Suppress("ktlint:standard:function-naming")
@Composable
fun ArcaLogo(
    modifier: Modifier = Modifier,
    size: Dp = 30.dp,
    contentDescription: String? = "El Arca",
) {
    Image(
        painter = painterResource(id = R.drawable.img_arca_logo),
        contentDescription = contentDescription,
        modifier = modifier.size(size),
    )
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun ArcaLogoPreview() {
    ArcaLogo()
}

package com.app.arcabyolimpo.presentation.ui.components.atoms.inputs

import android.net.Uri
import android.widget.ImageView
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import coil.compose.AsyncImage
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.UploadIcon
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme
import com.app.arcabyolimpo.ui.theme.ErrorRed
import com.app.arcabyolimpo.ui.theme.White
import com.app.arcabyolimpo.ui.theme.HighlightInputBlue
import com.app.arcabyolimpo.ui.theme.HighlightRed
import com.app.arcabyolimpo.ui.theme.InputBackgroundBlue
import com.app.arcabyolimpo.ui.theme.InputBackgroundRed
import com.app.arcabyolimpo.ui.theme.SelectInputBlue


/**
 * ImageUploadInput: clickable area to select an image from the gallery.
 *
 * - Visual style aligned with StandardInput (surface, primary/error, onSurface).
 * - Shows a placeholder with text + icon when no image is selected.
 * - Exposes the selected Uri via [onValueChange].
 *
 * @param label Title shown above the area.
 * @param onValueChange Callback triggered when an image is selected (pass null if you later want to clear it).
 * @param modifier Outer modifier.
 * @param value Uri? Initial state (defaults to null).
 * @param isError Marks the field as invalid (switches border to error color).
 * @param errorMessage Optional message displayed below the component (when isError = true).
 * @param height Minimum height of the container.
 */

@Suppress("ktlint:standard:function_naming")
@Composable
fun ImageUploadInput(
    label: String,
    onValueChange: (Uri?) -> Unit,
    modifier: Modifier = Modifier,
    value: Uri? = null,
    isError: Boolean = false,
    errorMessage: String? = null,
    height: Dp = 160.dp,
) {
    val shape = RoundedCornerShape(12.dp)
    val backgroundColor =
        if (isError) InputBackgroundRed else InputBackgroundBlue
    val borderColor =
        if (isError) HighlightRed else HighlightInputBlue
    val textColor = White

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        onValueChange(uri)
    }

    Column(
        modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .height(height)
                .clip(shape)
                .background(backgroundColor)
                .border(BorderStroke(1.dp, borderColor), shape)
                .clickable { launcher.launch("image/*") }
                .padding(12.dp),
            contentAlignment = Alignment.Center
        ) {
            if (value != null) {
                AsyncImage(
                    model = value,
                    contentDescription = "Imagen seleccionada por el usuario",
                    modifier =
                        Modifier
                            .fillMaxSize()
                            .clip(shape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Subir imagen",
                        style = MaterialTheme.typography.bodyMedium,
                        color = White.copy(alpha = 0.7f),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(8.dp))
                    UploadIcon(
                        modifier = Modifier.size(48.dp),
                        tint = White.copy(alpha = 0.8f)
                    )
                }
            }
        }

        if (isError && !errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = ErrorRed,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 2.dp),
            )
        }
    }
}

/* ===================== PREVIEWS ===================== */

@Suppress("ktlint:standard:function_naming")
@Preview
@Composable
private fun ImageUploadInputPreview() {
    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        ImageUploadInput(
            label = "Imagen del producto",
            value = null,
            onValueChange = {},
        )
    }
}



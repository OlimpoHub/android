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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.UploadIcon
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme

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
    var imageUri by rememberSaveable { mutableStateOf(value?.toString()) }
    val currentUri: Uri? = imageUri?.takeIf { it.isNotBlank() }?.let(Uri::parse)

    val shape = RoundedCornerShape(12.dp)
    val borderColor = if (isError) MaterialTheme.colorScheme.error
    else MaterialTheme.colorScheme.primary

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        imageUri = uri?.toString().orEmpty()
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
            color = MaterialTheme.colorScheme.onSurface,
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 4.dp)
                .height(height)
                .clip(shape)
                .background(MaterialTheme.colorScheme.surface)
                .border(BorderStroke(1.dp, borderColor), shape)
                .clickable { launcher.launch("image/*") }
                .padding(12.dp),
        ) {
            if (currentUri != null) {
                AndroidView(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(shape),
                    factory = { ctx ->
                        ImageView(ctx).apply {
                            adjustViewBounds = true
                            scaleType = ImageView.ScaleType.CENTER_CROP
                        }
                    },
                    update = { it.setImageURI(currentUri) },
                )
            } else {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = "Subir Imagen",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
                        textAlign = TextAlign.Center,
                    )
                    Spacer(Modifier.height(12.dp))
                    UploadIcon(
                        modifier = Modifier.size(56.dp),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        if (isError && !errorMessage.isNullOrEmpty()) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error,
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



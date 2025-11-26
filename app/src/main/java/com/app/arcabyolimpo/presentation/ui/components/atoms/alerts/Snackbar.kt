package com.app.arcabyolimpo.presentation.ui.components.atoms.alerts

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarVisuals
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.app.arcabyolimpo.ui.theme.ButtonBlue
import com.app.arcabyolimpo.ui.theme.DangerGray
import com.app.arcabyolimpo.ui.theme.ErrorRed
import kotlinx.coroutines.launch

/**
 * SnackbarVisualsWithError you dont use it, you use the Snackbarcustom
 *
 * Custom class that implements [SnackbarVisuals] to allow
 * displaying success or error messages within the Snackbar component.
 * @param message Text message to be displayed in the snackbar.
 * @param isError Indicates whether the message is an error (true) or a success (false).
 */
class SnackbarVisualsWithError(
    override val message: String,
    val isError: Boolean,
) : SnackbarVisuals {
    override val actionLabel: String
        get() = if (isError) "Error" else "OK"

    override val withDismissAction: Boolean
        get() = false

    override val duration: SnackbarDuration
        get() = SnackbarDuration.Long
}

@Suppress("ktlint:standard:function-naming")
@Composable
fun SnackbarArca() {
    // Remembered state to control active snack bars
    val snackbarHostState = remember { SnackbarHostState() }
    // Coroutine scope for launching suspended calls (showSnackbar)
    val scope = rememberCoroutineScope()

    // Scaffold allows you to place a snackbarHost
    // and a Floting Action Button to interact
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackbarHostState) { data ->
                // Every time a snackbar is displayed, our custom component is called
                Snackbarcustom(
                    data.visuals.message.toString(),
                    modifier = Modifier,
                    // This can be replaced with logic to validate
                    // the type of snack bar (whether it's red or blue).
                    ifSucces = true,
                )
            }
        },
        // Floating button that triggers the appearance of the snack bar
        floatingActionButton = {
            var clickCount by remember { mutableStateOf(0) }
            ExtendedFloatingActionButton(
                onClick = {
                    // show snackbar as a suspend function
                    scope.launch {
                        snackbarHostState.showSnackbar(
                            SnackbarVisualsWithError(
                                "Snackbar # ${++clickCount}",
                                isError = clickCount % 2 != 0,
                            ),
                        )
                    }
                },
            ) {
                Text("Show snackbar")
            }
        },
        content = { innerPadding ->
            Text(
                text = "Body content",
                modifier = Modifier.padding(innerPadding).fillMaxSize().wrapContentSize(),
            )
        },
    )
}

/**
 * Snackbarcustom
 *
 * Custom component to render the visual appearance of the snackbar.
 * Changes its background color depending on the message type (success or error).
 * @param title Text displayed within the snackbar.
 * @param modifier Allows modification of the component's external layout.
 * @param ifSuccess Optional Boolean that defines the color (true = success, false = error).
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun Snackbarcustom(
    title: String,
    modifier: Modifier,
    ifSucces: Boolean? = true,
) {
    var snackbartitle = title
    Snackbar(
        containerColor =
            if (ifSucces == true) {
                ButtonBlue
            } else {
                ErrorRed
            },
    ) {
        Text(snackbartitle)
    }
}

@Suppress("ktlint:standard:function-naming")
@Preview(showBackground = true)
@Composable
fun SnackbarPreview() {
    MaterialTheme {
        SnackbarArca()
    }
}


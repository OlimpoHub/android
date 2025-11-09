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
import kotlinx.coroutines.launch

class SnackbarVisualsWithError(override val message: String, val isError: Boolean) :
    SnackbarVisuals {
    override val actionLabel: String
        get() = if (isError) "Error" else "OK"

    override val withDismissAction: Boolean
        get() = false

    override val duration: SnackbarDuration
        get() = SnackbarDuration.Indefinite
}

@Composable
fun SnackbarArca(
){
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    Scaffold(
    snackbarHost = { SnackbarHost(snackbarHostState){data ->
        Snackbarcustom(data.visuals.message.toString(), modifier = Modifier)
    } },
    floatingActionButton = {
        var clickCount by remember { mutableStateOf(0) }
        ExtendedFloatingActionButton(
            onClick = {
                // show snackbar as a suspend function
                scope.launch { snackbarHostState.showSnackbar(
                    SnackbarVisualsWithError(
                        "Snackbar # ${++clickCount}",
                        isError = clickCount % 2 != 0,))}
            }
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
)}

@Composable
fun Snackbarcustom(
    title : String,
    modifier: Modifier
){
    Snackbar(
        modifier = modifier.background(color = Color.Red)
    ) {
        Text(title)
    }
}

@Preview(showBackground = true)
@Composable
fun SnackbarPreview() {
    MaterialTheme {
        SnackbarArca()
    }
}

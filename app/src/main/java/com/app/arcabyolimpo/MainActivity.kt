package com.app.arcabyolimpo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.app.arcabyolimpo.presentation.ui.components.atoms.buttons.AddButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    Scaffold(
        floatingActionButton = {
            AddButton(onClick = { println("Plus button clicked!") })
        },
    ) { innerPadding ->
        // Contenido de ejemplo
        Text(
            text = "Hello from MainActivity 👋",
            modifier = Modifier.padding(innerPadding),
        )
    }
}

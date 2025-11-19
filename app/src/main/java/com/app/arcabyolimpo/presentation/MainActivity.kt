package com.app.arcabyolimpo.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.Scaffold
import com.app.arcabyolimpo.data.remote.interceptor.SessionManager
import com.app.arcabyolimpo.presentation.navigation.ArcaNavGraph
import com.app.arcabyolimpo.presentation.screens.supply.supplyDetail.SuppliesDetailScreen
import com.app.arcabyolimpo.presentation.screens.supply.supplyList.SupplyListScreen
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * Main entry point of the app.
 *
 * This activity hosts the entire Jetpack Compose UI and initializes
 * the app's navigation graph and session handling through dependency injection.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var sessionManager: SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Scaffold { innerPadding ->
                ArcaNavGraph(
                    sessionManager = sessionManager,
                )
            }
        }
    }
}

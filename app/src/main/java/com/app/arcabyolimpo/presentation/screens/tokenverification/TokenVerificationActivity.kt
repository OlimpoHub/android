package com.app.arcabyolimpo.presentation.screens.tokenverification

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import com.app.arcabyolimpo.presentation.screens.passwordregisteration.PasswordRegistrationScreen
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TokenVerificationActivity : ComponentActivity() {
    private val viewModel: TokenVerificationViewModel by viewModels()
    val uiState = viewModel.uiState.value

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Get the token from the deep link
        val data: Uri? = intent?.data
        val token = data?.getQueryParameter("token")

        if (token != null) {
            // Call your ViewModel function
            viewModel.getTokenVerification(token)
        }

        setContent {
            val uiState by viewModel.uiState.collectAsState()

            when {
                uiState.response?.valid == true -> {
                    PasswordRegistrationScreen(
                        email = uiState.response?.email,
                        onBackClick = { finish() },
                        viewModel = hiltViewModel()
                    )
                }
                uiState.response?.valid == false -> {
                    TokenVerificationFailedScreen(
                        onBackClick = { finish() }
                    )
                }
            }
        }

    }
}

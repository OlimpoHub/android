package com.app.arcabyolimpo.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.app.arcabyolimpo.data.remote.interceptor.SessionManager
import com.app.arcabyolimpo.domain.model.auth.UserRole
import com.app.arcabyolimpo.presentation.screens.accountactivation.AccountActivationScreen
import com.app.arcabyolimpo.presentation.screens.admin.CoordinatorHomeScreen
import com.app.arcabyolimpo.presentation.screens.client.CollaboratorHomeScreen
import com.app.arcabyolimpo.presentation.screens.login.LoginScreen
import com.app.arcabyolimpo.presentation.screens.passwordrecovery.PasswordRecoveryScreen
import com.app.arcabyolimpo.presentation.screens.passwordregisteration.PasswordRegistrationScreen
import com.app.arcabyolimpo.presentation.screens.splash.SplashScreen
import com.app.arcabyolimpo.presentation.screens.tokenverification.TokenVerificationFailedScreen
import com.app.arcabyolimpo.presentation.screens.tokenverification.TokenVerificationViewModel

/**
 * Defines all available destinations (routes) in the application.
 *
 * Each screen is represented as a subclass of [Screen] with a unique [route].
 */
sealed class Screen(
    val route: String,
) {
    object Splash : Screen("splash")

    object Login : Screen("login")

    object PasswordRecovery : Screen("password-recovery")

    object AccountActivation : Screen("account-activation")

    object TokenVerification : Screen("user/verify-token?token={token}") {
        fun createRoute(token: String) = "user/verify-token?token=$token"
    }

    object TokenVerificationFailed : Screen("token-activation-failed")

    object PasswordRegistration : Screen("password-registration/{email}") {
        fun createRoute(email: String) = "password-registration/$email"
    }

    object CoordinatorHome : Screen("admin")

    object CollaboratorHome : Screen("client")
}

/**
 * Composable function that defines the main navigation graph of the app.
 *
 * It connects all screens and handles navigation.
 *
 * @param modifier Optional [Modifier] for layout adjustments.
 * @param navController The controller managing app navigation.
 * @param sessionManager Observes session state to handle automatic logout or token expiration.
 */
@Suppress("ktlint:standard:function-naming")
@Composable
fun ArcaNavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    sessionManager: SessionManager,
) {
    /**
     * Observes session expiration events. When the session expires, the user is
     * redirected to the Login screen, and the navigation back stack is cleared.
     */
    LaunchedEffect(sessionManager) {
        sessionManager.sessionExpired.collect {
            navController.navigate(Screen.Login.route) {
                popUpTo(0) { inclusive = true }
                launchSingleTop = true
            }
        }
    }

    //navController.navigate(Screen.PasswordRegistration.createRoute(email))

    /** Defines all navigation. The start destination is the Splash screen. */
    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route,
        modifier = modifier,
    ) {
        /** Splash Screen */
        composable(Screen.Splash.route) {
            SplashScreen(onNavigate = { role ->
                when (role) {
                    UserRole.COORD ->
                        navController.navigate(Screen.CoordinatorHome.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    UserRole.COLAB ->
                        navController.navigate(Screen.CollaboratorHome.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    null ->
                        navController.navigate(Screen.Login.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                }
            })
        }

        /** Login Screen */
        composable(Screen.Login.route) {
            LoginScreen(onLoginSuccess = { role ->
                when (role) {
                    UserRole.COORD ->
                        navController.navigate(Screen.CoordinatorHome.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                    UserRole.COLAB ->
                        navController.navigate(Screen.CollaboratorHome.route) {
                            popUpTo(Screen.Login.route) { inclusive = true }
                        }
                }
            })
        }

        composable(Screen.PasswordRecovery.route) {
            PasswordRecoveryScreen(
                onBackClick = {
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Login.route)
                    }
                },
                viewModel = hiltViewModel()
            )
        }

        composable(Screen.AccountActivation.route) {
            AccountActivationScreen(
                onBackClick = {
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Login.route)
                    }
                },
                viewModel = hiltViewModel()
            )
        }

        composable(
            route = Screen.TokenVerification.route,
            arguments = listOf(navArgument("token") {
                type = NavType.StringType
                nullable = true
            })
        ) { backStackEntry ->
            val token = backStackEntry.arguments?.getString("token")
            val viewModel: TokenVerificationViewModel = hiltViewModel()
            val uiState by viewModel.uiState.collectAsState()

            LaunchedEffect(token) {
                token?.let { viewModel.getTokenVerification(it) }
            }

            when {
                uiState.response?.valid == true -> {
                    PasswordRegistrationScreen(
                        email = uiState.response?.email,
                        onBackClick = { navController.popBackStack() },
                        viewModel = hiltViewModel()
                    )
                }
                uiState.response?.valid == false -> {
                    TokenVerificationFailedScreen(onBackClick = { navController.popBackStack() })
                }
            }
        }


        composable(Screen.TokenVerificationFailed.route) {
            TokenVerificationFailedScreen (
                onBackClick = {
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Login.route)
                    }
                },
            )
        }

        composable(
            route = Screen.PasswordRegistration.route,
            arguments = listOf(navArgument("email") { type = NavType.StringType })
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            PasswordRegistrationScreen(
                email = email,
                onBackClick = {
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Login.route)
                    }
                },
                viewModel = hiltViewModel()
            )
        }


        /** Coordinator Home Screen */
        composable(Screen.CoordinatorHome.route) {
            CoordinatorHomeScreen()
        }

        /** Collaborator Home Screen */
        composable(Screen.CollaboratorHome.route) {
            CollaboratorHomeScreen()
        }
    }
}

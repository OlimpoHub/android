package com.app.arcabyolimpo.presentation.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import com.app.arcabyolimpo.data.remote.interceptor.SessionManager
import com.app.arcabyolimpo.domain.model.auth.UserRole
import com.app.arcabyolimpo.presentation.common.components.LoadingShimmer
import com.app.arcabyolimpo.presentation.screens.accountactivation.AccountActivationScreen
import com.app.arcabyolimpo.presentation.screens.beneficiary.BeneficiaryDetailScreen
import com.app.arcabyolimpo.presentation.screens.beneficiary.BeneficiaryList
import com.app.arcabyolimpo.presentation.screens.beneficiary.BeneficiaryListScreen
import com.app.arcabyolimpo.presentation.screens.home.assistant.CollaboratorHomeScreen
import com.app.arcabyolimpo.presentation.screens.home.coordinator.CoordinatorHomeScreen
import com.app.arcabyolimpo.presentation.screens.login.LoginScreen
import com.app.arcabyolimpo.presentation.screens.passwordrecovery.PasswordRecoveryScreen
import com.app.arcabyolimpo.presentation.screens.passwordregisteration.PasswordRegistrationScreen
import com.app.arcabyolimpo.presentation.screens.passwordregisteration.PasswordRegistrationSuccessScreen
import com.app.arcabyolimpo.presentation.screens.splash.SplashScreen
import com.app.arcabyolimpo.presentation.screens.supply.supplyAdd.SupplyAddScreen
import com.app.arcabyolimpo.presentation.screens.supply.supplyDetail.SuppliesDetailScreen
import com.app.arcabyolimpo.presentation.screens.supply.supplyList.SupplyListScreen
import com.app.arcabyolimpo.presentation.screens.tokenverification.TokenVerificationFailedScreen
import com.app.arcabyolimpo.presentation.screens.tokenverification.TokenVerificationViewModel
import com.app.arcabyolimpo.presentation.screens.user.UserListScreen
import com.app.arcabyolimpo.presentation.screens.user.detail.UserDetailScreen
import com.app.arcabyolimpo.presentation.screens.user.register.UserRegisterScreen
import com.app.arcabyolimpo.presentation.screens.workshop.AddNewWorkshopScreen
import com.app.arcabyolimpo.presentation.screens.workshop.WorkshopDetailScreen
import com.app.arcabyolimpo.presentation.screens.workshop.WorkshopsListScreen

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

    object UserList : Screen("user")

    object UserRegister : Screen("user_register")

    object UserDetail : Screen("user_detail/{userId}") {
        fun createRoute(userId: String) = "user_detail/$userId"
    }

    object TokenVerification : Screen("user/verify-token?token={token}") {
        fun createRoute(token: String) = "user/verify-token?token=$token"
    }

    object TokenVerificationFailed : Screen("token-activation-failed")

    object PasswordRegistration : Screen("password-registration/{email}") {
        fun createRoute(email: String) = "password-registration/$email"
    }

    object PasswordRegistrationSuccess : Screen("pasword-registration-success")

    object CoordinatorHome : Screen("coordinator")

    object CollaboratorHome : Screen("collaborator")

    object SuppliesList : Screen("supply")

    object WorkshopsList : Screen("workshop")

    object WorkshopDetail : Screen("workshop/{id}") {
        fun createRoute(id: String): String = "workshop/$id"
    }

    object AddNewWorkshop : Screen("workshop/add")

    object BeneficiaryList : Screen("beneficiary_list")

    object BeneficiaryDetail : Screen("beneficiary_detail/{beneficiaryId}") {
        fun createRoute(beneficiaryId: String) = "beneficiary_detail/$beneficiaryId"
    }

    object SupplyDetail : Screen("supply/{idSupply}") {
        fun createRoute(idSupply: String) = "supply/$idSupply"
    }

    object SupplyAdd : Screen("supply/add")
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

    // navController.navigate(Screen.PasswordRegistration.createRoute(email))

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
                    UserRole.COORDINADOR ->
                        navController.navigate(Screen.CoordinatorHome.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    UserRole.ASISTENTE ->
                        navController.navigate(Screen.CollaboratorHome.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }
                    UserRole.BECARIO ->
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
            LoginScreen(
                onLoginSuccess = { role ->
                    when (role) {
                        UserRole.COORDINADOR ->
                            navController.navigate(Screen.CoordinatorHome.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        UserRole.ASISTENTE ->
                            navController.navigate(Screen.CollaboratorHome.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                        UserRole.BECARIO ->
                            navController.navigate(Screen.CollaboratorHome.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }
                    }
                },
                onRecoverPasswordClick = {
                    navController.navigate(Screen.PasswordRecovery.route)
                },
                onAccountActivationClick = {
                    navController.navigate(Screen.AccountActivation.route)
                },
            )
        }

        composable(Screen.PasswordRecovery.route) {
            PasswordRecoveryScreen(
                onBackClick = {
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Login.route)
                    }
                },
                viewModel = hiltViewModel(),
            )
        }

        composable(Screen.AccountActivation.route) {
            AccountActivationScreen(
                onBackClick = {
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Login.route)
                    }
                },
                viewModel = hiltViewModel(),
            )
        }

        composable(
            route = Screen.TokenVerification.route,
            arguments =
                listOf(
                    navArgument("token") {
                        type = NavType.StringType
                        nullable = true
                    },
                ),
            deepLinks =
                listOf(
                    navDeepLink {
                        uriPattern = "arcabyolimpo://user/verify-token?token={token}"
                    },
                ),
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
                        onPasswordRegistrationSucessClick = {
                            navController.navigate(Screen.PasswordRegistrationSuccess.route)
                        },
                        viewModel = hiltViewModel(),
                    )
                }

                uiState.isLoading -> {
                    LoadingShimmer(
                        modifier =
                            Modifier
                                .fillMaxSize(),
                    )
                }

                uiState.error == "Invalid or expired token" -> {
                    TokenVerificationFailedScreen(onBackClick = { navController.popBackStack() })
                }

                else -> {
                    LoadingShimmer(
                        modifier =
                            Modifier
                                .fillMaxSize(),
                    )
                }
            }
        }

        composable(Screen.TokenVerificationFailed.route) {
            TokenVerificationFailedScreen(
                onBackClick = {
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Login.route)
                    }
                },
            )
        }

        composable(
            route = Screen.PasswordRegistration.route,
            arguments = listOf(navArgument("email") { type = NavType.StringType }),
        ) { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")
            PasswordRegistrationScreen(
                email = email,
                onBackClick = {
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Login.route)
                    }
                },
                onPasswordRegistrationSucessClick = {
                    navController.popBackStack()
                    navController.popBackStack()
                },
                viewModel = hiltViewModel(),
            )
        }

        composable(Screen.PasswordRegistrationSuccess.route) {
            PasswordRegistrationSuccessScreen(
                onBackClick = {
                    navController.navigate(Screen.Login.route)
                },
            )
        }

        /** Coordinator Home Screen */
        composable(Screen.CoordinatorHome.route) {
            CoordinatorHomeScreen(
                navController = navController,
            )
        }

        /** Collaborator Home Screen */
        composable(Screen.CollaboratorHome.route) {
            CollaboratorHomeScreen()
        }

        composable(Screen.UserList.route) {
            UserListScreen(
                onCollabClick = { id ->
                    navController.navigate(Screen.UserDetail.createRoute(id))
                },
                onAddClick = {
                    navController.navigate(Screen.UserRegister.route)
                },
            )
        }

        /** User Detail Screen */
        composable(
            route = Screen.UserDetail.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            UserDetailScreen(
                onBackClick = { navController.popBackStack() },
                onEditClick = { id ->
                    // TODO: Navigate to edit screen when you create it
                },
                onDeleteClick = {
                    navController.navigate(Screen.UserList.route) {
                        popUpTo(Screen.UserList.route) { inclusive = true }
                    }
                },
            )
        }

        /** User Register Screen */
        composable(Screen.UserRegister.route) {
            UserRegisterScreen(
                onDismiss = { navController.popBackStack() },
                onSuccess = {
                    navController.popBackStack()
                },
            )
        }

        composable(Screen.UserList.route) {
            UserListScreen(
                onCollabClick = { id ->
                    navController.navigate(Screen.UserDetail.createRoute(id))
                },
                onAddClick = {
                    navController.navigate(Screen.UserRegister.route)
                },
            )
        }

        /**
         * Workshops List Screen.
         *
         * This composable represents the screen where users can view and interact with
         * the list of available workshops.
         *
         * It connects to the [WorkshopsListScreen] composable, which displays the UI and
         * interacts with its corresponding [WorkshopsListViewModel] to handle data fetching,
         * loading states, and errors.
         *
         */
        composable(Screen.WorkshopsList.route) {
            WorkshopsListScreen(
                navController = navController,
                workshopClick = { id ->
                    navController.navigate(Screen.WorkshopDetail.createRoute(id))
                },
            )
        }

        composable(
            route = Screen.WorkshopDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
        ) { backStackEntry ->
            val workshopId = backStackEntry.arguments?.getString("id") ?: ""
            WorkshopDetailScreen(navController, workshopId)
        }

        /**
         * Workshops Add Screen.
         *
         * This composable represents the screen where users can view and interact with
         * the register of a new workshop.
         *
         * It connects to the [AddNewWorkshopScreen] composable, which displays the UI and
         * interacts with its corresponding [AddNewWorkshopViewModel] to handle data fetching,
         * loading states, and errors.
         *
         */
        composable(Screen.AddNewWorkshop.route) {
            AddNewWorkshopScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                onSuccess = {
                    navController.popBackStack()
                },
            )
        }

        /**
         * Supply List Screen.
         *
         * This composable represents the screen where users can view and interact with
         * the list of available supplies.
         *
         * It connects to the [SupplyListScreen] composable, which displays the UI and
         * interacts with its corresponding [SuppliesListViewModel] to handle data fetching,
         * loading states, and errors.
         *
         */
        composable(Screen.SuppliesList.route) {
            SupplyListScreen(
                onSupplyClick = { id ->
                    navController.navigate("supply/$id")
                },
                onAddSupplyClick = {
                    navController.navigate(Screen.SupplyAdd.route)
                },
            )
        }

        composable(
            route = Screen.SupplyDetail.route,
            arguments = listOf(navArgument("idSupply") { type = NavType.StringType }),
        ) { backStackEntry ->
            val idSupply = backStackEntry.arguments?.getString("idSupply")
            SuppliesDetailScreen(
                idInsumo = idSupply ?: "",
                onBackClick = { navController.popBackStack() },
                onClickAddSupplyBatch = {
                    // TODO: Add when add a supply batch is ready
                },
                onClickDelete = {
                    // TODO: Add when delete a supply is ready
                },
                onClickModify = {
                    // TODO: Add when delete a supply is ready
                },
                modifySupplyBatch = {
                    // TODO: Add when delete a supply is ready
                },
                deleteSupplyBatch = {
                    // TODO: Add when delete a supply is ready
                },
            )
        }
        /**
         * Beneficiary List Screen.
         *
         * Shows the grid of beneficiaries.
         */
        composable(Screen.BeneficiaryList.route) {
            BeneficiaryListScreen(
                onBeneficiaryClick = { beneficiaryId ->
                    navController.navigate(Screen.BeneficiaryDetail.createRoute(beneficiaryId))
                },
                onFilterClick = { /* TODO: Lógica de VM */ },
                onNotificationClick = { /* TODO: Lógica de VM */ },
            )
        }

        /**
         * Beneficiary Detail Screen.
         *
         * Shows the details of a beneficiary and allows the function to eliminate them, others functionality are in progress.
         */
        composable(
            route = Screen.BeneficiaryDetail.route,
            arguments = listOf(navArgument("beneficiaryId") { type = NavType.StringType }),
        ) {
            BeneficiaryDetailScreen(
                onBackClick = { navController.popBackStack() },
                onModifyClick = { /* TODO: Lógica de VM */ },
                viewModel = hiltViewModel(),
            )
        }

        /**
         * Add New Supply Screen.
         *
         * Pantalla para registrar un nuevo insumo.
         */
        composable(Screen.SupplyAdd.route) {
            SupplyAddScreen(
                onSaveSuccess = {
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                },
            )
        }
    }
}

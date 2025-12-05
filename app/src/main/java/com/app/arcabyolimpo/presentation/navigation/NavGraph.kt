package com.app.arcabyolimpo.presentation.navigation

import android.util.Log
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
import com.app.arcabyolimpo.presentation.screens.attendance.AttendanceListScreen
import com.app.arcabyolimpo.presentation.screens.beneficiary.AddNewBeneficiaryScreen
import com.app.arcabyolimpo.presentation.screens.beneficiary.BeneficiaryDetailScreen
import com.app.arcabyolimpo.presentation.screens.beneficiary.BeneficiaryListScreen
import com.app.arcabyolimpo.presentation.screens.beneficiary.ModifyBeneficiaryScreen
import com.app.arcabyolimpo.presentation.screens.capacitations.DisabilitiesListScreen
import com.app.arcabyolimpo.presentation.screens.capacitations.DisabilityDetailScreen
import com.app.arcabyolimpo.presentation.screens.capacitations.disabilitiesRegister.DisabilitiesRegisterScreen
import com.app.arcabyolimpo.presentation.screens.home.assistant.CollaboratorHomeScreen
import com.app.arcabyolimpo.presentation.screens.home.coordinator.CoordinatorHomeScreen
import com.app.arcabyolimpo.presentation.screens.home.scholar.ScholarHomeScreen
import com.app.arcabyolimpo.presentation.screens.login.LoginScreen
import com.app.arcabyolimpo.presentation.screens.passwordrecovery.PasswordRecoveryScreen
import com.app.arcabyolimpo.presentation.screens.passwordregisteration.PasswordRegistrationScreen
import com.app.arcabyolimpo.presentation.screens.passwordregisteration.PasswordRegistrationSuccessScreen
import com.app.arcabyolimpo.presentation.screens.product.addProduct.ProductAddScreen
import com.app.arcabyolimpo.presentation.screens.product.list.ProductListScreen
import com.app.arcabyolimpo.presentation.screens.product.productDetail.ProductDetailScreen
import com.app.arcabyolimpo.presentation.screens.product.updateProduct.ProductUpdateScreen
import com.app.arcabyolimpo.presentation.screens.productbatches.productBatchDetail.ProductBatchDetailScreen
import com.app.arcabyolimpo.presentation.screens.productbatches.productBatchModify.ProductBatchModifyScreen
import com.app.arcabyolimpo.presentation.screens.productbatches.productBatchRegister.ProductBatchRegisterScreen
import com.app.arcabyolimpo.presentation.screens.productbatches.productBatchesList.ProductBatchesListScreen
import com.app.arcabyolimpo.presentation.screens.qr.qr.QrScreen
import com.app.arcabyolimpo.presentation.screens.qr.scanqr.ScanQrScreen
import com.app.arcabyolimpo.presentation.screens.qr.scanresult.ScanResultScreen
import com.app.arcabyolimpo.presentation.screens.qr.workshopselection.QrWorkshopsListScreen
import com.app.arcabyolimpo.presentation.screens.splash.SplashScreen
import com.app.arcabyolimpo.presentation.screens.supply.supplyAdd.SupplyAddScreen
import com.app.arcabyolimpo.presentation.screens.supply.supplyBatchList.SupplyBatchListScreen
import com.app.arcabyolimpo.presentation.screens.supply.supplyDetail.SuppliesDetailScreen
import com.app.arcabyolimpo.presentation.screens.supply.supplyList.SupplyListScreen
import com.app.arcabyolimpo.presentation.screens.supply.supplyUpdate.SupplyUpdateScreen
import com.app.arcabyolimpo.presentation.screens.supply.supplybatchmodify.SupplyBatchModifyScreen
import com.app.arcabyolimpo.presentation.screens.supply.supplybatchregister.SupplyBatchRegisterScreen
import com.app.arcabyolimpo.presentation.screens.tokenverification.TokenVerificationFailedScreen
import com.app.arcabyolimpo.presentation.screens.tokenverification.TokenVerificationViewModel
import com.app.arcabyolimpo.presentation.screens.user.detail.UserDetailScreen
import com.app.arcabyolimpo.presentation.screens.user.register.UserRegisterScreen
import com.app.arcabyolimpo.presentation.screens.user.updateuser.UpdateUserScreen
import com.app.arcabyolimpo.presentation.screens.workshop.AddNewWorkshopScreen
import com.app.arcabyolimpo.presentation.screens.workshop.WorkshopDetailScreen
import com.app.arcabyolimpo.presentation.screens.workshop.WorkshopsListScreen
import com.app.arcabyolimpo.presentation.screens.workshop.modifyWorkshopScreen

/**
 * Defines all available destinations (routes) in the application.
 *
 * Each screen is represented as a subclass of [Screen] with a unique [route].
 */
sealed class Screen(
    val route: String,
) {
    /**
     * Initial loading screen displayed while the app initializes,
     * checks authentication status, or prepares resources.
     */
    object Splash : Screen("splash")

    /**
     * Screen where users enter their credentials to access the application.
     */
    object Login : Screen("login")

    /**
     * Screen where users can request a password recovery email.
     */
    object PasswordRecovery : Screen("password-recovery")

    /**
     * Screen used for account activation requests, allowing users to resend
     * activation instructions or set up initial credentials.
     */
    object AccountActivation : Screen("account-activation")

    /**
     * Screen that displays the list of all registered users.
     *
     * This screen also supports search and filtering features.
     */
    object UserList : Screen("user")

    /**
     * Screen where a new user can be registered manually by an administrator.
     */
    object UserRegister : Screen("user_register")

    /**
     * Screen showing the detailed information of a specific user.
     *
     * Dynamic argument:
     * - userId: The ID of the user whose details should be displayed.
     *
     * @see createRoute Utility function for building the dynamic route.
     */
    object UserDetail : Screen("user_detail/{userId}") {
        /**
         * Builds a complete route for navigating to the User Detail screen.
         *
         * @param userId The ID of the user.
         * @return A formatted route string including the userId.
         */
        fun createRoute(userId: String) = "user_detail/$userId"
    }

    /**
     * Screen that allows editing the information of a specific user.
     *
     * Dynamic argument:
     * - userId: The ID of the user to be updated.
     *
     * @see createRoute Helper for constructing the navigation path.
     */
    object UpdateUserScreen : Screen("update_user/{userId}") {
        /**
         * Builds a dynamic route for navigating to the Update User screen.
         *
         * @param userId The ID of the user being edited.
         * @return A complete navigation route string.
         */
        fun createRoute(userId: String) = "update_user/$userId"
    }

    object AttendanceList : Screen("attendance_list/{userId}") {
        fun createRoute(userId: String) = "attendance_list/$userId"
    }

    /**
     * Screen used to verify a password recovery or account activation token.
     *
     * This route accepts a query parameter:
     * - token: The token received by email and required for backend verification.
     *
     * @see createRoute Helper for constructing a navigable route with the token included.
     */
    object TokenVerification : Screen("user/verify-token?token={token}") {
        /**
         * Builds a valid route for navigating to the token verification screen.
         *
         * @param token The verification token sent via email.
         * @return The complete route string including the token as a query parameter.
         */
        fun createRoute(token: String) = "user/verify-token?token=$token"
    }

    /**
     * Screen displayed when the token verification fails.
     *
     * This occurs when the token is invalid, expired, malformed,
     * or the backend rejects the validation for any reason.
     */
    object TokenVerificationFailed : Screen("token-activation-failed")

    /**
     * Screen where the user registers or restores their password.
     *
     * This route requires:
     * - email: The email associated with the account being recovered/activated.
     *
     * @see createRoute Builds the navigation route dynamically.
     */
    object PasswordRegistration : Screen("password-registration/{email}") {
        /**
         * Constructs the navigation route for the password registration screen.
         *
         * @param email The user’s email that needs a new password set.
         * @return A complete formatted route including the email parameter.
         */
        fun createRoute(email: String) = "password-registration/$email"
    }

    /**
     * Screen displayed after the password has been successfully registered or reset.
     */
    object PasswordRegistrationSuccess : Screen("pasword-registration-success")

    object CoordinatorHome : Screen("coordinator")

    object CollaboratorHome : Screen("collaborator")

    object ScholarHome : Screen("scholar")

    object SuppliesList : Screen("supply")

    /**
     * Screen that displays the full list of workshops.
     *
     * This screen serves as the entry point to browse all registered workshops.
     * It does not require any navigation arguments and simply loads the catalog
     * of workshops from the corresponding view model.
     */
    object WorkshopsList : Screen("workshop")

    /**
     * Screen that displays the detailed information of a single workshop.
     *
     * This route requires:
     * - id: The unique identifier of the workshop whose details will be shown.
     *
     * @see createRoute Builds the navigation route dynamically.
     */
    object WorkshopDetail : Screen("workshop/{id}") {
        /**
         * Constructs the navigation route for the workshop detail screen.
         *
         * @param id The unique identifier of the workshop to display.
         * @return A complete formatted route including the workshop id parameter.
         */
        fun createRoute(id: String): String = "workshop/$id"
    }

    /**
     * Screen where the user can register a new workshop.
     *
     * This route requires no parameters and opens the form
     * for adding a brand-new workshop to the system.
     */
    object AddNewWorkshop : Screen("workshop/add")

    /**
     * Screen where the user edits an existing workshop.
     *
     * This route requires:
     * - idTaller: The unique identifier of the workshop to be modified.
     *
     * @see createRoute Builds the navigation route dynamically.
     */
    object ModifyWorkshop : Screen("workshop/modify/{idTaller}") {
        /**
         * Constructs the navigation route for the modify workshop screen.
         *
         * @param idTaller The ID of the workshop the user intends to modify.
         * @return A complete formatted route including the workshop ID parameter.
         */
        fun createRoute(idTaller: String) = "workshop/modify/$idTaller"
    }

    object BeneficiaryList : Screen("beneficiary_list")

    object RegisterBatchSupply : Screen("register-batch-supply/{supplyId}") {
        fun createRoute(supplyId: String) = "register-batch-supply/$supplyId"
    }

    object BeneficiaryDetail : Screen("beneficiary_detail/{beneficiaryId}") {
        fun createRoute(beneficiaryId: String) = "beneficiary_detail/$beneficiaryId"
    }

    object AddNewBeneficiary : Screen("beneficiary/create")

    object ModifyBeneficiary : Screen("beneficiary/update/{beneficiaryId}") {
        fun createRoute(beneficiaryId: String) = "beneficiary/update/$beneficiaryId"
    }

    object CapacitationScreen : Screen("/disabilities/list")

    object DisabilitiesRegisterScreen : Screen("/disabilities/register")

    object DisabilityDetail : Screen("disability/{disabilityId}") {
        fun createRoute(disabilityId: String) = "disability/$disabilityId"
    }

    object SupplyDetail : Screen("supply/{idSupply}") {
        fun createRoute(idSupply: String) = "supply/$idSupply"
    }

    object SupplyAdd : Screen("supply/add")

    object ProductBatchesList : Screen("product_batches")

    object ProductBatchDetail : Screen("product_batch_detail/{batchId}") {
        fun createRoute(batchId: String) = "product_batch_detail/$batchId"
    }

    object ProductBatchRegister : Screen("product_batch_register")

    object ProductBatchModify : Screen("product_batch_modify/{batchId}") {
        fun createRoute(batchId: String) = "product_batch_modify/$batchId"
    }

    object ProductAdd : Screen("product/add")

    object ProductDeleteTest : Screen("test_delete_product")

    object SupplyBatchModify : Screen("supply_batch_modify/{id}") {
        fun createRoute(batchId: String) = "supply_batch_modify/$batchId"
    }

    object SupplyBatchList : Screen("supply_batch_list/dates/{date}?idInsumo={idInsumo}") {
        fun createRoute(
            date: String,
            idInsumo: String,
        ) = "supply_batch_list/dates/$date?idInsumo=$idInsumo"
    }

    object ProductDetail : Screen("product/{productId}") {
        fun createRoute(productId: String) = "product/$productId"
    }

    object SupplyUpdate : Screen("supply/update/{idSupply}") {
        fun createRoute(idSupply: String) = "supply/update/$idSupply"
    }

    object ProductUpdate : Screen("product/update/{idProduct}") {
        fun createRoute(idProduct: String) = "product/update/$idProduct"
    }

    object ProductList : Screen("product/")

    /**
     * Screen for selecting a workshop before creating a QR code.
     */
    object QrWorkshopSelection : Screen("qr/workshop_selection")

    /**
     * Screen that displays the generated QR code for a selected workshop.
     *
     * This route requires:
     * - [workshopID]   ID of the workshop the QR belongs to.
     * - [workshopName] Name of the workshop to show on the UI.
     *
     * @see createRoute Used to build the route dynamically.
     */
    object CreateQr : Screen("qr/workshop_selection/show_qr/{workshopID}/{workshopName}") {
        /**
         * Constructs a valid navigation route including the workshop parameters.
         *
         * @param workshopID The identifier of the selected workshop.
         * @param workshopName The display name of the workshop.
         * @return A fully formatted navigation route.
         */
        fun createRoute(
            workshopID: String,
            workshopName: String,
        ) = "qr/workshop_selection/show_qr/$workshopID/$workshopName"
    }

    /**
     * Screen where the camera is used to scan QR codes.
     */
    object ScanQr : Screen("qr/scan_qr")

    /**
     * Screen responsible for validating the scanned QR content.
     */
    object ValidateQr : Screen("qr/scan_qr/validate_qr")
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
        // TODO: Cambiar a Screen.Splash.route cuando acabe
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
                        navController.navigate(Screen.CoordinatorHome.route) {
                            popUpTo(Screen.Splash.route) { inclusive = true }
                        }

                    UserRole.BECARIO ->
                        navController.navigate(Screen.ScholarHome.route) {
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
                            navController.navigate(Screen.CoordinatorHome.route) {
                                popUpTo(Screen.Login.route) { inclusive = true }
                            }

                        UserRole.BECARIO ->
                            navController.navigate(Screen.ScholarHome.route) {
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

        /**
         * Password Recovery Screen Navigation.
         *
         * Navigates to the screen where users can initiate the process of recovering
         * their password by submitting their registered email address.
         *
         * If the back stack is empty, navigation returns to the Login screen.
         */
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

        /**
         * Account Activation Screen Navigation.
         *
         * Displays the interface used to activate a user account after email verification.
         * This screen is accessed after requesting an activation link.
         *
         * If the back stack cannot be popped, the flow redirects to Login.
         */
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

        /**
         * Token Verification Screen Navigation.
         *
         * Handles deep links sent to the user via email to verify account or password
         * reset tokens.
         *
         * This composable:
         * - Extracts the token from navigation arguments or the deep link.
         * - Triggers token validation through [TokenVerificationViewModel].
         * - Displays different screens depending on validation state:
         *      - [PasswordRegistrationScreen] if the token is valid.
         *      - A loading shimmer during verification.
         *      - [TokenVerificationFailedScreen] if the token is invalid or expired.
         *
         * Deep Link format:
         *  arcabyolimpo://user/verify-token?token={token}
         */
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

            /** Depending on the response, it navigates different screen */
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

        /**
         * Token Verification Failed Screen Navigation.
         *
         * Displays an error message when the user accesses the app using an invalid,
         * malformed, or expired verification token.
         *
         * If the back stack is empty, the user is returned to the Login screen.
         */
        composable(Screen.TokenVerificationFailed.route) {
            TokenVerificationFailedScreen(
                onBackClick = {
                    if (!navController.popBackStack()) {
                        navController.navigate(Screen.Login.route)
                    }
                },
            )
        }

        /**
         * Password Registration Screen Navigation.
         *
         * Screen where the user creates a new password after a valid token is verified.
         * Receives the user's email as an argument.
         *
         * On successful password creation:
         * - The function pops two screens to return to the previous flow
         *   (usually the token verification or password recovery sequence).
         */
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

        /**
         * Password Registration Success Screen Navigation.
         *
         * Shows a confirmation message to the user after successfully creating a new
         * password. Redirects back to the Login screen upon exit.
         */
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
            CollaboratorHomeScreen(navController)
        }

        /** Scholar Home Screen */
        composable(Screen.ScholarHome.route) {
            ScholarHomeScreen(
                navController = navController,
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
                    navController.navigate(Screen.UpdateUserScreen.createRoute(id))
                },
                onDeleteClick = { navController.popBackStack() },
                onAttendanceClick = { id ->
                    navController.navigate(Screen.AttendanceList.createRoute(id))
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

        /** User Update Screen */
        composable(
            route = Screen.UpdateUserScreen.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val userId = requireNotNull(backStackEntry.arguments?.getString("userId"))

            UpdateUserScreen(
                onDismiss = { navController.popBackStack() },
                onSuccess = {
                    // avisa al detalle que debe recargar y regresa
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("refresh_detail_$userId", true)
                    navController.popBackStack()
                },
            )
        }

        /** Attendance List Screen */
        composable(
            route = Screen.AttendanceList.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType }),
        ) {
            AttendanceListScreen(
                onBackClick = { navController.popBackStack() },
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

        /**
         * Workshops Detail Screen.
         *
         * This composable represents the screen where users can view and interact with
         * the detail of a workshop.
         *
         * It connects to the [WorkshopDetailScreen] composable, which displays the UI and
         * interacts with its corresponding [WorkshopDetailViewModel] to handle data fetching,
         * loading states, and errors.
         *
         */
        composable(
            route = Screen.WorkshopDetail.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
        ) { backStackEntry ->
            val workshopId = backStackEntry.arguments?.getString("id") ?: ""
            WorkshopDetailScreen(
                navController,
                workshopId,
                onModifyClick = { workshopId ->
                    navController.navigate(Screen.ModifyWorkshop.createRoute(workshopId))
                },
            )
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
         * Workshops Modify Screen.
         *
         * This composable represents the screen where users can modify the data
         * of an existing workshop. It receives the workshop's unique identifier
         * through navigation arguments.
         *
         * It connects to the [modifyWorkshopScreen] composable, which displays the UI and
         * interacts with its corresponding [ModifyWorkshopViewModel] to handle current data loading,
         * update operations, validation, and error states.
         *
         * Navigation Arguments:
         * - idTaller: The unique ID of the workshop to be edited.
         */
        composable(
            route = Screen.ModifyWorkshop.route,
            arguments = listOf(navArgument("idTaller") { type = NavType.StringType }),
        ) { backStackEntry ->
            val workshopId = backStackEntry.arguments?.getString("idTaller") ?: ""

            modifyWorkshopScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                workshopId = workshopId,
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
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        /**

         * Supply Batch Register Screen.
         *
         * This composable represents the screen where users can view and interact with
         * the register of a new supply batch.
         *
         * It connects to the [SupplyBatchRegisterScreen] composable, which displays the UI and
         * interacts with its corresponding [SupplyBatchRegisterViewModel] to handle data fetching,
         * loading states, and errors.
         *
         */
        composable(
            Screen.RegisterBatchSupply.route,
            arguments = listOf(navArgument("supplyId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val supplyId = backStackEntry.arguments?.getString("supplyId") ?: ""

            SupplyBatchRegisterScreen(
                supplyId = supplyId,
                onRegisterClick = {
                    // Pass a snackbar message to the previous back stack entry so it shows the toast after navigation
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("snackbarMessage", "Lote registrado correctamente")
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("snackbarSuccess", true)
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        composable(
            Screen.SupplyBatchModify.route,
            arguments = listOf(navArgument("id") { type = NavType.StringType }),
        ) { backStackEntry ->
            val batchId = backStackEntry.arguments?.getString("id") ?: ""
            SupplyBatchModifyScreen(
                supplyBatchId = batchId,
                onRegisterClick = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("snackbarMessage", "Lote modificado correctamente")
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("snackbarSuccess", true)
                    navController.popBackStack()
                },
                onBackClick = {
                    navController.popBackStack()
                },
            )
        }

        composable(
            route = Screen.SupplyDetail.route,
            arguments = listOf(navArgument("idSupply") { type = NavType.StringType }),
        ) { backStackEntry ->
            val idSupply = backStackEntry.arguments?.getString("idSupply")
            SuppliesDetailScreen(
                navController = navController,
                idInsumo = idSupply ?: "",
                onBackClick = { navController.popBackStack() },
                onClickAddSupplyBatch = {
                    navController.navigate(Screen.RegisterBatchSupply.createRoute(idSupply ?: ""))
                },
                onClickDelete = {
                    // TODO: Add when delete a supply is ready
                },
                onClickModify = {
                    if (idSupply != null) {
                        navController.navigate(Screen.SupplyUpdate.createRoute(idSupply))
                    }
                },
                onViewBatches = { date, supplyId ->
                    // Navigate to the batch list for this supply and date.
                    try {
                        android.util.Log.d("NavGraph", "Navigating to SupplyBatchList with date=$date, idSupply=$supplyId")
                        navController.navigate(Screen.SupplyBatchList.createRoute(date, supplyId))
                    } catch (e: Exception) {
                        android.util.Log.e("NavGraph", "Failed to navigate to SupplyBatchList", e)
                    }
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
                navController = navController,
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
                onModifyClick = { beneficiaryId ->
                    navController.navigate(Screen.ModifyBeneficiary.createRoute(beneficiaryId))
                },
                viewModel = hiltViewModel(),
                beneficiaryId = it.arguments?.getString("beneficiaryId") ?: "",
            )
        }

        /**
         * Modify Beneficiary Screen.
         *
         * Allows the modification of an existing and active beneficiary.
         */
        composable(
            route = Screen.ModifyBeneficiary.route,
            arguments = listOf(navArgument("beneficiaryId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val beneficiaryId = backStackEntry.arguments?.getString("beneficiaryId") ?: ""
            ModifyBeneficiaryScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                beneficiaryId = beneficiaryId,
            )
        }

        /**
         * Capacitations Screen.
         *
         * Shows a list of the disabilities created by the users
         */

        composable(route = Screen.CapacitationScreen.route) {
            DisabilitiesListScreen(
                navController = navController,
                onDisabilityClick = { id ->
                    Log.d("Click", "Click")
                    // TODO: Navigate to disability detail when screen is created
                    navController.navigate(Screen.DisabilityDetail.createRoute(id))
                },
                onAddClick = { navController.navigate(Screen.DisabilitiesRegisterScreen.route) },
                onBackClick = { navController.popBackStack() },
            )
        }

        /*** The screen for registering a new disability.
         * Navigates back to the previous screen on back click, and to the disabilities list
         * screen upon successful creation.
         */
        composable(Screen.DisabilitiesRegisterScreen.route) {
            DisabilitiesRegisterScreen(
                onCreated = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() },
            )
        }

        /**
         * Disability Detail Screen.
         *
         * Shows the details of a disability.
         */
        composable(
            route = Screen.DisabilityDetail.route,
        ) {
            DisabilityDetailScreen(
                onBackClick = { navController.popBackStack() },
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
                onBackClick = { navController.popBackStack() },
            )
        }
        /**
         * Add new beneficiary
         */

        composable(Screen.AddNewBeneficiary.route) {
            AddNewBeneficiaryScreen(
                navController = navController,
                viewModel = hiltViewModel(),
                onSuccess = {
                    navController.popBackStack()
                },
            )
        }

        /**
         * Product Batches List Screen.
         *
         * Displays a list of product batches and allows navigation to details or registration.
         * DetailClick -> navigates to ProductBatchDetailScreen
         * AddClick -> navigates to ProductBatchRegisterScreen
         */
        composable(Screen.ProductBatchesList.route) {
            ProductBatchesListScreen(
                onBackClick = { navController.popBackStack() },
                onDetailClick = { id ->
                    navController.navigate(Screen.ProductBatchDetail.createRoute(id))
                },
                onAddClick = {
                    navController.navigate(Screen.ProductBatchRegister.route)
                },
            )
        }

        /**
         * Product Batches Detail Screen.
         *
         * Displays a view of product batch.
         */
        composable(
            route = Screen.ProductBatchDetail.route,
            arguments = listOf(navArgument("batchId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val batchId = backStackEntry.arguments?.getString("batchId") ?: ""
            ProductBatchDetailScreen(
                batchId = batchId,
                onModifyClick = { id ->
                    navController.navigate(Screen.ProductBatchModify.createRoute(id))
                },
                onBackClick = { navController.popBackStack() },
            )
        }

        /**
         * Product Batches Register Screen.
         *
         * Allows the registration of a new product batch.
         */
        composable(Screen.ProductBatchRegister.route) {
            ProductBatchRegisterScreen(
                onCreated = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() },
            )
        }

        /**
         * Product Batches Modify Screen.
         *
         * Allows the modification of a defined product batch.
         */
        composable(
            route = Screen.ProductBatchModify.route,
            arguments = listOf(navArgument("batchId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val batchId = backStackEntry.arguments?.getString("batchId") ?: ""
            ProductBatchModifyScreen(
                batchId = batchId,
                onModified = { navController.popBackStack() },
                onBackClick = { navController.popBackStack() },
            )
        }

        composable(Screen.ProductAdd.route) {
            ProductAddScreen(
                onSaveSuccess = {
                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                },
            )
        }

        composable(Screen.ProductList.route) {
            ProductListScreen(
                onProductClick = { productId ->
                    navController.navigate(Screen.ProductDetail.createRoute(productId))
                },
                onAddProductClick = {
                    navController.navigate(Screen.ProductAdd.route)
                },
                onBackClick = {
                    navController.navigateUp()
                },
            )
        }

        composable(
            route = Screen.ProductDetail.route,
            arguments = listOf(navArgument("productId") { type = NavType.StringType }),
        ) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(
                productId = productId,
                onBackClick = { navController.popBackStack() },
                onEditClick = { id ->
                    navController.navigate(Screen.ProductUpdate.createRoute(id))
                },
                onDeleteClick = {
                    navController.popBackStack()
                },
            )
        }

        composable(
            route = Screen.ProductUpdate.route,
            arguments =
                listOf(
                    navArgument("idProduct") { type = NavType.StringType },
                ),
        ) {
            ProductUpdateScreen(
                onModifyClick = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("shouldRefresh", true)

                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                },
            )
        }

        composable(
            Screen.SupplyBatchList.route,
            arguments =
                listOf(
                    navArgument("date") { type = NavType.StringType },
                    navArgument("idInsumo") {
                        type = NavType.StringType
                        defaultValue = ""
                    },
                ),
        ) { backStackEntry ->
            val date = backStackEntry.arguments?.getString("date") ?: ""
            val idInsumo = backStackEntry.arguments?.getString("idInsumo") ?: ""
            android.util.Log.d("NavGraph", "SupplyBatchList args received: date=$date, idInsumo=$idInsumo")
            SupplyBatchListScreen(
                supplyId = idInsumo,
                supplyName = "",
                date = date,
                onModifyClick = { batchId ->
                    android.util.Log.d("NavGraph", "Modify clicked, batchId=$batchId")
                    if (batchId.isNotBlank()) {
                        navController.navigate(Screen.SupplyBatchModify.createRoute(batchId))
                    }
                },
                onBackClick = { navController.popBackStack() },
            )
        }

        composable(
            route = Screen.SupplyUpdate.route,
            arguments = listOf(navArgument("idSupply") { type = NavType.StringType }),
        ) { backStackEntry ->

            SupplyUpdateScreen(
                onModifyClick = {
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("shouldRefresh", true)

                    navController.popBackStack()
                },
                onCancel = {
                    navController.popBackStack()
                },
                onBackClick = { navController.popBackStack() },
            )
        }

        /**
         * QR Workshop Selection Screen Navigation.
         *
         * Displays the list of available workshops for which a coordinator can generate
         * a QR code. When a workshop is selected, navigation proceeds to the QR creation
         * screen using the workshop's ID and name.
         *
         * This composable hosts the [QrWorkshopsListScreen] and handles back navigation
         * by popping the current screen from the back stack.
         */
        composable(Screen.QrWorkshopSelection.route) {
            QrWorkshopsListScreen(
                onBackClick = { navController.popBackStack() },
                workshopClick = { id, name ->
                    navController.navigate(Screen.CreateQr.createRoute(id, name))
                },
            )
        }

        /**
         * QR Creation Screen Navigation.
         *
         * This screen displays the generated QR code for a specific workshop.
         * It receives two navigation arguments:
         *  - `workshopID`: Identifier for the selected workshop.
         *  - `workshopName`: Display name of the workshop.
         *
         * These values are extracted from the back stack and passed to [QrScreen],
         * where the QR is generated and presented to the user.
         */
        composable(
            route = Screen.CreateQr.route,
            arguments =
                listOf(
                    navArgument("workshopID") { type = NavType.StringType },
                    navArgument("workshopName") { type = NavType.StringType },
                ),
        ) { backStackEntry ->

            val id = backStackEntry.arguments?.getString("workshopID") ?: ""
            val name = backStackEntry.arguments?.getString("workshopName") ?: ""

            QrScreen(
                onBackClick = { navController.popBackStack() },
                workshopId = id,
                workshopName = name,
            )
        }

        /**
         * QR Scanning Screen Navigation.
         *
         * Opens the camera interface allowing users to scan the QR code of a workshop.
         * Upon successful scan, the scanned value is stored in the `savedStateHandle`
         * of the current back stack entry. Navigation then proceeds to the QR
         * validation screen.
         *
         * This composable hosts the [ScanQrScreen] and supports returning to the
         * previous screen through the back stack.
         */
        composable(Screen.ScanQr.route) {
            ScanQrScreen(
                onBackClick = { navController.popBackStack() },
                onScanSuccess = { qrValue ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("qrValue", qrValue)
                    navController.navigate(Screen.ValidateQr.route)
                },
            )
        }

        /**
         * QR Validation Screen Navigation.
         *
         * Retrieves the scanned QR value stored in the previous back stack entry’s
         * `savedStateHandle` and displays the validation result via [ScanResultScreen].
         *
         * This screen shows whether the scanned QR corresponds to a valid workshop
         * entry and allows the user to return to the scanning interface.
         */
        composable(route = Screen.ValidateQr.route) { backStackEntry ->
            val qrValue =
                navController.previousBackStackEntry
                    ?.savedStateHandle
                    ?.get<String>("qrValue")

            ScanResultScreen(
                onBackClick = {
                    navController.popBackStack()
                },
                qrValue = qrValue,
            )
        }
    }
}

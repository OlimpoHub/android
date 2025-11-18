package com.app.arcabyolimpo.presentation.screens.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.app.arcabyolimpo.R
import com.app.arcabyolimpo.presentation.navigation.Screen
import com.app.arcabyolimpo.presentation.screens.user.UserListScreen
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.ConfigurationIcon
import com.app.arcabyolimpo.presentation.ui.components.atoms.icons.NotificationIcon
import com.app.arcabyolimpo.presentation.ui.components.molecules.HomeScreenCard
import com.app.arcabyolimpo.ui.theme.ArcaByOlimpoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Suppress("ktlint:standard:function-naming")
@Composable
fun HomeScreen(navController: NavHostController) {
    var selectedOption by rememberSaveable { mutableStateOf<String?>(null) }

    ArcaByOlimpoTheme(darkTheme = true, dynamicColor = false) {
        Box(modifier = Modifier.fillMaxSize()) {
            Scaffold(
                containerColor = Color(0xFF040610),
                topBar = {
                    // SOLO SE MUESTRA CUANDO selectedOption ES NULL (MENU PRINCIPAL)
                    if (selectedOption == null) {
                        TopBarContent()
                    }
                },
            ) { paddingValues ->

                // ------------ CONTENIDO SEGÚN ESTADO -----------
                if (selectedOption == null) {
                    MenuPrincipal(
                        paddingValues = paddingValues,
                        onSelect = { selectedOption = it },
                    )
                } else {
                    when (selectedOption) {
                        "qr" -> QROptionScreen { selectedOption = null }
                        "usuarios" ->
                            UserListScreen(
                                onCollabClick = { id ->
                                    navController.navigate(Screen.UserDetail.createRoute(id))
                                },
                                onAddClick = {
                                    navController.navigate(Screen.UserRegister.route)
                                },
                                onBack = { selectedOption = null },
                            )
                        "caps" -> CapsScreen { selectedOption = null }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarContent() {
    Column(modifier = Modifier.fillMaxWidth()) {
        TopAppBar(
            title = {
                Column {
                    Text(
                        text = "Bienvenid@, ",
                        style = MaterialTheme.typography.headlineSmall,
                        color = Color(0xFFCCCCCC),
                        fontWeight = FontWeight.Normal,
                    )
                    Text(
                        text = "Jorge Garzón",
                        style = MaterialTheme.typography.headlineLarge,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                    )
                }
            },
            actions = {
                Box(modifier = Modifier.padding(end = 28.dp)) { NotificationIcon() }
                Box(modifier = Modifier.padding(end = 28.dp)) { ConfigurationIcon() }
            },
        )
    }
}

@Composable
fun MenuPrincipal(
    paddingValues: PaddingValues,
    onSelect: (String) -> Unit,
) {
    Column(
        modifier =
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(vertical = 24.dp, horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HomeScreenCard(
            name = "Generar QR de asistencia",
            image = painterResource(id = R.drawable.img_arca_logo),
            onClick = { onSelect("qr") },
        )

        Text(
            text = "Menú principal",
            style = MaterialTheme.typography.headlineMedium,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Left,
            modifier = Modifier.fillMaxWidth(),
        )

        HomeScreenCard(
            name = "Usuarios",
            image = painterResource(id = R.drawable.img_arca_logo),
            onClick = { onSelect("usuarios") },
        )

        HomeScreenCard(
            name = "Capacitaciones",
            image = painterResource(id = R.drawable.img_arca_logo),
            onClick = { onSelect("caps") },
        )
    }
}

@Composable
fun QROptionScreen(onBack: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.Start,
    ) {
        Text(
            text = "< Regresar",
            color = Color.Cyan,
            modifier = Modifier.padding(bottom = 16.dp).clickable { onBack() },
        )

        Text("Pantalla QR", color = Color.White, style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun UsuariosScreen(onBack: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Text(
            text = "< Regresar",
            color = Color.Cyan,
            modifier = Modifier.padding(bottom = 16.dp).clickable { onBack() },
        )

        Text("Pantalla Usuarios", color = Color.White, style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun CapsScreen(onBack: () -> Unit) {
    Column(
        Modifier
            .fillMaxSize()
            .padding(24.dp),
    ) {
        Text(
            text = "< Regresar",
            color = Color.Cyan,
            modifier = Modifier.padding(bottom = 16.dp).clickable { onBack() },
        )

        Text("Pantalla Capacitaciones", color = Color.White, style = MaterialTheme.typography.headlineMedium)
    }
}

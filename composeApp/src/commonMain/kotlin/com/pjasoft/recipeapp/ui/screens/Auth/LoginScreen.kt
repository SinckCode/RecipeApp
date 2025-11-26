package com.pjasoft.recipeapp.ui.screens.Auth

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.pjasoft.recipeapp.data.services.Preferences
import com.pjasoft.recipeapp.ui.Components.LoadingOverlay
import com.pjasoft.recipeapp.ui.RecipeTheme
import com.pjasoft.recipeapp.ui.screens.Auth.components.AuthBackGround
import com.pjasoft.recipeapp.ui.screens.Auth.components.AuthCard
import com.pjasoft.recipeapp.ui.screens.Auth.components.AuthTextField
import com.pjasoft.recipeapp.ui.screens.Auth.components.PrimaryButton
import com.pjasoft.recipeapp.ui.screens.LoginScreenRoute
import com.pjasoft.recipeapp.ui.screens.MainScreenRoute
import com.pjasoft.recipeapp.ui.screens.RegisterScreenRoute
import com.pjasoft.recipeapp.ui.viewmodels.AuthViewModel
import org.jetbrains.compose.ui.tooling.preview.Preview
import kotlin.reflect.KClass

@Composable
fun LoginScreen(
    navController: NavController
) {
    val colorScheme = MaterialTheme.colorScheme

    // ViewModel con factory (como ya lo tenías)
    val viewModel: AuthViewModel = viewModel(
        factory = object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(
                modelClass: KClass<T>,
                extras: CreationExtras
            ): T {
                return AuthViewModel() as T
            }
        }
    )

    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    // Leemos el estado de login desde Preferences solo una vez
    var isLogged by remember { mutableStateOf(Preferences.getIsLogged()) }

    // Para mostrar errores de login en pantalla
    var errorMessage by remember { mutableStateOf<String?>(null) }

    // Si ya está logueado (por token guardado), navega directo al main
    LaunchedEffect(isLogged) {
        if (isLogged) {
            navController.navigate(MainScreenRoute) {
                popUpTo(LoginScreenRoute) { inclusive = true }
            }
        }
    }

    Box(Modifier.fillMaxSize()) {
        // Fondo de gradiente / imagen
        AuthBackGround()

        // Tarjeta centrada
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Center)
                .padding(horizontal = 20.dp)
        ) {
            AuthCard(
                title = "Bienvenido"
            ) {
                Spacer(Modifier.height(10.dp))

                AuthTextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = "Correo electrónico",
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(10.dp))

                AuthTextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = "Contraseña",
                    isPassword = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(Modifier.height(14.dp))

                PrimaryButton(
                    text = "Iniciar sesión",
                    onClick = {
                        errorMessage = null // limpiamos errores anteriores

                        viewModel.login(
                            email = email,
                            password = password
                        ) { result, message ->
                            if (result) {
                                // Marcamos como logueado y navegamos al Main
                                isLogged = true
                                navController.navigate(MainScreenRoute) {
                                    popUpTo(LoginScreenRoute) { inclusive = true }
                                }
                            } else {
                                // Mostramos el mensaje de error
                                errorMessage = message
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                )

                Spacer(Modifier.height(8.dp))

                // Texto de error (si existe)
                errorMessage?.let { msg ->
                    Spacer(Modifier.height(4.dp))
                    Text(
                        text = msg,
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }

                Spacer(Modifier.height(12.dp))

                Text(
                    text = "¿No tienes una cuenta? Crea una",
                    style = MaterialTheme.typography.labelSmall,
                    color = colorScheme.primary,
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .clickable {
                            navController.navigate(RegisterScreenRoute)
                        }
                )
            }
        }

        // Overlay de carga mientras el ViewModel está haciendo la petición
        if (viewModel.isLoading) {
            LoadingOverlay(
                colors = colorScheme,
                message = "Iniciando sesión…"
            )
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    RecipeTheme {
        LoginScreen(
            navController = rememberNavController()
        )
    }
}

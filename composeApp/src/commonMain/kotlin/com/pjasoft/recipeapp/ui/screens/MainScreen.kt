package com.pjasoft.recipeapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.pjasoft.recipeapp.ui.screens.HomeScreen.HomeScreen

@Composable
fun MainScren(navController: NavHostController) {
    // Aquí podrías poner un Scaffold, bottom bar, etc.
    HomeScreen(navController = navController)
}
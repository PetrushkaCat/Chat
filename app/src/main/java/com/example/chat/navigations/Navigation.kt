package com.example.chat.navigations

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chat.LoginViewModel
import com.example.chat.screens.*

@Composable
fun Navigation() {
    val loginViewModel: LoginViewModel = viewModel()

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.LoginScreen.route) {
        composable(route = Screen.LoginScreen.route) {
            LoginScreen(loginViewModel = loginViewModel, navController = navController)
        }

        composable(route = Screen.ForgotPasswordScreen.route) {
            ForgotPasswordScreen(navController = navController)
        }

        composable(route = Screen.SignUpScreen.route) {
            SignUpScreen(navController = navController, loginViewModel = loginViewModel)
        }

        composable(route = Screen.ChatScreen.route) {
            ChatScreen(navController = navController)
        }

        composable(route = Screen.ProfileScreen.route) {
            ProfileScreen(navController = navController)
        }

    }


}
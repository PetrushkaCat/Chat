package com.example.chat.navigations

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.chat.screens.login.LoginViewModel
import com.example.chat.screens.login.ForgotPasswordScreen
import com.example.chat.screens.login.LoginScreen
import com.example.chat.screens.login.SignUpScreen

@Composable
fun Navigation() {
    val loginViewModel = hiltViewModel<LoginViewModel>()

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

        composable(route = Screen.MainContentScreen.route) {
            MainContent()
        }

    }


}
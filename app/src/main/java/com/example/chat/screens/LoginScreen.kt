package com.example.chat.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.navigation.NavController
import com.example.chat.viewmodels.LoginViewModel
import com.example.chat.navigations.Screen
import kotlinx.coroutines.*


@Preview(showBackground = true)
@Composable
fun LoginPreview() {
    //LoginScreen(LoginViewModel())
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navController: NavController) {

    var buttonClicked by rememberSaveable { mutableStateOf(false) }

    val mContext = LocalContext.current

    loginViewModel.isLoginSuccessful.observe(LocalLifecycleOwner.current) {
        if (it == true && buttonClicked) {
            navController.navigate(Screen.MainContentScreen.route)
            Log.d("navigation", "login successful")
            buttonClicked = false
        } else if (buttonClicked) {
            Toast.makeText(mContext, "Login failed", Toast.LENGTH_SHORT).show()
            Log.d("navigation", "login failed")
            buttonClicked = false
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {
        ClickableText(text = AnnotatedString("Sign up"),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            onClick = {
                      navController.navigate(Screen.SignUpScreen.route)
            },
            style = TextStyle(
            fontSize = 14.sp,
            fontFamily = FontFamily.Default,
            textDecoration = TextDecoration.Underline,
            color = Color.Blue
            )
        )
    }

    Column(
        modifier = Modifier.padding(20.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val email = rememberSaveable { mutableStateOf("") }
        val password = rememberSaveable { mutableStateOf("") }

        val coroutineScope = rememberCoroutineScope()

        Text(text = "Login", style = TextStyle(fontSize = 40.sp, fontFamily = FontFamily.Default))

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Email") },
            value = email.value,
            onValueChange = { email.value = it })

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Password") },
            value = password.value,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = { password.value = it })

        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    coroutineScope.launch {
                         loginViewModel.login(email.value, password.value)
                    }
                    buttonClicked = true
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Login")
            }
        }

        Spacer(modifier = Modifier.height(20.dp))
        ClickableText(
            text = AnnotatedString("Forgot Password?"),
            onClick = {
                navController.navigate(Screen.ForgotPasswordScreen.route)
            },
            style = TextStyle(
                fontSize = 14.sp,
                fontFamily = FontFamily.Default
            )
        )

    }
}


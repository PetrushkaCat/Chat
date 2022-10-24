package com.example.chat.screens

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.chat.viewmodels.LoginViewModel
import com.example.chat.navigations.Screen
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(loginViewModel: LoginViewModel, navController: NavController) {

    var buttonClicked by rememberSaveable { mutableStateOf(false) }

    val mContext = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    loginViewModel.isSignUpSuccessful.observe(lifecycleOwner) {
        if (it == true && buttonClicked) {
            Toast.makeText(mContext, "Signed up successfully!", Toast.LENGTH_LONG)
                .show()
            navController.navigate(Screen.LoginScreen.route)
            buttonClicked = false
        } else if(buttonClicked){
            Toast.makeText(mContext, "Incorrect data", Toast.LENGTH_LONG).show()
            buttonClicked = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        ClickableText(text = AnnotatedString("Back to login page"),
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(20.dp),
            onClick = {
                      navController.navigate(Screen.LoginScreen.route)
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
        val password2 = rememberSaveable { mutableStateOf("") }

        val coroutineScope = rememberCoroutineScope()

        TextField(
            label = { Text(text = "Email") },
            value = email.value,
            onValueChange = { email.value = it})

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Password") },
            value = password.value,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = {password.value = it})

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Repeat password") },
            value = password2.value,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            onValueChange = {password2.value = it})

        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    coroutineScope.launch {
                    (loginViewModel.signUp(
                                email.value,
                                password.value,
                                password2.value))
                    }
                    buttonClicked = true
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Sign up")
            }
        }

    }

}
package com.example.chat.screens.login

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chat.navigations.Screen
import com.example.chat.screens.login.LoginViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(navController: NavController) {

    val loginViewModel = hiltViewModel<LoginViewModel>()

    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val buttonClicked = rememberSaveable { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val email = rememberSaveable { mutableStateOf("") }

    loginViewModel.isEmailSent.observe(LocalLifecycleOwner.current) {
        if (buttonClicked.value && it == true) {
            Toast.makeText(context, "Email sent successfully", Toast.LENGTH_LONG).show()
            focusManager.clearFocus()
            email.value = ""
            buttonClicked.value = false

        } else if(buttonClicked.value) {
            Toast.makeText(context, "Email not sent.\nMake sure email address is correct.", Toast.LENGTH_LONG).show()
            buttonClicked.value = false
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

        Text(text = "We will send you password reset email",
            style = TextStyle(fontSize = 30.sp,
                fontFamily = FontFamily.Default,
                textAlign = TextAlign.Center))

        Spacer(modifier = Modifier.height(20.dp))

        TextField(
            label = { Text(text = "Email") },
            value = email.value,
            onValueChange = { email.value = it },
            singleLine = true)

        Spacer(modifier = Modifier.height(20.dp))

        Box(modifier = Modifier.padding(40.dp, 0.dp, 40.dp, 0.dp)) {
            Button(
                onClick = {
                    scope.launch {
                        loginViewModel.sendResetPasswordEmail(email.value)
                    }
                    buttonClicked.value = true
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text(text = "Submit request")
            }
        }

    }
}

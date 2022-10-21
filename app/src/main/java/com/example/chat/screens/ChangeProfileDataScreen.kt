package com.example.chat.screens

import android.app.Activity
import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chat.ProfileViewModel
import com.example.chat.navigations.MainScreens
import com.example.chat.ui.theme.Purple40
import com.example.domain.model.UserProfileData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@Composable
fun ChangeProfilesDataScreen(uid: String, navController: NavController) {

    val scope = rememberCoroutineScope()
    val profileViewModel = hiltViewModel<ProfileViewModel>()

    val userData = profileViewModel.profileData

    val username = remember { mutableStateOf(TextFieldValue()) }
    val firstName = remember { mutableStateOf(TextFieldValue()) }
    val lastName = remember { mutableStateOf(TextFieldValue()) }

    LaunchedEffect(key1 = uid) {
        scope.launch {
            profileViewModel.loadProfile(uid)
        }
    }

    Column(Modifier
        .fillMaxSize()
        .padding(5.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween) {

        Row() {

            var _imageStr = userData.imageStr ?: ""
            if(_imageStr == "") _imageStr = getGoogleLogo()
            val imageStrDecoded = Base64.decode(_imageStr, Base64.DEFAULT)
            val image = BitmapFactory.decodeByteArray(imageStrDecoded, 0, imageStrDecoded.size)

            val rowModifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp)

            Column(Modifier
                .fillMaxWidth()
                .padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = rowModifier) {
                    Image(bitmap = image.asImageBitmap(), contentDescription = null,
                        modifier = Modifier
                            .size(100.dp)
                            .border(1.dp, Color.Gray, CircleShape)
                    )
                }
                Row(modifier = rowModifier) {
                    TextField(value = username.value, onValueChange = { username.value = it}, label = {Text(text = userData.username ?: "username")})
                }
                Row(modifier = rowModifier) {
                    TextField(value = firstName.value, onValueChange = {  firstName.value = it}, label = {Text(text = userData.firstName ?: "first name")})
                }
                Row(modifier = rowModifier) {
                    TextField(value = lastName.value, onValueChange = { lastName.value = it }, label = {Text(text = userData.lastName ?: "last name")})
                }
            }
        }

        Row(modifier = Modifier
            .fillMaxWidth()
            .padding(4.dp),
            horizontalArrangement = Arrangement.SpaceBetween) {
            Button(onClick = {
                scope.launch {
                    navController.popBackStack()
                }
            }, content = { Text(text = "Cancel") },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Purple40,
                    contentColor = Color.White))

            Button(onClick = {
                scope.launch {
                    profileViewModel.saveProfileData(userProfileData = UserProfileData(
                        uid, firstName.value.text, lastName.value.text, username.value.text))

                    navController.popBackStack()
                }
            }, content = { Text(text = "Save") },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Purple40,
                    contentColor = Color.White))
        }
    }
}
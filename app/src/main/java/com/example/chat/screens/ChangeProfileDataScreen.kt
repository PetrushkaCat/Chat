package com.example.chat.screens

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.app.Activity
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chat.viewmodels.ProfileViewModel
import com.example.chat.convert
import com.example.chat.getBitmap
import com.example.chat.ui.theme.Purple40
import com.example.domain.model.UserProfileData
import kotlinx.coroutines.launch

@Composable
fun ChangeProfilesDataScreen(uid: String, navController: NavController) {

    val scope = rememberCoroutineScope()
    val profileViewModel = hiltViewModel<ProfileViewModel>()
    val context = LocalContext.current

    val userData by profileViewModel.profileData

    val username = rememberSaveable { mutableStateOf("") }
    val firstName = rememberSaveable { mutableStateOf("") }
    val lastName = rememberSaveable { mutableStateOf("") }

    var imageUri by rememberSaveable { mutableStateOf<Uri?>(null) }
    //cant just rememberSaveable bitmap... it's too large and crushes app at onStop()
    val bitmap = remember { mutableStateOf<Bitmap?>(null) }

    val launcher = rememberLauncherForActivityResult(contract =
    ActivityResultContracts.GetContent()) { uri: Uri? ->
        imageUri = uri
    }

    var dataLoaded by remember { mutableStateOf(0) }


    LaunchedEffect(true) {
        scope.launch {
            profileViewModel.loadProfile(uid)
        }
    }

    //somehow without "if" it makes loop...
    if (dataLoaded < 2) {
        bitmap.value = getBitmap(userData.imageStr)
        if (userData.uid != null) {
            Log.d("image", "/${userData.uid}/")
            dataLoaded++
        }
    }


    Column(Modifier
        .fillMaxSize()
        .padding(5.dp, 0.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween) {

        Row() {
            val rowModifier = Modifier.padding(0.dp, 4.dp, 0.dp, 0.dp)

            Column(Modifier
                .fillMaxWidth()
                .padding(4.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Row(modifier = rowModifier) {
                    Image(bitmap = bitmap.value!!.asImageBitmap(),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(100.dp)
                            .border(2.dp, Color.Gray, CircleShape)
                            .clickable {
                                if (ContextCompat.checkSelfPermission(context, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                                    requestPermissions(context as Activity, arrayOf(READ_EXTERNAL_STORAGE), 10);
                                }
                                else {
                                    launcher.launch("image/*")
                                }
                            }
                            .clip(CircleShape)
                    )
                }
                Row(modifier = rowModifier) {
                    TextField(value = username.value,
                        onValueChange = { username.value = it },
                        label = { Text(text = userData.username ?: "username") },
                        singleLine = true)
                }
                Row(modifier = rowModifier) {
                    TextField(value = firstName.value,
                        onValueChange = { firstName.value = it },
                        label = { Text(text = userData.firstName ?: "first name") },
                        singleLine = true)
                }
                Row(modifier = rowModifier) {
                    TextField(value = lastName.value,
                        onValueChange = { lastName.value = it },
                        label = { Text(text = userData.lastName ?: "last name") },
                        singleLine = true)
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
                        uid,
                        firstName.value,
                        lastName.value,
                        username.value,
                        convert(bitmap.value!!)))
                    navController.popBackStack()
                }
            }, content = { Text(text = "Save") },
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(backgroundColor = Purple40,
                    contentColor = Color.White))
        }
    }

    //it makes loop too, but because of this loop, we can remember the picture user chose
    imageUri?.let {
        if (Build.VERSION.SDK_INT < 28) {
            bitmap.value = MediaStore.Images
                .Media.getBitmap(context.contentResolver, it)

        } else {
            val source = ImageDecoder
                .createSource(context.contentResolver, it)
            bitmap.value = ImageDecoder.decodeBitmap(source)
        }
        Log.d("image", "image changed")
    }
}

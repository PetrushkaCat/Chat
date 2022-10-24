package com.example.chat.screens

import android.graphics.BitmapFactory
import android.util.Base64
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chat.ChatViewModel
import com.example.chat.LoginViewModel
import com.example.chat.ProfileViewModel
import com.example.chat.getBitmap
import com.example.chat.navigations.MainScreens
import com.example.chat.navigations.Screen
import com.example.chat.ui.theme.Purple40
import com.example.chat.ui.theme.PurpleGrey40
import com.example.data.repository.RepositoryImpl
import com.example.domain.usecases.GetMessagesUseCase
import com.example.domain.usecases.SendMessageUseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController) {

    val chatViewModel = hiltViewModel<ChatViewModel>()

    val messages = chatViewModel.messages.toList().sortedBy { it.first }
    val lazyListState: LazyListState = rememberLazyListState()

    var showIcon = false;

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(2.dp)) {

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
                .background(Color.White),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            state = lazyListState
        ) {
            items(
                count = messages.size
            ) { item ->
                Box {
                    showIcon = !(item > 0 && messages[item].second.uid == messages[item - 1].second.uid)

                    Message(uid = messages[item].second.uid,
                        text = messages[item].second.message,
                        username = messages[item].second.username,
                        imageStr = messages[item].second.icon ?: "",
                        navController = navController,
                        showIcon = showIcon
                    )
                }
            }
        }


        val messageText = rememberSaveable { mutableStateOf("") }
        val sendMessageIcon = rememberVectorPainter(image = Icons.Default.Send)

        val scope = rememberCoroutineScope()
        val focusManager = LocalFocusManager.current


        TextField(
            value = messageText.value,
            onValueChange = { messageText.value = it },
            placeholder = { Text("Enter your message") },
            shape = RoundedCornerShape(20.dp),
            trailingIcon = {
                Icon(painter = sendMessageIcon,
                    contentDescription = null,
                    modifier = Modifier.clickable {
                        val text = messageText.value
                        scope.launch {
                            chatViewModel.sendMessage(text)
                        }
                        focusManager.clearFocus()
                        messageText.value = ""
                    }
                )
            },
            modifier = Modifier
                .border(width = 1.dp, color = Color.DarkGray, shape = RoundedCornerShape(20.dp))
                .fillMaxWidth()
                .padding(2.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            )

        )
    }
}

@Composable
fun Message(
    uid: String,
    text: String,
    username: String,
    imageStr: String,
    navController: NavController,
    showIcon: Boolean
) {

    var bubbleColor = Color(0xFFF3EEEE)

    val image = getBitmap(imageStr)

    if (uid != LoginViewModel.uid) {
        Box {
            Row {

                if (showIcon) {
                    Image(bitmap = image.asImageBitmap(),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(start = 2.dp, top = 2.dp, bottom = 2.dp)
                            .align(Alignment.Top)
                            .clickable {
                                navController.navigate(MainScreens.ProfileContent.withUid(uid))
                            }
                            .clip(CircleShape))
                } else {
                    Box(modifier = Modifier.size(50.dp))
                }

                MessageBubble(bubbleColor = bubbleColor,
                    username = username,
                    text = text)

            }
        }
    } else {
        Box() {
            Row(horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()) {

                bubbleColor = Color(0xFFE7F0DB)

                MessageBubble(bubbleColor = bubbleColor,
                    username = username,
                    text = text)

                if (showIcon) {
                    Image(bitmap = image.asImageBitmap(),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(start = 2.dp, top = 2.dp, bottom = 2.dp)
                            .align(Alignment.Top)
                            .clickable {
                                navController.navigate(MainScreens.ProfileContent.withUid(uid))
                            }
                            .clip(CircleShape))
                } else {
                    Box(modifier = Modifier.size(50.dp))
                }

            }

        }
    }
}

@Composable
fun MessageBubble(bubbleColor: Color, username: String, text: String) {
    Column(modifier = Modifier
        .background(color = bubbleColor,
            shape = RoundedCornerShape(6.dp))
        .padding(6.dp)
        .defaultMinSize(100.dp)) {

        Text(text = username, style = TextStyle(color = Purple40, fontSize = 15.sp))

        Text(
            text = text,
            style = TextStyle(fontFamily = FontFamily.Default, fontSize = 20.sp),
        )
    }
}

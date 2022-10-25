package com.example.chat.screens.main.chat

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.chat.screens.login.LoginViewModel
import com.example.chat.getBitmap
import com.example.chat.navigations.MainScreens
import com.example.chat.ui.theme.Purple40
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController) {

    val chatViewModel = hiltViewModel<ChatViewModel>()

    val messages = chatViewModel.messages.toList().sortedBy { it.first }
    val lazyListState: LazyListState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    if(messages.isNotEmpty()) {
        LaunchedEffect(true) {
            scope.launch {
                lazyListState.animateScrollToItem(messages.size - 1)
            }
        }
    }

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
                    val showIcon = !(item > 0 && messages[item].second.uid == messages[item - 1].second.uid)

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
                .border(width = 1.dp, color = Color.DarkGray,
                    shape = RoundedCornerShape(20.dp))
                .fillMaxWidth()
                .padding(4.dp, 2.dp),
            colors = TextFieldDefaults.textFieldColors(
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                containerColor = Color.White
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
            Row(modifier = Modifier.fillMaxWidth()) {
                if (showIcon) {
                    Image(bitmap = image.asImageBitmap(),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp)
                            .align(Alignment.Top)
                            .clickable {
                                navController.navigate(MainScreens.ProfileContent.withUid(uid))
                            }
                            .clip(CircleShape))
                } else {
                    Box(modifier = Modifier.size(40.dp, 0.dp))
                }

                MessageBubble(bubbleColor = bubbleColor,
                    username = username,
                    text = text,
                    showName = showIcon,
                    onLeft = true)
            }
        }
    } else {
        Box() {
            Row(horizontalArrangement = Arrangement.End,
                modifier = Modifier.fillMaxWidth()) {

                bubbleColor = Color(0xFFDBEFF0)

                MessageBubble(bubbleColor = bubbleColor,
                    username = username,
                    text = text,
                    showName = showIcon,
                    onLeft = false)

                if (showIcon) {
                    Image(bitmap = image.asImageBitmap(),
                        contentDescription = "avatar",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(40.dp)
                            .padding(4.dp)
                            .align(Alignment.Top)
                            .clickable {
                                navController.navigate(MainScreens.ProfileContent.withUid(uid))
                            }
                            .clip(CircleShape))
                } else {
                    Box(modifier = Modifier.size(40.dp, 0.dp))
                }

            }

        }
    }
}

@Composable
fun MessageBubble(bubbleColor: Color, username: String,
                  text: String, showName: Boolean, onLeft: Boolean) {

    val columnModifier: Modifier = if(onLeft) {
        Modifier
            .background(color = bubbleColor, shape = RoundedCornerShape(0.dp, 10.dp, 10.dp, 10.dp))
            .defaultMinSize(100.dp)
            .padding(6.dp)
    } else {
        Modifier
            .background(color = bubbleColor, shape = RoundedCornerShape(10.dp, 0.dp, 10.dp, 10.dp))
            .defaultMinSize(100.dp)
            .padding(6.dp)
    }

    Column(modifier = columnModifier
    ) {

        if(showName) {
            Text(
                text = username,
                style = TextStyle(color = Purple40, fontSize = 15.sp)
            )
        }

        Text(
            text = text,
            style = TextStyle(fontSize = 16.sp),
        )
    }
}

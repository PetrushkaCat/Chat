package com.example.chat

import android.content.ContentValues.TAG
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Message
import com.example.domain.usecases.GetMessagesUseCase
import com.example.domain.usecases.SendMessageUseCase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val getMessagesUseCase: GetMessagesUseCase,
    private val sendMessagesUseCase: SendMessageUseCase
): ViewModel() {

    var messages by mutableStateOf(mapOf<String, Message>())

    suspend fun sendMessage(message: String) {
        val uid = LoginViewModel.uid ?: ""
        val image = ""
        val username = "username"
        val messageInstance = Message(uid, message, image, username)

        viewModelScope.launch {
            sendMessagesUseCase.execute(messageInstance)
        }
    }

    init {
        // Read from the database
        val myRef = Firebase.database.getReference("chat")

        myRef.orderByKey().addValueEventListener(object: ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(TAG, "value changed")
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                messages = snapshot.getValue<Map<String, Message>>() ?: emptyMap()
                Log.d(TAG, "Value is: " + messages)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }

        })
    }
}
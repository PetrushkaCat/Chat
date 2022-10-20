package com.example.data.firebase.database.chat

import com.example.domain.model.Message
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FirebaseChatDB {

    suspend fun sendMessage(message: Message) {
        val database = Firebase.database
        val myRef = database.getReference("chat")

        myRef.push().setValue(message)
    }
}
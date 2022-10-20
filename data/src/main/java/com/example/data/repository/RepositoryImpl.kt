package com.example.data.repository

import com.example.data.firebase.authentication.FirebaseAuthentication
import com.example.data.firebase.database.chat.FirebaseChatDB
import com.example.domain.model.Message
import com.example.domain.model.UserProfileData
import com.example.domain.model.UserProfileStyles
import com.example.domain.repository.Repository

class RepositoryImpl(
    private val firebaseAuthentication: FirebaseAuthentication,
    private val firebaseChatDB: FirebaseChatDB
    ): Repository {

    override suspend fun login(email: String, password: String): String? {
        return firebaseAuthentication.login(email, password)
    }

    override suspend fun signUp(email: String, password: String): Boolean {
        return firebaseAuthentication.signUp(email, password)
    }

    override suspend fun getProfileData(uid: String): UserProfileData {
        TODO("Not yet implemented")
    }

    override suspend fun saveProfileData(userProfileData: UserProfileData) {
        TODO("Not yet implemented")
    }

    override suspend fun getProfileStyles(uid: String): UserProfileStyles {
        TODO("Not yet implemented")
    }

    override suspend fun saveProfileStyles(userProfileStyles: UserProfileStyles) {
        TODO("Not yet implemented")
    }

    override suspend fun sendMessage(message: Message) {
        firebaseChatDB.sendMessage(message)
    }

    override suspend fun getMessages(): List<Message> {
        TODO("Not yet implemented")
    }
}
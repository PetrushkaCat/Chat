package com.example.domain.repository

import com.example.domain.model.Message
import com.example.domain.model.UserProfileData
import com.example.domain.model.UserProfileStyles

interface Repository {
    suspend fun login(email: String, password: String): String?
    suspend fun signUp(email: String, password: String): Boolean

    suspend fun getProfileData(uid: String): UserProfileData?
    suspend fun saveProfileData(userProfileData: UserProfileData)

    suspend fun getProfileStyles(uid: String): UserProfileStyles?
    suspend fun saveProfileStyles(userProfileStyles: UserProfileStyles)

    suspend fun sendMessage(message: Message)
    suspend fun getMessages(): List<Message>
}
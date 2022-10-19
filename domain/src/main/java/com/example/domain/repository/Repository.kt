package com.example.domain.repository

import com.example.domain.model.UserProfileData
import com.example.domain.model.UserProfileStyles

interface Repository {
    suspend fun login(email: String, password: String): String?
    suspend fun signUp(email: String, password: String): Boolean

    fun getProfileData(uid: String): UserProfileData
    fun saveProfileData(userProfileData: UserProfileData)

    fun getProfileStyles(uid: String): UserProfileStyles
    fun saveProfileStyles(userProfileStyles: UserProfileStyles)
}
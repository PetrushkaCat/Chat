package com.example.data.repository

import com.example.data.firebase.authentication.FirebaseAuthentication
import com.example.domain.model.UserProfileData
import com.example.domain.model.UserProfileStyles
import com.example.domain.repository.Repository

class RepositoryImpl(private val firebaseAuthentication: FirebaseAuthentication): Repository {

    override suspend fun login(email: String, password: String): String? {
        return firebaseAuthentication.login(email, password)
    }

    override suspend fun signUp(email: String, password: String): Boolean {
        return firebaseAuthentication.signUp(email, password)
    }

    override fun getProfileData(uid: String): UserProfileData {
        TODO("Not yet implemented")
    }

    override fun saveProfileData(userProfileData: UserProfileData) {
        TODO("Not yet implemented")
    }

    override fun getProfileStyles(uid: String): UserProfileStyles {
        TODO("Not yet implemented")
    }

    override fun saveProfileStyles(userProfileStyles: UserProfileStyles) {
        TODO("Not yet implemented")
    }
}
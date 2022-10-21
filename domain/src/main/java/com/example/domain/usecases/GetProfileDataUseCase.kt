package com.example.domain.usecases

import com.example.domain.model.UserProfileData
import com.example.domain.repository.Repository

class GetProfileDataUseCase(private val repository: Repository) {


    suspend fun execute(uid: String): UserProfileData? {
        return repository.getProfileData(uid)
    }

}
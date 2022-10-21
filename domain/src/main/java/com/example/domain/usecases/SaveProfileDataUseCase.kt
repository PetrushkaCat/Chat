package com.example.domain.usecases

import com.example.domain.model.UserProfileData
import com.example.domain.repository.Repository

class SaveProfileDataUseCase(private val repository: Repository) {

    suspend fun execute(userProfileData: UserProfileData) {
        repository.saveProfileData(userProfileData)
    }
}
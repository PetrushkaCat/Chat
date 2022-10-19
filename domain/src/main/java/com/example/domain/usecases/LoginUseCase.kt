package com.example.domain.usecases

import com.example.domain.repository.Repository

class LoginUseCase(private val repository: Repository) {

    suspend fun execute(email: String, password: String): String? {
        if(email.trim() == "" || password.trim() == "") return null

        return repository.login(email.trim(), password.trim())
    }
}
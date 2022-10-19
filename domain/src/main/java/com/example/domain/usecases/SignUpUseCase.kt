package com.example.domain.usecases

import com.example.domain.repository.Repository

class SignUpUseCase(private val repository: Repository) {

    suspend fun execute(email: String, password: String, password2: String): Boolean {
        if(password != password2) return false
        if(email.trim() == "" || password.trim() == "" ) return false

        return repository.signUp(email, password)
    }
}
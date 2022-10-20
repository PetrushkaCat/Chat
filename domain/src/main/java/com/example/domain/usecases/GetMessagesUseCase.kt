package com.example.domain.usecases

import com.example.domain.model.Message
import com.example.domain.repository.Repository

class GetMessagesUseCase(private val repository: Repository) {

    suspend fun execute(): List<Message> {
        return emptyList()
    }
}
package com.example.domain.usecases

import com.example.domain.model.Message
import com.example.domain.repository.Repository

class SendMessageUseCase(private val repository: Repository) {

    suspend fun execute(message: Message) {
        repository.sendMessage(message)
    }
}
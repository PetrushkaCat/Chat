package com.example.chat.di

import com.example.domain.repository.Repository
import com.example.domain.usecases.GetMessagesUseCase
import com.example.domain.usecases.LoginUseCase
import com.example.domain.usecases.SendMessageUseCase
import com.example.domain.usecases.SignUpUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
class DomainModule {

    @Provides
    fun provideLoginUseCase(repository: Repository): LoginUseCase {
        return LoginUseCase(repository)
    }

    @Provides
    fun provideSignUpUseCase(repository: Repository): SignUpUseCase {
        return SignUpUseCase(repository)
    }

    @Provides
    fun provideSendMessageUseCase(repository: Repository): SendMessageUseCase {
        return SendMessageUseCase(repository)
    }

    @Provides
    fun provideGetMessagesUseCase(repository: Repository): GetMessagesUseCase {
        return GetMessagesUseCase(repository)
    }
}
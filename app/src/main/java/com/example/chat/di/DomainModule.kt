package com.example.chat.di

import com.example.domain.repository.Repository
import com.example.domain.usecases.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
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

    @Provides
    fun provideSaveProfileDataUseCase(repository: Repository): SaveProfileDataUseCase {
        return SaveProfileDataUseCase(repository)
    }

    @Provides
    fun provideGetProfileDataUseCase(repository: Repository): GetProfileDataUseCase {
        return GetProfileDataUseCase(repository)
    }
}
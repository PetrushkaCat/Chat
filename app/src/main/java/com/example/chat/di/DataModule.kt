package com.example.chat.di

import com.example.data.firebase.authentication.FirebaseAuthentication
import com.example.data.repository.RepositoryImpl
import com.example.domain.repository.Repository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideRepository(firebaseAuthentication: FirebaseAuthentication): Repository {
        return RepositoryImpl(firebaseAuthentication)
    }

    @Provides
    @Singleton
    fun provideFirebaseAuthentication(): FirebaseAuthentication {
        return FirebaseAuthentication()
    }
}
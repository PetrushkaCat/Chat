package com.example.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.usecases.LoginUseCase
import com.example.domain.usecases.SignUpUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private var loginUseCase: LoginUseCase,
    private var signUpUseCase: SignUpUseCase
    ): ViewModel() {

    private val _isLoginSuccessful = MutableLiveData<Boolean>()
    val isLoginSuccessful: LiveData<Boolean> = _isLoginSuccessful

    private val _isSignUpSuccessful = MutableLiveData<Boolean>()
    val isSignUpSuccessful: LiveData<Boolean> = _isSignUpSuccessful

    suspend fun login(username: String, password: String) {
        val task = viewModelScope.async {
            token = loginUseCase.execute(username, password)
        }
        task.await()
        _isLoginSuccessful.value = token != null
    }

    suspend fun signUp(email: String, password: String, password2: String) {
        var isSuccess = false
        val task = viewModelScope.async {
            isSuccess = signUpUseCase.execute(email, password, password2)
        }
        task.await()
        _isSignUpSuccessful.value = isSuccess
    }

    companion object {
        var token: String? = null
    }

}
package com.example.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.UserProfileData
import com.example.domain.usecases.LoginUseCase
import com.example.domain.usecases.SaveProfileDataUseCase
import com.example.domain.usecases.SignUpUseCase
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private var loginUseCase: LoginUseCase,
    private var signUpUseCase: SignUpUseCase,
    private var saveProfileDataUseCase: SaveProfileDataUseCase
    ): ViewModel() {

    private val _isLoginSuccessful = MutableLiveData<Boolean>()
    val isLoginSuccessful: LiveData<Boolean> = _isLoginSuccessful

    private val _isSignUpSuccessful = MutableLiveData<Boolean>()
    val isSignUpSuccessful: LiveData<Boolean> = _isSignUpSuccessful

    suspend fun login(username: String, password: String) {
        val task = viewModelScope.async {
            uid = loginUseCase.execute(username, password)
        }
        task.await()
        _isLoginSuccessful.value = uid != null
    }

    suspend fun signUp(email: String, password: String, password2: String) {
        var isSuccess = false
        val task = viewModelScope.async {
            isSuccess = signUpUseCase.execute(email, password, password2)
        }
        task.await()
        _isSignUpSuccessful.value = isSuccess
        if(isSuccess) {
            viewModelScope.launch {
                saveProfileDataUseCase.execute(UserProfileData(uid = Firebase.auth.currentUser?.uid))
            }
        }
    }

    companion object {
        var uid: String? = Firebase.auth.currentUser?.uid
    }

}
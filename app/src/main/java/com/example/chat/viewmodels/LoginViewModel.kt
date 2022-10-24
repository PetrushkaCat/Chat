package com.example.chat.viewmodels

import android.content.ContentValues.TAG
import android.util.Log
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
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private var loginUseCase: LoginUseCase,
    private var signUpUseCase: SignUpUseCase,
    private var saveProfileDataUseCase: SaveProfileDataUseCase,
) : ViewModel() {

    private val _isLoginSuccessful = MutableLiveData<Boolean>()
    val isLoginSuccessful: LiveData<Boolean> = _isLoginSuccessful

    private val _isSignUpSuccessful = MutableLiveData<Boolean>()
    val isSignUpSuccessful: LiveData<Boolean> = _isSignUpSuccessful

    private val _isEmailSent = MutableLiveData<Boolean>()
    val isEmailSent: LiveData<Boolean> = _isEmailSent

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
        if (isSuccess) {
            viewModelScope.launch {
                saveProfileDataUseCase.execute(UserProfileData(uid = Firebase.auth.currentUser?.uid))
            }
        }
    }

    suspend fun sendResetPasswordEmail(emailAddress: String) {
        if (emailAddress == "") return
        var isSuccess = false

        try {
            val task = Firebase.auth.sendPasswordResetEmail(emailAddress)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d(TAG, "Email sent.")
                        isSuccess = true
                    } else {
                        Log.d(TAG, "Email not sent.")
                        isSuccess = false
                    }
                }
            task.await()
            _isEmailSent.value = isSuccess
        } catch(e: Exception) {
            e.printStackTrace()
            _isEmailSent.value = false
        }
    }

    companion object {
        var uid: String? = Firebase.auth.currentUser?.uid
    }

}
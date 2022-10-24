package com.example.chat.viewmodels

import android.content.ContentValues
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.UserProfileData
import com.example.domain.model.UserProfileStyles
import com.example.domain.usecases.GetProfileDataUseCase
import com.example.domain.usecases.SaveProfileDataUseCase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val getProfileDataUseCase: GetProfileDataUseCase,
    private val saveProfileDataUseCase: SaveProfileDataUseCase,
    //getProfileStylesUseCase: GetProfileStylesUseCase,
    //saveProfileStylesUseCase: SaveProfileStylesUseCase

): ViewModel() {

    val profileData = mutableStateOf(UserProfileData())

    private val _profileStyles = MutableLiveData<UserProfileStyles>()
    val profileStyles: LiveData<UserProfileStyles> = _profileStyles

    suspend fun loadProfile(uid: String) {
        val database = Firebase.database
        val myRef = database.getReference("profile/$uid/data")

        myRef.orderByKey().addValueEventListener(object : ValueEventListener {

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d(ContentValues.TAG, "value changed")
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                profileData.value = snapshot.getValue<UserProfileData>() ?: UserProfileData()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
            }

        })

    }

    suspend fun saveProfileData(userProfileData: UserProfileData) {
        if (userProfileData.firstName == "") userProfileData.firstName = profileData.value.firstName
        if (userProfileData.lastName == "") userProfileData.lastName = profileData.value.lastName
        if (userProfileData.username == "") userProfileData.username = profileData.value.username

        viewModelScope.launch {
            saveProfileDataUseCase.execute(userProfileData)
        }
    }
}
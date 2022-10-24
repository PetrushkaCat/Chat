package com.example.data.firebase.database.profile

import android.content.ContentValues
import android.util.Log
import com.example.domain.model.UserProfileData
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async

class FirebaseProfilesDB {

    suspend fun saveProfileData(userProfileData: UserProfileData) {
        val uid: String = Firebase.auth.currentUser?.uid ?: return

        val database = Firebase.database
        val myRef = database.getReference("profile/$uid/data")

        myRef.setValue(userProfileData)
    }

    suspend fun getProfileData(uid: String): UserProfileData? {
        val database = Firebase.database
        val myRef = database.getReference("profile/$uid/data")

        var userProfileData: UserProfileData? = null

        val task = CoroutineScope(Dispatchers.IO).async {

            myRef.orderByKey().addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    Log.d(ContentValues.TAG, "value changed")
                    // This method is called once with the initial value and again
                    // whenever data at this location is updated.
                    userProfileData = snapshot.getValue<UserProfileData>() ?: UserProfileData()
                    Log.d(ContentValues.TAG, "Value is: " + userProfileData)
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.w(ContentValues.TAG, "Failed to read value.", error.toException())
                }

            })
        }
        task.await()
        return userProfileData
    }
}
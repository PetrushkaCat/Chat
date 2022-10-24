package com.example.data.firebase.authentication

import android.content.ContentValues.TAG
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

class FirebaseAuthentication {
    private lateinit var auth: FirebaseAuth

    suspend fun signUp(email: String, password: String): Boolean {
        auth = Firebase.auth

        var isSuccess: Boolean = false

        try {
            val task = auth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "createUserWithEmail:success")
                        val user = auth.currentUser
                        isSuccess = true
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        isSuccess = false
                    }
                }
            task.await()
        } catch(e: Exception) {
            e.printStackTrace()
        }
        return isSuccess
    }

    suspend fun login(email: String, password: String): String? {
        auth = Firebase.auth

        try {
            val task = auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener() { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                    }
                }
            task.await()
        } catch(e: Exception) {
            e.printStackTrace()
            return null
        }
        return auth.currentUser?.uid
    }
}


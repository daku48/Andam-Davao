package com.example.andamdavao.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

data class AdminProfile(
    val uid: String,
    val email: String,
    val displayName: String,
    val role: String,
    val active: Boolean
)

class AuthRepository {

    private val auth: FirebaseAuth? by lazy {
        try {
            FirebaseAuth.getInstance()
        } catch (e: Exception) {
            null
        }
    }

    private val firestore: FirebaseFirestore? by lazy {
        try {
            FirebaseFirestore.getInstance()
        } catch (e: Exception) {
            null
        }
    }

    val currentUser: FirebaseUser?
        get() = auth?.currentUser

    val isGuestSession: Boolean
        get() = auth?.currentUser == null

    suspend fun signInWithEmail(email: String, pass: String): Result<FirebaseUser?> {
        val authInstance = auth ?: return Result.failure(Exception("Firebase Auth not initialized."))
        return try {
            val authResult = authInstance.signInWithEmailAndPassword(email, pass).await()
            Result.success(authResult.user)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    fun signOut() {
        auth?.signOut()
    }

    suspend fun checkAdminRole(uid: String): AdminProfile? {
        val db = firestore ?: return null
        return try {
            val doc = db.collection("admins").document(uid).get().await()
            if (doc.exists() && doc.getBoolean("active") == true) {
                AdminProfile(
                    uid = uid,
                    email = doc.getString("email") ?: "",
                    displayName = doc.getString("displayName") ?: "",
                    role = doc.getString("role") ?: "admin",
                    active = true
                )
            } else null
        } catch (e: Exception) {
            null
        }
    }
}

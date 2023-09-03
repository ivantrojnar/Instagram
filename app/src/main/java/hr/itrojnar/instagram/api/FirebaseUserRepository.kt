package hr.itrojnar.instagram.api

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hr.itrojnar.instagram.model.User
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository : UserRepository {

    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun getCurrentUserDetail(): User? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid
        Log.d("DEBUG", "Firebase User Repo logged in via Google with UID: $uid")
        if (uid != null) {
            val userDocument = firestore.collection("users").document(uid).get().await()
            return userDocument.toObject(User::class.java)
        }
        return null
    }
}
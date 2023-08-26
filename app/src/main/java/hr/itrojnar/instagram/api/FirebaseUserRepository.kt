package hr.itrojnar.instagram.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hr.itrojnar.instagram.model.User
import kotlinx.coroutines.tasks.await

class FirebaseUserRepository : UserRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val currentUser = FirebaseAuth.getInstance().currentUser

    override suspend fun getCurrentUserDetail(): User? {
        val uid = currentUser?.uid
        if (uid != null) {
            val userDocument = firestore.collection("users").document(uid).get().await()
            return userDocument.toObject(User::class.java)
        }
        return null
    }
}
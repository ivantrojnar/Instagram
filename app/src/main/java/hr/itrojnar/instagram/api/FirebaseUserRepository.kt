package hr.itrojnar.instagram.api

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hr.itrojnar.instagram.model.User
import kotlinx.coroutines.tasks.await
import java.time.LocalDate

class FirebaseUserRepository : UserRepository {

    private val firestore = FirebaseFirestore.getInstance()

    override suspend fun getCurrentUserDetail(): User? {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid
        val currentDate = LocalDate.now().toString()

        if (uid != null) {
            val userDocumentRef = firestore.collection("users").document(uid)
            val userDocument = userDocumentRef.get().await()
            val user = userDocument.toObject(User::class.java)

            if (user != null && user.lastSignInDate != currentDate) {
                // Create a copy with updated fields
                val updatedUser = user.copy(
                    lastSignInDate = currentDate,
                    mbUsedToday = 0f,
                    numOfPicsUploadedToday = 0
                )

                // Update Firestore with the modified user details
                userDocumentRef.set(updatedUser).await()

                return updatedUser
            }
            return user
        }
        return null
    }

    override suspend fun updateUserConsumption(mbUsed: Float, numOfPics: Int) {
        val currentUser = FirebaseAuth.getInstance().currentUser
        val uid = currentUser?.uid

        if (uid != null) {
            val userDocumentRef = firestore.collection("users").document(uid)
            val userDocument = userDocumentRef.get().await()
            val user = userDocument.toObject(User::class.java)

            if (user != null) {
                val updatedUser = user.copy(
                    mbUsedToday = user.mbUsedToday + mbUsed,
                    numOfPicsUploadedToday = user.numOfPicsUploadedToday + numOfPics
                )

                userDocumentRef.set(updatedUser).await()
            }
        }
    }
}
package hr.itrojnar.instagram.sign_in

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import com.firebase.ui.auth.AuthUI
import com.firebase.ui.auth.IdpResponse
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GithubAuthProvider
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import java.util.UUID

class GithubAuthClient(
    private val context: Context
) {
    private val auth = FirebaseAuth.getInstance()
    private val storage = FirebaseStorage.getInstance()

    suspend fun signInWithIntent(intentData: IdpResponse): SignInResult {
        Log.w("CUSTOM TAG", "ENTER the function")
        val githubToken = intentData.idpToken ?: return SignInResult(
            data = null,
            errorMessage = "Github token is null."
        )
        Log.w("CUSTOM TAG", "YEEEEEEEEEEEEEEEEES")
        val githubCredentials = GithubAuthProvider.getCredential(githubToken)

        return try {
            val authResult = auth.signInWithCredential(githubCredentials).await()
            val user = authResult.user
            //val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false
            val existingUser = Firebase.firestore.collection("users").document(user?.uid ?: "").get().await()
            val isNewUser = !existingUser.exists()
            Log.w("CUSTOM TAG", isNewUser.toString())

            if (isNewUser) {
                var imageUrl: String? = user?.photoUrl?.toString()
                if (!imageUrl.isNullOrEmpty()) {
                    val localUri = downloadImageToUri(imageUrl)
                    imageUrl = localUri?.let { uri ->
                        uploadProfileImageToFirebase(uri)
                    }
                }

                createUserInFirestore(
                    firebaseUserId = user?.uid,
                    fullName = user?.displayName,
                    email = user?.email,
                    profilePictureUrl = user?.photoUrl?.toString() ?: ""
                )
            }

            Firebase.analytics.setUserId(user?.uid)
            Firebase.analytics.logEvent("login_with_github", null)

            SignInResult(
                data = user?.run {
                    UserData(
                        userId = uid,
                        userName = displayName,
                        profilePictureUrl = photoUrl?.toString()
                    )
                },
                errorMessage = null
            )
        } catch (e: Exception) {
            e.printStackTrace()
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    fun startGithubSignIn(): Intent {
        val providers = arrayListOf(
            AuthUI.IdpConfig.GitHubBuilder().build()
        )

        return AuthUI.getInstance()
            .createSignInIntentBuilder()
            .setAvailableProviders(providers)
            .build()
    }

    suspend fun downloadImageToUri(imageUrl: String): Uri? {
        return withContext(Dispatchers.IO) {
            try {
                val url = URL(imageUrl)
                val connection = url.openConnection() as HttpURLConnection
                connection.doInput = true
                connection.connect()
                val input = connection.inputStream

                val directory = context.cacheDir
                val outputFileName = "tempProfileImage"
                val file = File(directory, outputFileName)

                val fos = FileOutputStream(file)
                val buffer = ByteArray(1024)
                var length: Int
                while (input.read(buffer).also { length = it } > 0) {
                    fos.write(buffer, 0, length)
                }
                fos.close()
                input.close()

                return@withContext Uri.fromFile(file)
            } catch (e: Exception) {
                e.printStackTrace()
                return@withContext null
            }
        }
    }

    suspend fun uploadProfileImageToFirebase(imageUri: Uri): String? {
        val profileImagesRef: StorageReference = storage.reference.child("profile_images/${UUID.randomUUID()}.jpg")
        val uploadTask = profileImagesRef.putFile(imageUri)
        val task = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            profileImagesRef.downloadUrl
        }.await()
        return task.toString()
    }

    private fun createUserInFirestore(
        firebaseUserId: String?,
        fullName: String?,
        email: String?,
        profilePictureUrl: String
    ) {
        val today = LocalDate.now()
        val user = hashMapOf(
            "firebaseUserId" to firebaseUserId,
            "fullName" to fullName,
            "email" to email,
            "profilePictureUrl" to profilePictureUrl,
            "subscriptionId" to 1,
            "lastSignInDate" to today.toString(),
            "mbUsedToday" to 0,
            "numOfPicsUploadedToday" to 0
        )

        val db = Firebase.firestore
        firebaseUserId?.let {
            db.collection("users").document(it).set(user)
        }
    }
}

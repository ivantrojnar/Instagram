package hr.itrojnar.instagram.sign_in

import android.content.Context
import android.content.Intent
import android.content.IntentSender
import android.net.Uri
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.BeginSignInRequest.GoogleIdTokenRequestOptions
import com.google.android.gms.auth.api.identity.SignInClient
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import hr.itrojnar.instagram.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import java.net.HttpURLConnection
import java.net.URL
import java.time.LocalDate
import java.util.UUID
import java.util.concurrent.CancellationException

class GoogleAuthUiClient(
    private val context: Context,
    private val oneTapClient: SignInClient
) {
    private val auth = Firebase.auth
    private val storage = FirebaseStorage.getInstance()

    suspend fun signIn(): IntentSender? {
        val result = try {
            oneTapClient.beginSignIn(
                buildSignInRequest()
            ).await()
        } catch (e: Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
            null
        }
        return result?.pendingIntent?.intentSender
    }

    suspend fun signInWithIntent(intent: Intent ): SignInResult {
        val credential = oneTapClient.getSignInCredentialFromIntent(intent)
        val googleIdToken = credential.googleIdToken
        val googleCredentials = GoogleAuthProvider.getCredential(googleIdToken, null)
        return try {
            val authResult = auth.signInWithCredential(googleCredentials).await()
            val user = authResult.user
            val isNewUser = authResult.additionalUserInfo?.isNewUser ?: false

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
                    profilePictureUrl = imageUrl ?: ""
                )
            }

            Firebase.analytics.setUserId(user?.uid)
            Firebase.analytics.logEvent("login_with_google", null)

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
            if (e is CancellationException) throw e
            SignInResult(
                data = null,
                errorMessage = e.message
            )
        }
    }

    suspend fun signOut() {
        try {
            oneTapClient.signOut().await()
            auth.signOut()
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            if (e is CancellationException) throw e
        }
    }

    fun getSignedInUser(): UserData? = auth.currentUser?.run {
        UserData(
            userId = uid,
            userName = displayName,
            profilePictureUrl = photoUrl?.toString()
        )
    }

    private fun buildSignInRequest(): BeginSignInRequest {
        return BeginSignInRequest.Builder()
            .setGoogleIdTokenRequestOptions(
                GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setFilterByAuthorizedAccounts(false)
                    .setServerClientId(context.getString(R.string.web_client_id))
                    .build()
            )
            .setAutoSelectEnabled(true)
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

                val directory = context.cacheDir // Assuming 'context' is available here
                val outputFileName = "tempProfileImage" // You might want to make this unique
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

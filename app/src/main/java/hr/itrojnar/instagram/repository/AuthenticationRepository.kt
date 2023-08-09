package hr.itrojnar.instagram.repository

import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

object AuthenticationRepository {

    private val auth by lazy {
        Firebase.auth
    }

    fun logIn(
        email: String,
        password: String,
        onSuccess: () -> Unit,
        onFail: () -> Unit
    ) {
        //onSuccess()
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    onSuccess()
                } else {
                    onFail()
                    Log.e("LOGIN", "Wrong credentials")
                }
            }

    }
}
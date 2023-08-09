package hr.itrojnar.instagram.view.auth

import android.net.Uri

data class SignUpState(
    val imageUri: Uri? = null,
    val email: String = "",
    val password: String = "",
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
)

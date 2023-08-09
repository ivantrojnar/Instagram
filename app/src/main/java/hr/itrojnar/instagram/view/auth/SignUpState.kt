package hr.itrojnar.instagram.view.auth

import android.net.Uri

// TODO add subscription
data class SignUpState(
    val imageUri: Uri? = null,
    val fullName: String = "",
    val email: String = "",
    val password: String = "",
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
    val isFullNameValid: Boolean = false,
    val isImageSelected: Boolean = false
)

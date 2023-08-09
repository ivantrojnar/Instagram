package hr.itrojnar.instagram.sign_in

data class GoogleSignInState(
    val isSignInSuccessful: Boolean = false,
    val signInError: String? = null
)
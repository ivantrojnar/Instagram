package hr.itrojnar.instagram.view.auth

data class LogInState(
    val email: String = "",
    val password: String = "",
    val isEmailValid: Boolean = false,
    val isPasswordValid: Boolean = false,
)
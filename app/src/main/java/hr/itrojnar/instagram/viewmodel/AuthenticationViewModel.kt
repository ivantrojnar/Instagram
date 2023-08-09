package hr.itrojnar.instagram.viewmodel

import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hr.itrojnar.instagram.repository.AuthenticationRepository
import hr.itrojnar.instagram.util.isValidPassword
import hr.itrojnar.instagram.view.auth.LogInState

class AuthenticationViewModel : ViewModel() {

    private val _authenticationState = mutableStateOf(
        LogInState()
    )

    val authenticationState: State<LogInState>
        get() = _authenticationState

    fun onEmailChanged(email: String) {
        _authenticationState.value = _authenticationState.value.copy(
            email = email,
            isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        )
    }

    fun onPasswordChanged(password: String) {
        _authenticationState.value = _authenticationState.value.copy(
            password = password,
            isPasswordValid = password.isValidPassword()
        )
    }

    fun logIn(onSuccess: () -> Unit, onFail: () -> Unit) {
        AuthenticationRepository.logIn(
            _authenticationState.value.email,
            _authenticationState.value.password,
            onSuccess = onSuccess,
            onFail = onFail
        )
    }
}
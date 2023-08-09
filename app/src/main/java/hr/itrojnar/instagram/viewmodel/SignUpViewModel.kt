package hr.itrojnar.instagram.viewmodel

import android.net.Uri
import android.util.Patterns
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import hr.itrojnar.instagram.repository.AuthenticationRepository
import hr.itrojnar.instagram.util.isValidPassword
import hr.itrojnar.instagram.view.auth.SignUpState

class SignUpViewModel: ViewModel() {

    private val _signUpState = mutableStateOf(
        SignUpState()
    )

    val signUpstate: State<SignUpState>
        get() = _signUpState

    fun onEmailChanged(email: String) {
        _signUpState.value = _signUpState.value.copy(
            email = email,
            isEmailValid = Patterns.EMAIL_ADDRESS.matcher(email).matches()
        )
    }

    fun onPasswordChanged(password: String) {
        _signUpState.value = _signUpState.value.copy(
            password = password,
            isPasswordValid = password.isValidPassword()
        )
    }

    fun onFullNameChanged(fullName: String) {
        _signUpState.value = _signUpState.value.copy(
            fullName = fullName,
            isFullNameValid = fullName.isNotBlank()
        )
    }

    fun onImageUriChanged(uri: Uri) {
        _signUpState.value = _signUpState.value.copy(
            imageUri = uri,
            isImageSelected = true
        )
    }

    // TODO add more fields
    fun register(onSuccess: () -> Unit, onFail: () -> Unit) {
        AuthenticationRepository.register(
            _signUpState.value.email,
            _signUpState.value.password,
            onSuccess = onSuccess,
            onFail = onFail
        )
    }

    fun resetSignUpState() {
        _signUpState.value = SignUpState()  // Resets to default values
    }
}
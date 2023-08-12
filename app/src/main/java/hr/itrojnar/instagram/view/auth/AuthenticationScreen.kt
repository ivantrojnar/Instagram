package hr.itrojnar.instagram.view.auth

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.sign_in.GoogleSignInState

@Composable
fun AuthenticationScreen(
    googleSignInState: GoogleSignInState,
    onSignInClick: () -> Unit,
    logInState: LogInState,
    onLogin: () -> Unit,
    signUpState: SignUpState,
    onSignUp: () -> Unit,
    onGithubSignIn: () -> Unit,
    onRequestEmailForForgottenPassword: () -> Unit,
    onLoginEmailChanged: (String) -> Unit,
    onLoginPasswordChanged: (String) -> Unit,
    onSignUpEmailChanged: (String) -> Unit,
    onSignUpPasswordChanged: (String) -> Unit,
    onFullNameChanged: (String) -> Unit,
    onImageUriChanged: (Uri) -> Unit,
    resetSignUpState: () -> Unit,
    modifier: Modifier = Modifier) {

    var currentScreen by remember {
        mutableStateOf(AuthenticationScreenState.LogIn)
    }

    val context = LocalContext.current

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)) {
            Crossfade(targetState = currentScreen) { screen ->
                when (screen) {
                    AuthenticationScreenState.LogIn -> LogInScreen(
                        logInState = logInState,
                        onEmailChanged = onLoginEmailChanged,
                        onPasswordChanged = onLoginPasswordChanged,
                        googleSignInState = googleSignInState,
                        onSignInClick = onSignInClick,
                        onGithubSignIn = onGithubSignIn,
                        modifier = modifier,
                        onSignUpClick = { currentScreen = AuthenticationScreenState.SignUp },
                        onForgotPasswordClick = { currentScreen = AuthenticationScreenState.ForgotPassword },
                        onLogin = onLogin
                    )
                    AuthenticationScreenState.SignUp -> SignUpScreen(
                        modifier = modifier,
                        onSignUpEmailChanged = onSignUpEmailChanged,
                        onSignUpPasswordChanged = onSignUpPasswordChanged,
                        onFullNameChanged = onFullNameChanged,
                        onImageUriChanged = onImageUriChanged,
                        signUpState = signUpState,
                        onLogInClick = { currentScreen = AuthenticationScreenState.LogIn },
                        resetSignUpState = resetSignUpState,
                        onSignUp = onSignUp
                    )
                    AuthenticationScreenState.ForgotPassword -> ForgotPasswordScreen(
                        modifier = modifier,
                        onLogInClick = { currentScreen = AuthenticationScreenState.LogIn },
                        onRequestEmailForForgottenPassword = { email, onSuccess, onFailure ->
                            val auth = FirebaseAuth.getInstance()
                            auth.sendPasswordResetEmail(email)
                                .addOnCompleteListener { task ->
                                    if (task.isSuccessful) {
                                    } else {
                                        Toast.makeText(context, context.getString(R.string.unable_to_send_email_to_reset_password), Toast.LENGTH_SHORT).show()
                                    }
                                }
                        }
                    )
                }
            }
        }
    }
}

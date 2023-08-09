package hr.itrojnar.instagram.view.auth

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
import hr.itrojnar.instagram.sign_in.GoogleSignInState

@Composable
fun AuthenticationScreen(
    googleSignInState: GoogleSignInState,
    onSignInClick: () -> Unit,
    logInState: LogInState,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onRequestEmailForForgottenPassword: () -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    modifier: Modifier = Modifier) {

    var currentScreen by remember {
        mutableStateOf(AuthenticationScreenState.LogIn)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)) {
            Crossfade(targetState = currentScreen) { screen ->
                when (screen) {
                    AuthenticationScreenState.LogIn -> LogInScreen(
                        logInState = logInState,
                        onEmailChanged = onEmailChanged,
                        onPasswordChanged = onPasswordChanged,
                        googleSignInState = googleSignInState,
                        onSignInClick = onSignInClick,
                        modifier = modifier,
                        onSignUpClick = { currentScreen = AuthenticationScreenState.SignUp },
                        onForgotPasswordClick = { currentScreen = AuthenticationScreenState.ForgotPassword },
                        onLogin = onLogin
                    )
                    AuthenticationScreenState.SignUp -> SignUpScreen(
                        modifier = modifier,
                        onLogInClick = { currentScreen = AuthenticationScreenState.LogIn },
                        onRegister = onRegister
                    )
                    AuthenticationScreenState.ForgotPassword -> ForgotPasswordScreen(
                        modifier = modifier,
                        onLogInClick = { currentScreen = AuthenticationScreenState.LogIn },
                        onRequestEmailForForgottenPassword = onRequestEmailForForgottenPassword
                    )
                }
            }
        }
    }
}

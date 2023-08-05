package hr.itrojnar.instagram.nav

import android.content.Context
import android.widget.Toast
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import hr.itrojnar.instagram.view.AuthScreen
import hr.itrojnar.instagram.view.auth.AuthenticationScreen
import hr.itrojnar.instagram.viewmodel.AuthenticationViewModel

fun NavGraphBuilder.authNavGraph(
    navHostController: NavHostController,
    authenticationViewModel: AuthenticationViewModel,
    context: Context
) {
    navigation(
        route = Graph.AUTH,
        startDestination = AuthScreen.Login.route
    ) {

        composable(route = AuthScreen.Login.route) {
            AuthenticationScreen(
                authenticationState = authenticationViewModel.authenticationState.value,
                onLogin = { Toast.makeText(context, "Log in click", Toast.LENGTH_SHORT).show() },
                onRegister = { Toast.makeText(context, "Sign up click", Toast.LENGTH_SHORT).show() },
                onRequestEmailForForgottenPassword = { Toast.makeText(context, "Request email", Toast.LENGTH_SHORT).show() },
                onEmailChanged = { authenticationViewModel.onEmailChanged(it) },
                onPasswordChanged = { authenticationViewModel.onPasswordChanged(it) })
        }
    }
}
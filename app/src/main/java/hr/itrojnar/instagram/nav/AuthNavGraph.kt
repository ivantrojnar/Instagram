package hr.itrojnar.instagram.nav

import android.app.Activity.RESULT_OK
import android.content.Context
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.google.android.gms.auth.api.identity.Identity
import hr.itrojnar.instagram.sign_in.GoogleAuthUiClient
import hr.itrojnar.instagram.view.AuthScreen
import hr.itrojnar.instagram.view.auth.AuthenticationScreen
import hr.itrojnar.instagram.viewmodel.AuthenticationViewModel
import hr.itrojnar.instagram.viewmodel.SignInViewModel
import kotlinx.coroutines.launch

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

            val viewModel = viewModel<SignInViewModel>()
            val state by viewModel.state.collectAsStateWithLifecycle()
            val coroutineScope = rememberCoroutineScope()
            val googleAuthUiClient by lazy {
                GoogleAuthUiClient(
                    context = context,
                    oneTapClient = Identity.getSignInClient(context)
                )
            }
            
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        coroutineScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(
                                intent = result.data ?: return@launch
                            )
                            viewModel.onSignInResult(signInResult)
                        }
                    }
                }
            )

            AuthenticationScreen(
                state = state,
                onSignInClick = {
                    coroutineScope.launch {
                        val signInIntentSender = googleAuthUiClient.signIn()
                        launcher.launch(
                            IntentSenderRequest.Builder(
                                signInIntentSender ?: return@launch
                            ).build()
                        )
                    }
                },
                logInState = authenticationViewModel.logInState.value,
                onLogin = { Toast.makeText(context, "Log in click", Toast.LENGTH_SHORT).show() },
                onRegister = { Toast.makeText(context, "Sign up click", Toast.LENGTH_SHORT).show() },
                onRequestEmailForForgottenPassword = { Toast.makeText(context, "Request email", Toast.LENGTH_SHORT).show() },
                onEmailChanged = { authenticationViewModel.onEmailChanged(it) },
                onPasswordChanged = { authenticationViewModel.onPasswordChanged(it) })
        }
    }
}
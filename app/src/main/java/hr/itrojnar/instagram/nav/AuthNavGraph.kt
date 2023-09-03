package hr.itrojnar.instagram.nav

import android.app.Activity.RESULT_OK
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import com.firebase.ui.auth.IdpResponse
import com.google.android.gms.auth.api.identity.Identity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.sign_in.GithubAuthClient
import hr.itrojnar.instagram.sign_in.GoogleAuthUiClient
import hr.itrojnar.instagram.util.ShowSuccessDialog
import hr.itrojnar.instagram.view.AuthScreen
import hr.itrojnar.instagram.view.auth.AuthenticationScreen
import hr.itrojnar.instagram.viewmodel.AuthenticationViewModel
import hr.itrojnar.instagram.viewmodel.SignInViewModel
import hr.itrojnar.instagram.viewmodel.SignUpViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

fun NavGraphBuilder.authNavGraph(
    navHostController: NavHostController,
    authenticationViewModel: AuthenticationViewModel,
    signUpViewModel: SignUpViewModel,
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
            val githubAuthClient = GithubAuthClient(context = context)
            
            val launcher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartIntentSenderForResult(),
                onResult = { result ->
                    if (result.resultCode == RESULT_OK) {
                        coroutineScope.launch {
                            val signInResult = googleAuthUiClient.signInWithIntent(result.data ?: return@launch)
                            viewModel.onSignInResult(signInResult)
                            if (signInResult.data != null) {
                                val uid = signInResult.data.userId
                                Log.d("DEBUG", "Logged in via Google with UID: $uid")
                                navHostController.popBackStack()
                                navHostController.navigate(Graph.MAIN)
                            } else {
                                // Handle unsuccessful sign-in, show error or something similar
                            }
                        }
                    }
                }
            )

            val githubSignInLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.StartActivityForResult()
            ) { result ->
                if (result.resultCode == RESULT_OK) {
                    val response = IdpResponse.fromResultIntent(result.data)
                    if (response == null) {
                        return@rememberLauncherForActivityResult
                    }
                    if (response.error != null) {
                        return@rememberLauncherForActivityResult
                    }

                    coroutineScope.launch {
                        val signInResult = githubAuthClient.signInWithIntent(response)
                        if (signInResult.data != null) {
                            val uid = signInResult.data.userId
                            Log.d("DEBUG", "Logged in via GitHub with UID: $uid")
                            navHostController.popBackStack()
                            navHostController.navigate(Graph.MAIN)
                        } else {
                            // TODO Handle unsuccessful sign-in
                        }
                    }
                }
            }

            val showDialog = remember { mutableStateOf(false) }

            ShowSuccessDialog(showDialog = showDialog)

            AuthenticationScreen(
                googleSignInState = state,
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
                logInState = authenticationViewModel.authenticationState.value,
                onLogin = { authenticationViewModel.logIn(
                    onSuccess = {
                        navHostController.popBackStack()
                        navHostController.navigate(Graph.MAIN)
                    },
                    onFail = {
                        Toast.makeText(context, context.getString(R.string.unable_to_log_in), Toast.LENGTH_SHORT).show()
                    }
                ) },
                signUpState = signUpViewModel.signUpstate.value,
                onSignUp = {
                    showDialog.value = true
                    coroutineScope.launch {
                        delay(3500)
                        showDialog.value = false
                        navHostController.popBackStack()
                        navHostController.navigate(Graph.MAIN)
                    }
                },
                onGithubSignIn = {
                    val githubSignInIntent = githubAuthClient.startGithubSignIn()
                    githubSignInLauncher.launch(githubSignInIntent)
                },
                onRequestEmailForForgottenPassword = { Toast.makeText(context, context.getString(R.string.request_email), Toast.LENGTH_SHORT).show() },
                onLoginEmailChanged = { authenticationViewModel.onEmailChanged(it) },
                onLoginPasswordChanged = { authenticationViewModel.onPasswordChanged(it) },
                onSignUpEmailChanged = { signUpViewModel.onEmailChanged(it) },
                onSignUpPasswordChanged = { signUpViewModel.onPasswordChanged(it) },
                onFullNameChanged = { signUpViewModel.onFullNameChanged(it) },
                onImageUriChanged = { signUpViewModel.onImageUriChanged(it)},
                resetSignUpState = { signUpViewModel.resetSignUpState()})
        }
    }
}
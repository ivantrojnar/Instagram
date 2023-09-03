package hr.itrojnar.instagram.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import hr.itrojnar.instagram.view.MainScreen
import hr.itrojnar.instagram.viewmodel.AuthenticationViewModel
import hr.itrojnar.instagram.viewmodel.SignUpViewModel

@Composable
fun RootNavGraph(navController: NavHostController) {
    val authenticationViewModel = viewModel<AuthenticationViewModel>()
    val signUpViewModel = viewModel<SignUpViewModel>()
    val context = LocalContext.current
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTH) {
        authNavGraph(navHostController = navController, authenticationViewModel, signUpViewModel, context)
        composable(route = Graph.MAIN) {
            MainScreen(navHostController = navController)
        }
    }
}
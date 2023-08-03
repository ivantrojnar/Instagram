package hr.itrojnar.instagram.nav

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import hr.itrojnar.instagram.viewmodel.AuthenticationViewModel

@Composable
fun RootNavGraph(navController: NavHostController) {
    val authenticationViewModel = viewModel<AuthenticationViewModel>()
    val context = LocalContext.current
    NavHost(
        navController = navController,
        route = Graph.ROOT,
        startDestination = Graph.AUTH) {
    }
}
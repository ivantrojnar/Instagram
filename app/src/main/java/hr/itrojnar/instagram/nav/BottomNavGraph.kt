package hr.itrojnar.instagram.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import hr.itrojnar.instagram.view.BottomNavScreen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavScreen.Home.route
    ) {
        composable(route = BottomNavScreen.Home.route) {
        }
        composable(route = BottomNavScreen.Map.route) {
        }
        composable(route = BottomNavScreen.Profile.route) {
        }
    }
}
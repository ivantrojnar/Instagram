package hr.itrojnar.instagram.nav

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import hr.itrojnar.instagram.view.BottomNavScreen
import hr.itrojnar.instagram.view.Screen

@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavScreen.Home.route
    ) {
        composable(route = BottomNavScreen.Home.route) {
        }
        composable(route = BottomNavScreen.Search.route) {
        }
        composable(route = BottomNavScreen.Map.route) {
        }
        composable(route = BottomNavScreen.Profile.route) {
        }
        composable(route = BottomNavScreen.Settings.route) {
        }
        composable(route = Screen.Camera) {
            // Camera Screen Composable Here
        }
    }
}
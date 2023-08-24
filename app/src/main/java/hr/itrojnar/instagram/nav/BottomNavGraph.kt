package hr.itrojnar.instagram.nav

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.paging.ExperimentalPagingApi
import hr.itrojnar.instagram.view.BottomNavScreen
import hr.itrojnar.instagram.view.CameraScreen
import hr.itrojnar.instagram.view.Screen
import hr.itrojnar.instagram.view.main.HomeScreen
import hr.itrojnar.instagram.view.main.PostsState
import hr.itrojnar.instagram.view.main.SearchScreen
import hr.itrojnar.instagram.viewmodel.PostsViewModel
import hr.itrojnar.instagram.viewmodel.SearchPostsViewModel
import kotlinx.coroutines.flow.toList

@ExperimentalPagingApi
@Composable
fun BottomNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomNavScreen.Home.route
    ) {
        composable(route = BottomNavScreen.Home.route) {
            val postsViewModel = hiltViewModel<PostsViewModel>()
            val postsState = PostsState(postsViewModel)
            HomeScreen(postsState = postsState)
        }
        composable(route = BottomNavScreen.Search.route) {
            val searchPostsViewModel = hiltViewModel<SearchPostsViewModel>()
            SearchScreen(searchPostsViewModel = searchPostsViewModel)
        }
        composable(route = BottomNavScreen.Map.route) {
        }
        composable(route = BottomNavScreen.Profile.route) {
        }
        composable(route = BottomNavScreen.Settings.route) {
        }
        composable(route = Screen.Camera) {
            CameraScreen(navController = navController)
        }
    }
}
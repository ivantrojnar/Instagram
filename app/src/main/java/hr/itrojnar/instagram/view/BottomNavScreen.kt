package hr.itrojnar.instagram.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector
import hr.itrojnar.instagram.R

sealed class BottomNavScreen(
    val route: String,
    val title: Int,
    val icon: ImageVector
) {
    object Home: BottomNavScreen(
        route = "home",
        title = R.string.home,
        icon = Icons.Default.Home
    )
    object Map: BottomNavScreen(
        route = "map",
        title = R.string.map,
        icon = Icons.Default.Map
    )
    object Profile: BottomNavScreen(
        route = "profile",
        title = R.string.profile,
        icon = Icons.Default.Person
    )
}
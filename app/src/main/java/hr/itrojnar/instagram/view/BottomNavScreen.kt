package hr.itrojnar.instagram.view

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Map
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.ui.graphics.vector.ImageVector
import hr.itrojnar.instagram.R

sealed class BottomNavScreen(
    val route: String,
    val title: Int,
    val icon: ImageVector,
    val selectedIcon: ImageVector
) {
    object Home: BottomNavScreen(
        route = "home",
        title = R.string.home,
        icon = Icons.Outlined.Home,
        selectedIcon = Icons.Filled.Home
    )
    object Search: BottomNavScreen(
        route = "search",
        title = R.string.search,
        icon = Icons.Outlined.Search,
        selectedIcon = Icons.Filled.Search
    )
    object Map: BottomNavScreen(
        route = "map",
        title = R.string.map,
        icon = Icons.Outlined.Map,
        selectedIcon = Icons.Filled.Map
    )
    object Profile: BottomNavScreen(
        route = "profile",
        title = R.string.profile,
        icon = Icons.Outlined.Person,
        selectedIcon = Icons.Filled.Person
    )
    object Settings: BottomNavScreen(
        route = "settings",
        title = R.string.settings,
        icon = Icons.Outlined.Settings,
        selectedIcon = Icons.Filled.Settings
    )

    companion object {
        val items = listOf(Home, Search, Map, Profile, Settings)
    }
}
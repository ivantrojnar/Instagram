package hr.itrojnar.instagram.view

sealed class DrawerScreen(val route: String, val title: String) {
    object Home : DrawerScreen("home", "Home")
    object Profile : DrawerScreen("profile", "Profile")
}

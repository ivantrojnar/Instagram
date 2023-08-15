package hr.itrojnar.instagram.view

sealed class DrawerScreen(val route: String, val title: String) {
    object Profile : DrawerScreen("profile", "Profile")
    object Settings : DrawerScreen("settings", "Settings")
}

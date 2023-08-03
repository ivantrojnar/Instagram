package hr.itrojnar.instagram.view

sealed class AuthScreen(val route: String) {
    object Login: AuthScreen(route = "login")
}
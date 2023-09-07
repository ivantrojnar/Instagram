package hr.itrojnar.instagram.app

// Dependency Inversion Principle (DIP): Introduced an abstraction for theme colors provider, so LogInScreen depends on an abstraction, not on concrete details.
interface ThemeColorsProvider {
    fun getThemeColors(darkTheme: Boolean): ThemeColors
}
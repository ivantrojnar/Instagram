package hr.itrojnar.instagram.app

import androidx.compose.ui.graphics.Color

class DarkThemeColorsProvider: ThemeColorsProvider {
    override fun getThemeColors(darkTheme: Boolean): ThemeColors {
        return if (darkTheme) {
            ThemeColors(
                backgroundColor = Color.Black,
                unfocusedLabelColor = Color.LightGray,
                unfocusedBorderColor = Color.LightGray,
                focusedLabelColor = Color.White,
                focusedBorderColor = Color.White,
                textColor = Color.White
            )
        } else {
            ThemeColors(
                backgroundColor = Color.Transparent,
                unfocusedLabelColor = Color.Gray,
                unfocusedBorderColor = Color.Gray,
                focusedLabelColor = Color.Black,
                focusedBorderColor = Color.Black,
                textColor = Color.Black
            )
        }
    }
}
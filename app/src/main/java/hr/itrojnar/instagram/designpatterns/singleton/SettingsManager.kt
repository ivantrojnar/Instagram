package hr.itrojnar.instagram.designpatterns.singleton

import androidx.compose.ui.graphics.Color

object SettingsManager {
    var themeColor: Color = Color.Blue // just a sample property

    fun printSetting() {
        println("Theme color is $themeColor")
    }
}
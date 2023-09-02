package hr.itrojnar.instagram.designpatterns.factory

object SettingFactory {
    fun getSetting(type: String): Setting {
        return when (type) {
            "DarkMode" -> DarkModeSetting()
            "Language" -> LanguageSetting()
            else -> throw IllegalArgumentException("Invalid type")
        }
    }
}
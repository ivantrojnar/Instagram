package hr.itrojnar.instagram.app

interface SettingItemContract {
    val name: String
    val enabled: Boolean
    fun getDescription(): String
}
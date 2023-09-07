package hr.itrojnar.instagram.app

data class BasicSetting(
    override val name: String,
    override val enabled: Boolean,
    val summary: String
) : SettingItemContract {

    override fun getDescription(): String {
        return summary
    }
}
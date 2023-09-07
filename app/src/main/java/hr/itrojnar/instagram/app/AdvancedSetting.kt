package hr.itrojnar.instagram.app

data class AdvancedSetting(
    override val name: String,
    override val enabled: Boolean,
    val summary: String,
    val icon: Int,
) : SettingItemContract {

    override fun getDescription(): String {
        return summary
    }
}

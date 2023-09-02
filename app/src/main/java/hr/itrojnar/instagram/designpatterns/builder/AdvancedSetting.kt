package hr.itrojnar.instagram.designpatterns.builder

class AdvancedSetting private constructor(
    val name: String?,
    val enabled: Boolean?,
    val summary: String?,
    val icon: Int
) {
    class Builder {
        private var name: String? = null
        private var enabled: Boolean? = null
        private var summary: String? = null
        private var icon: Int = 0

        fun setName(name: String) = apply { this.name = name }
        fun setEnabled(enabled: Boolean) = apply { this.enabled = enabled }
        fun setSummary(summary: String) = apply { this.summary = summary }
        fun setIcon(icon: Int) = apply { this.icon = icon }

        fun build() = AdvancedSetting(name, enabled, summary, icon)
    }
}
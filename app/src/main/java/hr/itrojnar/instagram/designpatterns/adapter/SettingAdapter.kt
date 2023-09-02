package hr.itrojnar.instagram.designpatterns.adapter

import hr.itrojnar.instagram.designpatterns.factory.Setting

class SettingAdapter(private val oldSetting: OldSetting) : Setting {
    override val name: String
        get() = oldSetting.title

    // Adapt the value to something meaningful for the new system
    val adaptedValue: Boolean
        get() = oldSetting.value == "Enabled"
}
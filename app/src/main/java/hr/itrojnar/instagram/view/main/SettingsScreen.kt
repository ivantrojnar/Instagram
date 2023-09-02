package hr.itrojnar.instagram.view.main

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.designpatterns.DesignPatternItem
import hr.itrojnar.instagram.designpatterns.adapter.OldSetting
import hr.itrojnar.instagram.designpatterns.adapter.SettingAdapter
import hr.itrojnar.instagram.designpatterns.builder.AdvancedSetting
import hr.itrojnar.instagram.designpatterns.factory.SettingFactory
import hr.itrojnar.instagram.designpatterns.observer.SettingsChangeNotifier
import hr.itrojnar.instagram.designpatterns.observer.SettingsObserver
import hr.itrojnar.instagram.designpatterns.singleton.SettingsManager
import hr.itrojnar.instagram.designpatterns.strategy.CloudSave
import hr.itrojnar.instagram.designpatterns.strategy.LocalSave
import hr.itrojnar.instagram.designpatterns.strategy.SettingsSaver

@Composable
fun SettingsScreen() {
    // Singleton usage
    val singletonUsage = SettingsManager.printSetting()

    // Observer usage
    val notifier = SettingsChangeNotifier()
    val observerOutput = StringBuilder()
    val observer = object : SettingsObserver {
        override fun onSettingChanged() {
            observerOutput.append("Settings changed!")
        }
    }
    notifier.addObserver(observer)
    notifier.notifyChange()

    // Factory usage
    val darkModeSetting = SettingFactory.getSetting("DarkMode")

    // Strategy pattern usage
    val settingsData = mapOf(
        "DarkMode" to true,
        "Notification" to false
    )
    val settingsSaver = SettingsSaver(LocalSave())
    val strategyOutput = StringBuilder()
    val isNetworkAvailable = true

    if (isNetworkAvailable) {
        settingsSaver.setStrategy(CloudSave())
        strategyOutput.append("Using CloudSave strategy.\n")
    } else {
        settingsSaver.setStrategy(LocalSave())
        strategyOutput.append("Using LocalSave strategy.\n")
    }

    settingsSaver.saveSettings(settingsData)

    // Builder pattern usage
    val notificationSetting = AdvancedSetting.Builder()
        .setName("Notifications")
        .setEnabled(true)
        .setSummary("Manage app notifications")
        .setIcon(R.drawable.instagram_verified) // Placeholder
        .build()

    // Adapter pattern usage
    val oldNotificationSetting = OldSetting("Notification", "Enabled")
    val newNotificationSetting = SettingAdapter(oldNotificationSetting)
    val adapterOutput = "${newNotificationSetting.name} is ${newNotificationSetting.adaptedValue}"

    // Visualize patterns
    Column {
        DesignPatternItem("Singleton", "Current Setting: $singletonUsage")
        DesignPatternItem("Observer", observerOutput.toString())
        DesignPatternItem("Factory", "Created Setting: ${darkModeSetting.name}")
        DesignPatternItem("Strategy", strategyOutput.toString())
        DesignPatternItem("Builder", "Setting: ${notificationSetting.name}, Enabled: ${notificationSetting.enabled}")
        DesignPatternItem("Adapter", adapterOutput)
    }
}
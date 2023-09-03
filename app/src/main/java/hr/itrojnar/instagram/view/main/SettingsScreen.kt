package hr.itrojnar.instagram.view.main

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.itrojnar.instagram.R
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
import hr.itrojnar.instagram.enums.Subscription
import hr.itrojnar.instagram.util.getUser
import hr.itrojnar.instagram.view.utility.SubscriptionCard
import java.time.LocalDate

@Composable
fun SettingsScreen() {
    val context = LocalContext.current
    val sharedPreferences = context.getSharedPreferences("user_details", Context.MODE_PRIVATE)
    val user = sharedPreferences.getUser()

    val selectedSubscription = remember { mutableStateOf(user.subscriptionId) }
    val lastChangedDate = remember { mutableStateOf(sharedPreferences.getString("lastChangedDate", "")) }

    val canChangeToday = LocalDate.now().toString() != lastChangedDate.value
    val hasChanged = remember { mutableStateOf(false) }  // New state to check if subscription was changed

    val currentUploadedPhotos = remember { mutableStateOf(sharedPreferences.getInt("numOfPicsUploadedToday", 0)) }
    val currentUploadedMegabytes = remember { mutableStateOf(sharedPreferences.getFloat("mbUsedToday", 0.0f).toDouble()) }

    // Retrieve the Subscription details using the selectedSubscription value
    val selectedSubDetails = Subscription.values().first { it.id == selectedSubscription.value }

    val photoLimit = selectedSubDetails.photoLimit
    val mbLimit = selectedSubDetails.uploadLimit

    val photoUsagePercentage = (currentUploadedPhotos.value.toDouble() / photoLimit) * 100
    val mbUsagePercentage = (currentUploadedMegabytes.value / mbLimit) * 100


    Column(
        modifier = Modifier.verticalScroll(rememberScrollState()).fillMaxSize()
    ) {
        Text (
            text = stringResource(R.string.your_subscription_settings),
            fontSize = 20.sp,
            modifier = Modifier.padding(10.dp)
        )

        Subscription.values().forEach { subscription ->
            val title = stringResource(id = subscription.titleResId)
            val description = stringResource(id = subscription.descriptionResId)

            SubscriptionCard(
                title = title,
                description = description,
                gradient = when (subscription) {
                    Subscription.FREE -> listOf(Color.LightGray, Color.Gray)
                    Subscription.PRO -> listOf(Color.Blue, Color(0xFF00008B))
                    Subscription.GOLD -> listOf(Color.Yellow, Color(0xFFFFD700))
                },
                onClick = {
                    if (canChangeToday) {
                        selectedSubscription.value = subscription.id
                        hasChanged.value = true
                    }
                },
                isSelected = selectedSubscription.value == subscription.id
            )
        }

        // If the user has changed the subscription today and can't change again
        if (!canChangeToday) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = stringResource(R.string.note_you_can_change_your_subscription_only_once_a_day_changes_will_take_effect_the_next_day)
            )
        }

        // If the user has made a recent change
        if (hasChanged.value) {
            Text(
                modifier = Modifier.padding(10.dp),
                text = stringResource(R.string.note_subscription_changed_awaiting_confirmation)  // This would be a new string resource indicating that the subscription has changed and the user needs to confirm
            )
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(horizontal = 10.dp),
                onClick = {
                    sharedPreferences.edit().apply {
                        putInt("subscriptionId", selectedSubscription.value)
                        putString("lastChangedDate", LocalDate.now().toString())
                        apply()
                    }
                    hasChanged.value = false  // Reset after user confirms
                },
                shape = RoundedCornerShape(5.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF3797EF)
                ),
            ) {
                Text(
                    stringResource(R.string.confirm_subscription_change),
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
        }

        Text(
            text = "Usage: ${currentUploadedPhotos.value} / $photoLimit photos (${photoUsagePercentage.toInt()}%)",
            fontSize = 14.sp,
            modifier = Modifier.padding(10.dp)
        )
        // Add a line or a ProgressBar to visualize the percentage
        UsageLine(photoUsagePercentage)

        Text(
            text = "Usage: ${currentUploadedMegabytes.value} / $mbLimit MB (${mbUsagePercentage.toInt()}%)",
            fontSize = 14.sp,
            modifier = Modifier.padding(10.dp)
        )
        // Add a line or a ProgressBar to visualize the percentage
        UsageLine(mbUsagePercentage)

        Spacer(modifier = Modifier.height(65.dp))
    }

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
//    Column {
//        DesignPatternItem("Singleton", "Current Setting: $singletonUsage")
//        DesignPatternItem("Observer", observerOutput.toString())
//        DesignPatternItem("Factory", "Created Setting: ${darkModeSetting.name}")
//        DesignPatternItem("Strategy", strategyOutput.toString())
//        DesignPatternItem("Builder", "Setting: ${notificationSetting.name}, Enabled: ${notificationSetting.enabled}")
//        DesignPatternItem("Adapter", adapterOutput)
//    }
}

@Composable
fun UsageLine(percentage: Double) {
    // A simple line (can be replaced with a ProgressBar for a better visual)
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(5.dp)
            .background(Color.Gray)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth(fraction = percentage.toFloat() / 100)
                .height(5.dp)
                .background(Color.Blue)
        )
    }
}
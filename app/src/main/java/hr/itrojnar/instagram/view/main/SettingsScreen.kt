package hr.itrojnar.instagram.view.main

import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
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
import hr.itrojnar.instagram.enums.Subscription
import hr.itrojnar.instagram.model.User
import hr.itrojnar.instagram.util.getUser
import hr.itrojnar.instagram.view.utility.SubscriptionCard
import hr.itrojnar.instagram.view.utility.UsageLine
import java.text.NumberFormat
import java.text.ParseException
import java.time.LocalDate
import java.util.Locale

@Composable
fun SettingsScreen() {

    val darkTheme = isSystemInDarkTheme()
    val backgroundColor = if (darkTheme) Color.Black else Color.Transparent

    val context = LocalContext.current

    val firebaseAuth = FirebaseAuth.getInstance()
    val currentUser = firebaseAuth.currentUser
    val uid = currentUser?.uid

    val firebaseUser = remember { mutableStateOf<User?>(null) }
    val isLoading = remember { mutableStateOf(true) }

    LaunchedEffect(uid) {
        val db = FirebaseFirestore.getInstance()
        val docRef = db.collection("users").document(uid!!)
        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null) {
                    val userData = document.toObject(User::class.java)
                    firebaseUser.value = userData
                }
                isLoading.value = false
            }
            .addOnFailureListener { exception ->
                isLoading.value = false
            }
    }

    if (!isLoading.value) {
        val sharedPreferences = context.getSharedPreferences("user_details", Context.MODE_PRIVATE)
        val user = sharedPreferences.getUser()

        val selectedSubscription = remember { mutableStateOf(user.subscriptionId) }
        val lastChangedDate = remember { mutableStateOf(sharedPreferences.getString("lastChangedDate", "")) }

        val canChangeToday = LocalDate.now().toString() != lastChangedDate.value
        val hasChanged = remember { mutableStateOf(false) }

        val currentUploadedPhotos = firebaseUser.value!!.numOfPicsUploadedToday
        val currentUploadedMegabytes = String.format(Locale.US,"%.2f", firebaseUser.value!!.mbUsedToday.toDouble()).toDouble()

        // Retrieve the Subscription details using the selectedSubscription value
        val selectedSubDetails = Subscription.values().first { it.id == selectedSubscription.value }

        val photoLimit = selectedSubDetails.photoLimit
        val mbLimit = selectedSubDetails.uploadLimit

        val photoUsagePercentage = (currentUploadedPhotos.toDouble() / photoLimit) * 100
        val mbUsagePercentage = (currentUploadedMegabytes / mbLimit) * 100

        val photoUsageText = buildAnnotatedString {
            val boldStyle = SpanStyle(fontWeight = FontWeight.Bold)

            append(stringResource(R.string.usage_photos) + " ")
            withStyle(boldStyle) {
                append(currentUploadedPhotos.toString())
            }
            append("/")
            withStyle(boldStyle) {
                append("$photoLimit")
            }
            append(stringResource(R.string.photos))
            withStyle(boldStyle) {
                append(photoUsagePercentage.toInt().toString())
                append("%")
            }
            append(")")
        }

        val mbsUsageText = buildAnnotatedString {
            val boldStyle = SpanStyle(fontWeight = FontWeight.Bold)

            append(stringResource(R.string.usage_mb) + " ")
            withStyle(boldStyle) {
                append(currentUploadedMegabytes.toString())
                append("/")
            }
            withStyle(boldStyle) {
                append("$mbLimit")
            }
            append(" MB (")
            withStyle(boldStyle) {
                append(mbUsagePercentage.toInt().toString())
                append("%")
            }
            append(")")
        }

        //DESIGN PATTERNS
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
            .setIcon(R.drawable.instagram_verified)
            .build()

        // Adapter pattern usage
        val oldNotificationSetting = OldSetting("Notification", "Enabled")
        val newNotificationSetting = SettingAdapter(oldNotificationSetting)
        val adapterOutput = "${newNotificationSetting.name} is ${newNotificationSetting.adaptedValue}"


        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .fillMaxSize()
                .background(backgroundColor)
        ) {
            Text (
                text = stringResource(R.string.your_subscription_settings),
                fontSize = 20.sp,
                modifier = Modifier.padding(10.dp),
                fontWeight = FontWeight.Bold
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

            if (!canChangeToday) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = stringResource(R.string.note_you_can_change_your_subscription_only_once_a_day_changes_will_take_effect_the_next_day)
                )
            }

            if (hasChanged.value) {
                Text(
                    modifier = Modifier.padding(10.dp),
                    text = stringResource(R.string.note_subscription_changed_awaiting_confirmation)
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
                        hasChanged.value = false
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

            Text (
                text = stringResource(R.string.your_usage_statistics),
                fontSize = 20.sp,
                modifier = Modifier.padding(10.dp),
                fontWeight = FontWeight.Bold
            )

            Text(
                text = photoUsageText,
                fontSize = 17.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 5.dp)
            )

            UsageLine(photoUsagePercentage)

            Text(
                text = mbsUsageText,
                fontSize = 17.sp,
                modifier = Modifier.padding(horizontal = 10.dp, vertical = 10.dp)
            )

            UsageLine(mbUsagePercentage)

            Spacer(modifier = Modifier.height(30.dp))

            DesignPatternItem("Singleton", "Current Setting: $singletonUsage")
            DesignPatternItem("Observer", observerOutput.toString())
            DesignPatternItem("Factory", "Created Setting: ${darkModeSetting.name}")
            DesignPatternItem("Strategy", strategyOutput.toString())
            DesignPatternItem("Builder", "Setting: ${notificationSetting.name}, Enabled: ${notificationSetting.enabled}")
            DesignPatternItem("Adapter", adapterOutput)

            Spacer(modifier = Modifier.height(65.dp))
        }
    }
}

fun parseDoubleLocaleAware(input: String): Double? {
    return try {
        val numberFormat = NumberFormat.getInstance(Locale.getDefault())
        val number = numberFormat.parse(input)
        number?.toDouble()
    } catch (e: ParseException) {
        null
    }
}
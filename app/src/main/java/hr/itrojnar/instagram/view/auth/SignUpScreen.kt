package hr.itrojnar.instagram.view.auth

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import hr.itrojnar.instagram.util.findActivity
import hr.itrojnar.instagram.view.dialogue.CameraPermissionTextProvider
import hr.itrojnar.instagram.view.dialogue.MediaImagesPermissionTextProvider
import hr.itrojnar.instagram.view.dialogue.PermissionDialog
import hr.itrojnar.instagram.view.dialogue.PhoneCallPermissionTextProvider
import hr.itrojnar.instagram.view.dialogue.RecordAudioPermissionTextProvider
import hr.itrojnar.instagram.viewmodel.MainViewModel

@Composable
fun SignUpScreen(modifier: Modifier, onLogInClick: () -> Unit, onRegister: () -> Unit) {

    var currentScreen by remember { mutableStateOf("SignUp") }

    val context = LocalContext.current
    val activity = context.findActivity()

    val viewModel = viewModel<MainViewModel>()
    val dialogQueue = viewModel.visiblePermissionDialogQueue

    val commonPermissions = listOf(
        Manifest.permission.CAMERA,
        Manifest.permission.RECORD_AUDIO,
        Manifest.permission.CALL_PHONE,
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        (listOf(Manifest.permission.READ_MEDIA_IMAGES) + commonPermissions).toTypedArray()
    } else {
        (listOf(Manifest.permission.READ_EXTERNAL_STORAGE) + commonPermissions).toTypedArray()
    }

    val multiplePermissionResultLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.RequestMultiplePermissions(),
            onResult = { permissions ->
                permissionsToRequest.forEach { permission ->
                    viewModel.onPermissionResult(
                        permission = permission, isGranted = permissions[permission] == true
                    )
                }
            })

    AnimatedVisibility(visible = currentScreen == "SignUp",
        enter = if (currentScreen == "SignUp") slideInHorizontally(initialOffsetX = { -it }) else slideInHorizontally(initialOffsetX = { it }),
        exit = if (currentScreen == "SignUp") slideOutHorizontally(targetOffsetX = { it }) else slideOutHorizontally(targetOffsetX = { -it })
    ) {
        Button(
            onClick = onLogInClick,
        ) {
            Text(text = "Switch to Log In")
        }
        Button(
            modifier = modifier.padding(top = 60.dp).fillMaxWidth(),
            onClick = {
                multiplePermissionResultLauncher.launch(permissionsToRequest)
            },
        ) {
            Text(text = "Request permissions")
        }
        dialogQueue.reversed().forEach { permission ->
                PermissionDialog(permissionTextProvider = when (permission) {
                    Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_EXTERNAL_STORAGE -> {
                        MediaImagesPermissionTextProvider()
                    }

                    Manifest.permission.CAMERA -> {
                        CameraPermissionTextProvider()
                    }

                    Manifest.permission.RECORD_AUDIO -> {
                        RecordAudioPermissionTextProvider()
                    }

                    Manifest.permission.CALL_PHONE -> {
                        PhoneCallPermissionTextProvider()
                    }

                    else -> return@forEach
                }, isPermanentlyDeclined = !activity!!.shouldShowRequestPermissionRationale(
                    permission
                ), onDismiss = viewModel::dismissDialog, onOkClick = {
                    viewModel.dismissDialog()
                    multiplePermissionResultLauncher.launch(
                        arrayOf(permission)
                    )
                }, onGoToAppSettingsClick = { openAppSettings(activity = activity) })
            }
        Button(
            modifier = modifier.padding(top = 150.dp),
            onClick = { currentScreen = "Subscription" },
        ) {
            Text(text = "Subscription")
        }
    }

    AnimatedVisibility(
        visible = currentScreen == "Subscription",
        enter = if (currentScreen == "Subscription") slideInHorizontally(initialOffsetX = { it }) else slideInHorizontally(initialOffsetX = { -it }),
        exit = if (currentScreen == "Subscription") slideOutHorizontally(targetOffsetX = { -it }) else slideOutHorizontally(targetOffsetX = { it })
    ) {
        Button(
            modifier = modifier.fillMaxWidth(),
            onClick = { currentScreen = "SignUp" },
        ) {
            Text(text = "Back")
        }
    }
}

fun openAppSettings(activity: Activity) {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", activity.packageName, null)
    ).also { activity.startActivity(it) }
}
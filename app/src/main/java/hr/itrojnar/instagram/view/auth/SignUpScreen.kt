package hr.itrojnar.instagram.view.auth

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.lifecycle.viewmodel.compose.viewModel
import hr.itrojnar.instagram.util.findActivity
import hr.itrojnar.instagram.view.dialogue.CameraPermissionTextProvider
import hr.itrojnar.instagram.view.dialogue.PermissionDialog
import hr.itrojnar.instagram.viewmodel.MainViewModel

@Composable
fun SignUpScreen(modifier: Modifier, onLogInClick: () -> Unit, onRegister: () -> Unit) {

    val context = LocalContext.current
    val activity = context.findActivity()

    val viewModel = viewModel<MainViewModel>()
    val dialogQueue = viewModel.visiblePermissionDialogQueue

    val cameraPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            viewModel.onPermissionResult(
                permission = Manifest.permission.CAMERA,
                isGranted = isGranted
            )
        }
    )

    val multiplePermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { permissions ->
            permissions.keys.forEach { permission ->
                viewModel.onPermissionResult(
                    permission = permission,
                    isGranted = permissions[permission] == true
                )
            }
        }
    )

    Button(
        onClick = onLogInClick,
    ) {
        Text(text = "Switch to Log In")
    }
    Button(
        modifier = modifier.padding(top = 60.dp),
        onClick = {
            multiplePermissionResultLauncher.launch(
                arrayOf(
                    Manifest.permission.CAMERA,
                    Manifest.permission.RECORD_AUDIO,
                    Manifest.permission.CALL_PHONE
                )
            )
        },
    ) {
        Text(text = "Request permissions")
    }
    dialogQueue
        .reversed()
        .forEach { permission ->
            PermissionDialog(
                permissionTextProvider = when (permission) {
                    Manifest.permission.CAMERA -> {
                        CameraPermissionTextProvider()
                    }
                    Manifest.permission.RECORD_AUDIO -> {
                        CameraPermissionTextProvider()
                    }
                    Manifest.permission.CALL_PHONE -> {
                        CameraPermissionTextProvider()
                    }
                    else -> return@forEach
                },
                isPermanentlyDeclined = !activity!!.shouldShowRequestPermissionRationale(
                    permission
                ),
                onDismiss = viewModel::dismissDialog,
                onOkClick = {
                            viewModel.dismissDialog()
                    multiplePermissionResultLauncher.launch(
                        arrayOf(permission)
                    )
                },
                onGoToAppSettingsClick = { openAppSettings(activity) }
            )
        }
}

fun openAppSettings(activity: Activity) {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", activity.packageName, null)
    ).also { activity.startActivity(it) }
}
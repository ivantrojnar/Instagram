package hr.itrojnar.instagram.view.auth

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberImagePainter
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.util.LogoImage
import hr.itrojnar.instagram.util.LottieAnimationLoop
import hr.itrojnar.instagram.util.findActivity
import hr.itrojnar.instagram.view.dialog.CameraPermissionTextProvider
import hr.itrojnar.instagram.view.dialog.ImagePickerDialog
import hr.itrojnar.instagram.view.dialog.MediaImagesPermissionTextProvider
import hr.itrojnar.instagram.view.dialog.PermissionDialog
import hr.itrojnar.instagram.view.dialog.PhoneCallPermissionTextProvider
import hr.itrojnar.instagram.view.dialog.RecordAudioPermissionTextProvider
import hr.itrojnar.instagram.viewmodel.MainViewModel
import java.io.ByteArrayOutputStream
import hr.itrojnar.instagram.enums.Subscription
import hr.itrojnar.instagram.util.createUserWithImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
    modifier: Modifier,
    onSignUpEmailChanged: (String) -> Unit,
    onSignUpPasswordChanged: (String) -> Unit,
    onFullNameChanged: (String) -> Unit,
    onImageUriChanged: (Uri) -> Unit,
    signUpState: SignUpState,
    onLogInClick: () -> Unit,
    onSignUp: () -> Unit,
    resetSignUpState: () -> Unit,
) {

    var currentScreen by remember { mutableStateOf("SignUp") }

    val focusManager = LocalFocusManager.current

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

    var showImagePickerDialog by remember { mutableStateOf(false) }
    var imageUri by remember { mutableStateOf<Uri?>(null) }

    var selectedSubscription by remember { mutableStateOf<String?>(null) }
    var selectedSubscriptionId by remember { mutableStateOf<Int?>(null) }

    fun onSelectedChanged(subscriptionId: Int, subscription: String) {
        selectedSubscriptionId = subscriptionId
        selectedSubscription = subscription
    }

    val permissionsToRequest = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        (listOf(Manifest.permission.READ_MEDIA_IMAGES) + commonPermissions).toTypedArray()
    } else {
        (listOf(Manifest.permission.READ_EXTERNAL_STORAGE) + commonPermissions).toTypedArray()
    }

    val takePictureLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            bitmap?.let {
                val bytes = ByteArrayOutputStream()
                it.compress(Bitmap.CompressFormat.JPEG, 100, bytes)
                val path = MediaStore.Images.Media.insertImage(
                    context.contentResolver, it, "Title", null
                )
                imageUri = Uri.parse(path)
                imageUri?.let {uri ->
                    onImageUriChanged(uri)
                }
            }
        }

    val pickImageLauncher =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri: Uri? ->
            imageUri = uri
            uri?.let {
                onImageUriChanged(it)
            }
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

    var showDialog by remember { mutableStateOf(false) }

    // DIALOG FOR IMAGE SELECTION
    if (showImagePickerDialog) {
        ImagePickerDialog(
            onTakePhoto = {
                takePictureLauncher.launch(null)
                showImagePickerDialog = false
            },
            onSelectFromGallery = {
                pickImageLauncher.launch("image/*")
                showImagePickerDialog = false
            },
            onDismissRequest = { showImagePickerDialog = false }
        )
    }

    AnimatedVisibility(
        visible = currentScreen == "SignUp",
        enter = if (currentScreen == "SignUp") slideInHorizontally(initialOffsetX = { -it }) else slideInHorizontally(
            initialOffsetX = { it }),
        exit = if (currentScreen == "SignUp") slideOutHorizontally(targetOffsetX = { it }) else slideOutHorizontally(
            targetOffsetX = { -it })
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Transparent)
        ) {
            LogoImage(topPadding = 10, 0)

            LottieAnimationLoop(
                resId = R.raw.sign_up_animation,
                modifier
                    .fillMaxWidth()
                    .height(170.dp)
            )
            Text(
                text = stringResource(R.string.sign_up),
                style = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 21.sp,
                    textAlign = TextAlign.Center
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .padding(bottom = 10.dp)
            )
            ProfileImage(imageUri = imageUri) {
                multiplePermissionResultLauncher.launch(permissionsToRequest)
                showImagePickerDialog = true
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 0.dp, end = 20.dp, bottom = 0.dp)
                    .background(Color.Transparent),
                shape = RoundedCornerShape(10.dp),
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    value = signUpState.fullName,
                    onValueChange = onFullNameChanged,
                    label = { Text(text = stringResource(R.string.full_name)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedLabelColor = Color.Gray, // Color for the hint text when not focused
                        unfocusedBorderColor = Color.Gray, // Color for the border when not focused
                        focusedLabelColor = Color.Black, // Color for the hint text when focused
                        focusedBorderColor = Color.Black, // Color for the border when focused
                    )
                )
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 0.dp, end = 20.dp, bottom = 0.dp)
                    .background(Color.Transparent),
                shape = RoundedCornerShape(10.dp),
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    value = signUpState.email,
                    onValueChange = onSignUpEmailChanged,
                    label = { Text(text = stringResource(R.string.email)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Email,
                        imeAction = ImeAction.Next
                    ),
                    keyboardActions = KeyboardActions(
                        onNext = { focusManager.moveFocus(FocusDirection.Down) }
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedLabelColor = Color.Gray, // Color for the hint text when not focused
                        unfocusedBorderColor = Color.Gray, // Color for the border when not focused
                        focusedLabelColor = Color.Black, // Color for the hint text when focused
                        focusedBorderColor = Color.Black, // Color for the border when focused
                    )
                )
            }
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, top = 0.dp, end = 20.dp, bottom = 10.dp)
                    .background(Color.Transparent),
                shape = RoundedCornerShape(10.dp),
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    value = signUpState.password,
                    onValueChange = onSignUpPasswordChanged,
                    label = { Text(text = stringResource(R.string.password)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    ),
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        unfocusedLabelColor = Color.Gray,
                        unfocusedBorderColor = Color.Gray,
                        focusedLabelColor = Color.Black,
                        focusedBorderColor = Color.Black,
                    ),
                    visualTransformation = PasswordVisualTransformation()
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 20.dp, end = 20.dp),
                onClick = {
                    currentScreen = "Subscription"
                },
                enabled = signUpState.isImageSelected && signUpState.isFullNameValid && signUpState.isEmailValid && signUpState.isPasswordValid,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color(0xFF3797EF).copy(alpha = 0.4f),
                    containerColor = Color(0xFF3797EF)
                ),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(
                    stringResource(R.string.next),
                    fontSize = 16.sp,
                    color = Color.White
                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                ClickableText(
                    text = AnnotatedString(stringResource(R.string.back_to_log_in)),
                    onClick = {
                        onLogInClick()
                        resetSignUpState()
                    },
                    style = TextStyle(
                        color = Color(0xFF3797EF),
                        fontSize = 16.sp
                    )
                )
            }

            dialogQueue.reversed().forEach { permission ->
                PermissionDialog(permissionTextProvider = when (permission) {
                    Manifest.permission.READ_MEDIA_IMAGES, Manifest.permission.READ_EXTERNAL_STORAGE -> {
                        MediaImagesPermissionTextProvider(context)
                    }

                    Manifest.permission.CAMERA -> {
                        CameraPermissionTextProvider(context)
                    }

                    Manifest.permission.RECORD_AUDIO -> {
                        RecordAudioPermissionTextProvider(context)
                    }

                    Manifest.permission.CALL_PHONE -> {
                        PhoneCallPermissionTextProvider(context)
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
        }
    }

    AnimatedVisibility(
        visible = currentScreen == "Subscription",
        enter = if (currentScreen == "Subscription") slideInHorizontally(initialOffsetX = { it }) else slideInHorizontally(
            initialOffsetX = { -it }),
        exit = if (currentScreen == "Subscription") slideOutHorizontally(targetOffsetX = { -it }) else slideOutHorizontally(
            targetOffsetX = { it })
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp, top = 10.dp, bottom = 0.dp)
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.back_arrow),
                modifier = Modifier
                    .padding(16.dp)
                    .size(24.dp)
                    .clickable { currentScreen = "SignUp" }
            )
            Text(
                text = if (selectedSubscription != null) stringResource(R.string.current_selection) + " $selectedSubscription" else stringResource(
                                    R.string.no_subscription_selected),
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(horizontal = 10.dp)
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
                    onClick = { onSelectedChanged(subscription.id, title) },
                    isSelected = selectedSubscription == title
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 10.dp, end = 10.dp),
                onClick = {
                    createUserWithImage(
                        email = signUpState.email,
                        fullName = signUpState.fullName,
                        password = signUpState.password,
                        imageUri = imageUri!!,
                        subscriptionId = selectedSubscriptionId!!,
                        onSuccess = {
                            onSignUp()
                        },
                        onFailure = {
                            Toast.makeText(context, context.getString(R.string.unable_to_register), Toast.LENGTH_SHORT).show()
                        }
                    )
                },
                enabled = selectedSubscriptionId != null,
                colors = ButtonDefaults.buttonColors(
                    disabledContainerColor = Color(0xFF3797EF).copy(alpha = 0.4f),
                    containerColor = Color(0xFF3797EF)
                ),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(
                    stringResource(R.string.sign_up),
                    fontSize = 16.sp,
                    color = Color.White,
                    modifier = Modifier.padding(top = 5.dp)
                )
            }
        }
        if (showDialog) {
            Dialog(onDismissRequest = { showDialog = false }) {
                Box(
                    modifier = Modifier
                        .background(Color(0xFF3797EF), RoundedCornerShape(8.dp))
                        .padding(2.dp)
                        .size(350.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .background(Color.White, RoundedCornerShape(6.dp))
                            .padding(10.dp)
                            .fillMaxSize(0.98f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                            LottieAnimationLoop(
                                resId = R.raw.email_password_success_animation,
                                Modifier.width(280.dp).height(200.dp)
                            )

                            Button(
                                onClick = { showDialog = false },
                                modifier = Modifier.width(100.dp).height(40.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF3797EF)
                                ),
                                shape = RoundedCornerShape(5.dp)
                            ) {
                                Text("Ok")
                            }
                        }
                    }
                }
            }
        }
    }
}

fun openAppSettings(activity: Activity) {
    Intent(
        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
        Uri.fromParts("package", activity.packageName, null)
    ).also { activity.startActivity(it) }
}

@Composable
fun SubscriptionCard(
    title: String,
    description: String,
    gradient: List<Color>,
    onClick: () -> Unit,
    isSelected: Boolean
) {
    val border = if (isSelected) BorderStroke(4.dp, Color.Black) else null

    Card(
        modifier = Modifier
            .padding(horizontal = 10.dp, vertical = 8.dp)
            .clickable(onClick = onClick),
        border = border,
        shape = RoundedCornerShape(15.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .requiredHeight(200.dp)
                .background(Brush.horizontalGradient(gradient))
        ) {
            Column(
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(10.dp),
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(
                        shadow = Shadow(
                            Color.Black,
                            offset = Offset(1f, 1f),
                            blurRadius = 2f
                        )
                    ),
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyLarge.copy(
                        shadow = Shadow(
                            Color.Black,
                            offset = Offset(1f, 1f),
                            blurRadius = 2f
                        )
                    ),
                    color = Color.White,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            }
        }
    }
}

@Composable
fun ProfileImage(imageUri: Uri?, onImageClick: () -> Unit) {
    val image: Painter = imageUri?.let {
        rememberImagePainter(data = it)
    } ?: painterResource(id = R.drawable.default_profile_picture)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp), horizontalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .clickable { onImageClick() }) {
            Image(
                painter = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
        }
    }
}
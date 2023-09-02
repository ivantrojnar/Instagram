package hr.itrojnar.instagram.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.net.Uri
import androidx.annotation.DrawableRes
import androidx.annotation.RawRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.graphics.drawable.toBitmap
import coil.ImageLoader
import coil.request.ImageRequest
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.model.User
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import java.util.UUID
import java.util.concurrent.TimeUnit
import java.util.regex.Pattern

@Composable
fun LottieAnimationLoop(@RawRes resId: Int, modifier: Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)

    LottieAnimation(
        composition,
        progress = progress,
        modifier = modifier
    )
}

@Composable
fun LottieAnimation(@RawRes resId: Int, modifier: Modifier) {
    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
    val progress by animateLottieCompositionAsState(composition)

    LottieAnimation(
        composition,
        progress = progress,
        modifier = modifier
    )
}

fun String.isValidPassword() =
    Pattern.compile("^" +
            "(?=.*[0-9])" +         //at least 1 digit
            "(?=.*[a-z])" +         //at least 1 lower case letter
            "(?=.*[A-Z])" +         //at least 1 upper case letter
            "(?=.*[a-zA-Z])" +      //any letter
            "(?=.*[@#$%^&+=])" +    //at least 1 special character
            "(?=\\S+$)" +           //no white spaces
            ".{8,}" +               //at least 8 characters
            "$").matcher(this).matches()

fun Context.findActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) {
            return currentContext
        }
        currentContext = currentContext.baseContext
    }
    return null
}

@Composable
fun LogoImage(topPadding: Int, bottomPadding: Int) {
    Image(
        painter = painterResource(id = R.drawable.instagram_logo),
        contentDescription = stringResource(R.string.instagram_logo),
        contentScale = ContentScale.Fit,
        modifier = Modifier.padding(
            start = 95.dp,
            top = topPadding.dp,
            end = 95.dp,
            bottom = bottomPadding.dp
        )
    )
}

@Composable
fun ShowSuccessDialog(showDialog: MutableState<Boolean>) {
    if (showDialog.value) {
        Dialog(onDismissRequest = { showDialog.value = false }) {
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
                            resId = R.raw.success_animation,
                            Modifier
                                .width(300.dp)
                                .height(250.dp)
                        )
                    }
                }
            }
        }
    }
}

fun createUserWithImage(
    email: String,
    password: String,
    fullName: String,
    imageUri: Uri,
    subscriptionId: Int,
    onSuccess: () -> Unit,
    onFailure: (Exception) -> Unit
    ) {

    val auth = Firebase.auth

    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val firebaseUser = auth.currentUser

            val storage = FirebaseStorage.getInstance()
            val storageRef = storage.reference
            val imageRef = storageRef.child("profile_images/${UUID.randomUUID()}.jpg")

            val uploadTask = imageRef.putFile(imageUri)
            uploadTask.continueWithTask { uploadTask ->
                if (!uploadTask.isSuccessful) {
                    uploadTask.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }.addOnCompleteListener { downloadTask ->
                if (downloadTask.isSuccessful) {
                    val downloadUrl = downloadTask.result

                    val today = LocalDate.now()
                    val user = hashMapOf(
                        "firebaseUserId" to firebaseUser?.uid,
                        "fullName" to fullName,
                        "email" to email,
                        "profilePictureUrl" to downloadUrl.toString(),
                        "subscriptionId" to subscriptionId,
                        "lastSignInDate" to today.toString(),
                        "mbUsedToday" to 0,
                        "numOfPicsUploadedToday" to 0
                    )

                    val db = Firebase.firestore
                    firebaseUser?.let {
                        db.collection("users").document(it.uid).set(user).addOnSuccessListener {
                            onSuccess()
                        }.addOnFailureListener { e ->
                            onFailure(e)
                        }
                    }
                } else {
                    onFailure(downloadTask.exception!!)
                }
            }
        }
    }
}

@Composable
fun vectorResource(id: Int): ImageVector {
    return ImageVector.vectorResource(id)
}

@Composable
fun loadPicture(url: String, @DrawableRes defaultImage: Int): State<Bitmap?> {
    val imageState: MutableState<Bitmap?> = remember { mutableStateOf(null) }
    val context = LocalContext.current

    val imageLoader = ImageLoader(context)
    val request = ImageRequest.Builder(context)
        .data(url)
        .target { drawable ->
            imageState.value = drawable.toBitmap()
        }
        .error(defaultImage)
        .build()

    DisposableEffect(url) {
        val disposable = imageLoader.enqueue(request)
        onDispose {
            disposable.dispose()
        }
    }

    return imageState
}

fun formatDate(date: Date, currentYear: Int): String {
    val now = Date()
    val minutesDiff = TimeUnit.MILLISECONDS.toMinutes(now.time - date.time)
    val hoursDiff = TimeUnit.MILLISECONDS.toHours(now.time - date.time)
    val daysDiff = TimeUnit.MILLISECONDS.toDays(now.time - date.time)

    return when {
        minutesDiff <= 1 -> "A minute ago"
        minutesDiff in 2..59 -> "$minutesDiff minutes ago"
        hoursDiff == 1L -> "1 hour ago"
        hoursDiff in 2L..23L -> "$hoursDiff hours ago"
        daysDiff == 1L -> "1 day ago"
        daysDiff in 2L..6L -> "$daysDiff days ago"
        SimpleDateFormat("yyyy").format(date).toInt() == currentYear -> SimpleDateFormat("d MMMM").format(date)
        else -> SimpleDateFormat("d MMMM yyyy").format(date)
    }
}

val instagramGradientColors = listOf(
    Color(0xFFF58529), // Orange
    Color(0xFFDD2A7B), // Pinkish
    Color(0xFF8134AF), // Purple
    Color(0xFF515BD4)  // Blue
)

fun SharedPreferences.Editor.putUser(user: User) {
    putString("firebaseUserId", user.firebaseUserId)
    putString("fullName", user.fullName)
    putString("email", user.email)
    putString("profilePictureUrl", user.profilePictureUrl)
    putInt("subscriptionId", user.subscriptionId)
    putString("lastSignInDate", user.lastSignInDate)
    putInt("mbUsedToday", user.mbUsedToday)
    putInt("numOfPicsUploadedToday", user.numOfPicsUploadedToday)
    apply()
}

fun SharedPreferences.getUser(): User {
    return User(
        firebaseUserId = getString("firebaseUserId", "") ?: "",
        fullName = getString("fullName", "") ?: "",
        email = getString("email", "") ?: "",
        profilePictureUrl = getString("profilePictureUrl", null),
        subscriptionId = getInt("subscriptionId", 0),
        lastSignInDate = getString("lastSignInDate", LocalDate.now().toString()) ?: LocalDate.now().toString(),
        mbUsedToday = getInt("mbUsedToday", 0),
        numOfPicsUploadedToday = getInt("numOfPicsUploadedToday", 0)
    )
}

fun getRandomNumber(): Int = (0..10000).random()
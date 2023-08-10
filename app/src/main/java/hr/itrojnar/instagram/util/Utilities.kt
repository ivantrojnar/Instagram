package hr.itrojnar.instagram.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import hr.itrojnar.instagram.R
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
            // Outer Box with gray background to act as the border
            Box(
                modifier = Modifier
                    .background(Color(0xFF3797EF), RoundedCornerShape(8.dp))
                    .padding(2.dp)
                    .size(350.dp),
                contentAlignment = Alignment.Center
            ) {
                // Inner Box with white background
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

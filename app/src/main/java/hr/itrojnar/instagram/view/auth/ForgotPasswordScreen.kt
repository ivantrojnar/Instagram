package hr.itrojnar.instagram.view.auth

import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.ktx.Firebase
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.util.LogoImage
import hr.itrojnar.instagram.util.LottieAnimation
import hr.itrojnar.instagram.util.LottieAnimationLoop

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    modifier: Modifier,
    onLogInClick: () -> Unit){

    val context = LocalContext.current

    val darkTheme = isSystemInDarkTheme()
    val backgroundColor = if (darkTheme) Color.Black else Color.Transparent
    val unfocusedLabelColor = if (darkTheme) Color.LightGray else Color.Gray // Color for the hint text when not focused
    val unfocusedBorderColor = if (darkTheme) Color.LightGray else Color.Gray // Color for the border when not focused
    val focusedLabelColor = if (darkTheme) Color.White else Color.Black // Color for the hint text when focused
    val focusedBorderColor = if (darkTheme) Color.White else Color.Black // Color for the border when focused
    val textColor = if (darkTheme) Color.White else Color.Black // Color for the border when focused

    val focusManager = LocalFocusManager.current

    var forgotPasswordEmailState by remember {
        mutableStateOf("")
    }

    var isEmailValid by remember { mutableStateOf(false) }

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(backgroundColor)) {

        LogoImage(topPadding = 110, 25)

        LottieAnimationLoop(resId = R.raw.forgot_password_animation,
            modifier
                .fillMaxWidth()
                .height(250.dp))

        Text(
            text = stringResource(R.string.trouble_logging_in),
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 16.dp)
                .testTag("Trouble loggin in?"),
            color = textColor
        )

        Text(
            text = stringResource(R.string.enter_your_email_and_we_ll_send_you_a_link_to_get_back_into_your_account),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            color = textColor
        )

        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            value = forgotPasswordEmailState,
            onValueChange = { value ->
                forgotPasswordEmailState = value
                isEmailValid = Patterns.EMAIL_ADDRESS.matcher(value).matches()
            },
            label = { Text(text = stringResource(R.string.email)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Done
            ),
            keyboardActions = KeyboardActions (
                onDone = { focusManager.clearFocus() }
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedLabelColor = unfocusedLabelColor,
                unfocusedBorderColor = unfocusedBorderColor,
                focusedLabelColor = focusedLabelColor,
                focusedBorderColor = focusedBorderColor,
            )
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = 20.dp, end = 20.dp, top = 10.dp),
            onClick = {
                val auth = FirebaseAuth.getInstance()
                auth.sendPasswordResetEmail(forgotPasswordEmailState)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // Handle success here
                            val bundle = Bundle().apply {
                                putString("email", forgotPasswordEmailState)
                            }
                            Firebase.analytics.logEvent("request_email_for_password_reset", bundle)
                            showDialog = true
                        } else {
                            // Handle failure here
                            Toast.makeText(context, context.getString(R.string.unable_to_send_email_to_reset_password), Toast.LENGTH_SHORT).show()
                        }
                    }
            },
            enabled = isEmailValid,
            colors = ButtonDefaults.buttonColors(
                disabledContainerColor = Color(0xFF3797EF).copy(alpha = 0.4f),
                containerColor = Color(0xFF3797EF)
            ),
            shape = RoundedCornerShape(5.dp)
        ) {
            Text(
                stringResource(R.string.next),
                fontSize = 16.sp,
                color = Color.White)
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            ClickableText(
                text = AnnotatedString(stringResource(R.string.back_to_log_in)),
                onClick = { onLogInClick() },
                style = TextStyle(
                    color = Color(0xFF3797EF),
                    fontSize = 16.sp
                )
            )
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

                            Text(
                                text = stringResource(R.string.instructions_sent_to_your_email),
                                textAlign = TextAlign.Center,
                                style = TextStyle(fontSize = 16.sp, color = Color.Black)
                            )

                            Button(
                                onClick = { showDialog = false },
                                modifier = Modifier.width(100.dp).height(40.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF3797EF)
                                ),
                                shape = RoundedCornerShape(5.dp)
                            ) {
                                Text(stringResource(R.string.ok))
                            }
                        }
                    }
                }
            }
        }
    }
}
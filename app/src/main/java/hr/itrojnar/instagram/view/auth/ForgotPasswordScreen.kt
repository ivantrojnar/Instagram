package hr.itrojnar.instagram.view.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.util.LogoImage
import hr.itrojnar.instagram.util.LottieAnimationLoop

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ForgotPasswordScreen(
    modifier: Modifier,
    onLogInClick: () -> Unit,
    onRequestEmailForForgottenPassword: () -> Unit) {

    val focusManager = LocalFocusManager.current

    var forgotPasswordEmailState by remember {
        mutableStateOf("")
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)) {

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
        )

        Text(
            text = stringResource(R.string.enter_your_email_and_we_ll_send_you_a_link_to_get_back_into_your_account),
            textAlign = TextAlign.Center,
            fontSize = 14.sp,
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 8.dp)
        )

        OutlinedTextField(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 10.dp),
            value = forgotPasswordEmailState,
            onValueChange = { forgotPasswordEmailState = it },
            label = { Text(text = stringResource(R.string.email)) },
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Email,
                imeAction = ImeAction.Next
            ),
            keyboardActions = KeyboardActions (
                onDone = { focusManager.clearFocus() }
            ),
            colors = TextFieldDefaults.outlinedTextFieldColors(
                unfocusedLabelColor = Color.Gray, // Color for the hint text when not focused
                unfocusedBorderColor = Color.Gray, // Color for the border when not focused
                focusedLabelColor = Color.Black, // Color for the hint text when focused
                focusedBorderColor = Color.Black, // Color for the border when focused
            )
        )
        Button(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .padding(start = 20.dp, end = 20.dp, top = 10.dp),
            onClick = onRequestEmailForForgottenPassword,
            colors = ButtonDefaults.buttonColors(Color(0xFF3797EF)),
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
    }
}
package hr.itrojnar.instagram.view.auth

import androidx.compose.animation.core.MutableTransitionState
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.itrojnar.instagram.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LogInScreen(modifier: Modifier = Modifier, onSignUpClick: () -> Unit, onForgotPasswordClick: () -> Unit) {
    val focusManager = LocalFocusManager.current

    // TODO Provjeriti
    val state = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }
    // TODO Privremeno
    var emailState by remember {
        mutableStateOf("")
    }
    var passwordState by remember {
        mutableStateOf("")
    }

    Box(modifier = Modifier
        .fillMaxSize()
        .background(Color.Transparent),
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Transparent)) {
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp, 0.dp)
                    .background(Color.Transparent),
                shape = RoundedCornerShape(10.dp),
            ) {
                OutlinedTextField(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(0.dp, 0.dp, 0.dp, 5.dp),
                    value = emailState,
                    onValueChange = { emailState = it },
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
                        .padding(0.dp, 5.dp, 0.dp, 5.dp),
                    value = emailState,
                    onValueChange = { emailState = it },
                    label = { Text(text = stringResource(R.string.password)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password,
                        imeAction = ImeAction.Done
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
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, end = 20.dp, bottom = 25.dp),
                horizontalArrangement = Arrangement.End
            ) {
                ClickableText(
                    text = AnnotatedString(stringResource(R.string.forgot_password)),
                    onClick = { onForgotPasswordClick() },
                    style = TextStyle(
                        color = Color(0xFF3797EF)
                    )
                )
            }
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .padding(start = 20.dp, end = 20.dp),
                onClick = { },
                colors = ButtonDefaults.buttonColors(Color(0xFF3797EF)),
                shape = RoundedCornerShape(5.dp)
            ) {
                Text(
                    stringResource(R.string.log_in),
                    fontSize = 16.sp,
                    color = Color.White)
            }
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 40.dp)
                    .clickable { },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {

                Image(
                    painter = painterResource(id = R.drawable.google_icon),
                    contentDescription = stringResource(R.string.google_icon),
                    modifier = modifier.size(20.dp))

                Spacer(modifier = modifier.width(8.dp))

                Text(text = stringResource(R.string.log_in_with_google))
            }
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp)
                    .clickable { },
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center) {

                Image(
                    painter = painterResource(id = R.drawable.github_icon),
                    contentDescription = stringResource(R.string.github_icon),
                    modifier = modifier.size(20.dp))

                Spacer(modifier = modifier.width(8.dp))

                Text(text = stringResource(R.string.log_in_with_github))
            }
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 30.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {

                Spacer(
                    modifier = modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Color.Black.copy(alpha = 0.2f)))

                Spacer(modifier = modifier.width(8.dp))

                Text(
                    modifier = modifier.padding(horizontal = 8.dp),
                    text = stringResource(R.string.or),
                    color = Color.Black.copy(alpha = 0.4f))

                Spacer(modifier = modifier.width(8.dp))

                Spacer(
                    modifier = modifier
                        .weight(1f)
                        .height(1.dp)
                        .background(Color.Black.copy(alpha = 0.2f)))
            }
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically) {

                Text(
                    text = stringResource(R.string.don_t_have_an_account),
                    style = TextStyle(fontSize = 16.sp),
                    color = Color.Black.copy(alpha = 0.4f))

                Spacer(modifier = Modifier.width(4.dp))

                ClickableText(
                    text = AnnotatedString(stringResource(R.string.sign_up)),
                    style = TextStyle(
                        color = Color(0xFF3797EF),
                        fontSize = 16.sp
                    ),
                    onClick = {})
            }
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 16.dp),
                verticalArrangement = Arrangement.Bottom,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Thin line
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(1.dp)
                        .background(Color.Black.copy(alpha = 0.2f))
                )

                // "Instagram or Facebook" text, centered
                Text(
                    text = stringResource(R.string.instagram_or_facebook),
                    color = Color.Black.copy(alpha = 0.4f),
                    modifier = Modifier.padding(top = 30.dp),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}
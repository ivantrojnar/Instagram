package hr.itrojnar.instagram.view.auth

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.itrojnar.instagram.R

@Composable
fun AuthenticationScreen(
    authenticationState: AuthenticationState,
    onLogin: () -> Unit,
    onRegister: () -> Unit,
    onEmailChanged: (String) -> Unit,
    onPasswordChanged: (String) -> Unit,
    modifier: Modifier = Modifier) {

    var currentScreen by remember {
        mutableStateOf(AuthenticationScreenState.LogIn)
    }

    Box(modifier = modifier.fillMaxSize()) {
        Column(modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)) {
            Image(
                painter = painterResource(id = R.drawable.instagram_logo),
                contentDescription = stringResource(R.string.instagram_logo),
                contentScale = ContentScale.Fit,
                modifier = Modifier.padding(start = 95.dp, top = 110.dp, end = 95.dp, bottom = 25.dp)
            )
            Crossfade(targetState = currentScreen) { screen ->
                when (screen) {
                    AuthenticationScreenState.LogIn -> LogInScreen(
                        modifier = modifier,
                        onSignUpClick = { currentScreen = AuthenticationScreenState.SignUp },
                        onForgotPasswordClick = { currentScreen = AuthenticationScreenState.ForgotPassword },
                        onLogin = onLogin
                    )
                    AuthenticationScreenState.SignUp -> SignUpScreen(
                        modifier = modifier,
                        onLogInClick = { currentScreen = AuthenticationScreenState.LogIn },
                        onRegister = onRegister
                    )
                    AuthenticationScreenState.ForgotPassword -> ForgotPasswordScreen(
                        modifier = modifier,
                        onLogInClick = { currentScreen = AuthenticationScreenState.LogIn }
                    )
                }
            }
        }
    }
}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun LogInScreen(modifier: Modifier = Modifier, onSignUpClick: () -> Unit, onForgotPasswordClick: () -> Unit) {
//    val focusManager = LocalFocusManager.current
//
//    // TODO Provjeriti
//    val state = remember {
//        MutableTransitionState(false).apply {
//            targetState = true
//        }
//    }
//    // TODO Privremeno
//    var emailState by remember {
//        mutableStateOf("")
//    }
//    var passwordState by remember {
//        mutableStateOf("")
//    }
//
//    Box(modifier = Modifier
//        .fillMaxSize()
//        .background(Color.Transparent),
//    ) {
//        Column(
//            modifier = modifier
//                .fillMaxSize()
//                .background(Color.Transparent)) {
//            Surface(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(20.dp, 0.dp)
//                    .background(Color.Transparent),
//                shape = RoundedCornerShape(10.dp),
//            ) {
//                OutlinedTextField(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(0.dp, 0.dp, 0.dp, 5.dp),
//                    value = emailState,
//                    onValueChange = { emailState = it },
//                    label = { Text(text = stringResource(R.string.email)) },
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Email,
//                        imeAction = ImeAction.Next
//                    ),
//                    keyboardActions = KeyboardActions (
//                        onDone = { focusManager.clearFocus() }
//                    ),
//                    colors = TextFieldDefaults.outlinedTextFieldColors(
//                        unfocusedLabelColor = Color.Gray, // Color for the hint text when not focused
//                        unfocusedBorderColor = Color.Gray, // Color for the border when not focused
//                        focusedLabelColor = Color.Black, // Color for the hint text when focused
//                        focusedBorderColor = Color.Black, // Color for the border when focused
//                    )
//                )
//            }
//            Surface(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(start = 20.dp, top = 0.dp, end = 20.dp, bottom = 0.dp)
//                    .background(Color.Transparent),
//                shape = RoundedCornerShape(10.dp),
//            ) {
//                OutlinedTextField(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .padding(0.dp, 5.dp, 0.dp, 5.dp),
//                    value = emailState,
//                    onValueChange = { emailState = it },
//                    label = { Text(text = stringResource(R.string.password)) },
//                    keyboardOptions = KeyboardOptions(
//                        keyboardType = KeyboardType.Password,
//                        imeAction = ImeAction.Done
//                    ),
//                    keyboardActions = KeyboardActions (
//                        onDone = { focusManager.clearFocus() }
//                    ),
//                    colors = TextFieldDefaults.outlinedTextFieldColors(
//                        unfocusedLabelColor = Color.Gray, // Color for the hint text when not focused
//                        unfocusedBorderColor = Color.Gray, // Color for the border when not focused
//                        focusedLabelColor = Color.Black, // Color for the hint text when focused
//                        focusedBorderColor = Color.Black, // Color for the border when focused
//                    )
//                )
//            }
//            Row(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .padding(top = 10.dp, end = 20.dp, bottom = 25.dp),
//                horizontalArrangement = Arrangement.End
//            ) {
//                ClickableText(
//                    text = AnnotatedString(stringResource(R.string.forgot_password)),
//                    onClick = { onForgotPasswordClick() },
//                    style = TextStyle(
//                        color = Color(0xFF3797EF)
//                    )
//                )
//            }
//            Button(
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(50.dp)
//                    .padding(start = 20.dp, end = 20.dp),
//                onClick = { },
//                colors = ButtonDefaults.buttonColors(Color(0xFF3797EF)),
//                shape = RoundedCornerShape(5.dp)
//            ) {
//                Text(
//                    stringResource(R.string.log_in),
//                    fontSize = 16.sp,
//                    color = Color.White)
//            }
//            Row(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .padding(top = 40.dp)
//                    .clickable { },
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center) {
//
//                Image(
//                    painter = painterResource(id = R.drawable.google_icon),
//                    contentDescription = stringResource(R.string.google_icon),
//                    modifier = modifier.size(20.dp))
//
//                Spacer(modifier = modifier.width(8.dp))
//
//                Text(text = stringResource(R.string.log_in_with_google))
//            }
//            Row(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .padding(top = 10.dp)
//                    .clickable { },
//                verticalAlignment = Alignment.CenterVertically,
//                horizontalArrangement = Arrangement.Center) {
//
//                Image(
//                    painter = painterResource(id = R.drawable.github_icon),
//                    contentDescription = stringResource(R.string.github_icon),
//                    modifier = modifier.size(20.dp))
//
//                Spacer(modifier = modifier.width(8.dp))
//
//                Text(text = stringResource(R.string.log_in_with_github))
//            }
//            Row(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 20.dp, vertical = 30.dp),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically) {
//
//                Spacer(
//                    modifier = modifier
//                        .weight(1f)
//                        .height(1.dp)
//                        .background(Color.Black.copy(alpha = 0.2f)))
//
//                Spacer(modifier = modifier.width(8.dp))
//
//                Text(
//                    modifier = modifier.padding(horizontal = 8.dp),
//                    text = stringResource(R.string.or),
//                    color = Color.Black.copy(alpha = 0.4f))
//
//                Spacer(modifier = modifier.width(8.dp))
//
//                Spacer(
//                    modifier = modifier
//                        .weight(1f)
//                        .height(1.dp)
//                        .background(Color.Black.copy(alpha = 0.2f)))
//            }
//            Row(
//                modifier = modifier
//                    .fillMaxWidth()
//                    .padding(horizontal = 20.dp),
//                horizontalArrangement = Arrangement.Center,
//                verticalAlignment = Alignment.CenterVertically) {
//
//                Text(
//                    text = stringResource(R.string.don_t_have_an_account),
//                    style = TextStyle(fontSize = 16.sp),
//                    color = Color.Black.copy(alpha = 0.4f))
//
//                Spacer(modifier = Modifier.width(4.dp))
//
//                ClickableText(
//                    text = AnnotatedString(stringResource(R.string.sign_up)),
//                    style = TextStyle(
//                        color = Color(0xFF3797EF),
//                        fontSize = 16.sp
//                    ),
//                    onClick = {})
//            }
//            Column(
//                modifier = Modifier
//                    .fillMaxSize()
//                    .padding(bottom = 16.dp),
//                verticalArrangement = Arrangement.Bottom,
//                horizontalAlignment = Alignment.CenterHorizontally
//            ) {
//                // Thin line
//                Spacer(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .height(1.dp)
//                        .background(Color.Black.copy(alpha = 0.2f))
//                )
//
//                // "Instagram or Facebook" text, centered
//                Text(
//                    text = stringResource(R.string.instagram_or_facebook),
//                    color = Color.Black.copy(alpha = 0.4f),
//                    modifier = Modifier.padding(top = 30.dp),
//                    textAlign = TextAlign.Center
//                )
//            }
//        }
//    }
//}

//@Composable
//fun SignUpScreen(modifier: Modifier, onLogInClick: () -> Unit) {
//    Button(
//        onClick = onLogInClick,
//    ) {
//        Text(text = "Switch to Log In")
//    }
//}

//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ForgotPasswordScreen(modifier: Modifier, onLogInClick: () -> Unit) {
//
//    val focusManager = LocalFocusManager.current
//
//    var forgotPasswordEmailState by remember {
//        mutableStateOf("")
//    }
//
//    Column(
//        modifier = modifier
//            .fillMaxSize()
//            .background(Color.Transparent)) {
//
//        LottieAnimationLoop(resId = R.raw.forgot_password_animation,
//            modifier
//                .fillMaxWidth()
//                .height(250.dp))
//
//        Text(
//            text = "Trouble logging in?",
//            fontWeight = FontWeight.Bold,
//            textAlign = TextAlign.Center,
//            modifier = modifier
//                .fillMaxWidth()
//                .padding(top = 16.dp)
//        )
//
//        Text(
//            text = stringResource(R.string.enter_your_email_and_we_ll_send_you_a_link_to_get_back_into_your_account),
//            textAlign = TextAlign.Center,
//            fontSize = 14.sp,
//            modifier = modifier
//                .fillMaxWidth()
//                .padding(top = 8.dp)
//        )
//
//        OutlinedTextField(
//            modifier = modifier
//                .fillMaxWidth()
//                .padding(horizontal = 20.dp, vertical = 10.dp),
//            value = forgotPasswordEmailState,
//            onValueChange = { forgotPasswordEmailState = it },
//            label = { Text(text = stringResource(R.string.email)) },
//            keyboardOptions = KeyboardOptions(
//                keyboardType = KeyboardType.Email,
//                imeAction = ImeAction.Next
//            ),
//            keyboardActions = KeyboardActions (
//                onDone = { focusManager.clearFocus() }
//            ),
//            colors = TextFieldDefaults.outlinedTextFieldColors(
//                unfocusedLabelColor = Color.Gray, // Color for the hint text when not focused
//                unfocusedBorderColor = Color.Gray, // Color for the border when not focused
//                focusedLabelColor = Color.Black, // Color for the hint text when focused
//                focusedBorderColor = Color.Black, // Color for the border when focused
//            )
//        )
//        Button(
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(60.dp)
//                .padding(start = 20.dp, end = 20.dp, top = 10.dp),
//            onClick = {},
//            colors = ButtonDefaults.buttonColors(Color(0xFF3797EF)),
//            shape = RoundedCornerShape(5.dp)
//        ) {
//            Text(
//                stringResource(R.string.next),
//                fontSize = 16.sp,
//                color = Color.White)
//        }
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(top = 40.dp),
//            horizontalArrangement = Arrangement.Center
//        ) {
//            ClickableText(
//                text = AnnotatedString(stringResource(R.string.back_to_log_in)),
//                onClick = { onLogInClick() },
//                style = TextStyle(
//                    color = Color(0xFF3797EF),
//                    fontSize = 16.sp
//                )
//            )
//        }
//    }
//}

//@Composable
//fun LottieAnimationLoop(@RawRes resId: Int, modifier: Modifier) {
//    val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(resId))
//    val progress by animateLottieCompositionAsState(composition, iterations = LottieConstants.IterateForever)
//
//    LottieAnimation(
//        composition,
//        progress = progress,
//        modifier = modifier
//    )
//}

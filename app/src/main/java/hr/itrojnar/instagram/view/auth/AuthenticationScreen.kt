package hr.itrojnar.instagram.view.auth

import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.itrojnar.instagram.R


@Preview
@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun AuthenticationScreen(modifier: Modifier = Modifier) {

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

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Transparent)) {
        Image(
            painter = painterResource(id = R.drawable.instagram_logo),
            contentDescription = "Instagram logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.padding(start = 95.dp, top = 110.dp, end = 95.dp, bottom = 25.dp)
        )
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
    }
}
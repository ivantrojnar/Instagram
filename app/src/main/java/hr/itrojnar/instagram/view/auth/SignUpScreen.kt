package hr.itrojnar.instagram.view.auth

import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun SignUpScreen(modifier: Modifier, onLogInClick: () -> Unit) {
    Button(
        onClick = onLogInClick,
    ) {
        Text(text = "Switch to Log In")
    }
}
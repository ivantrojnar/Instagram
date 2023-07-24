package hr.itrojnar.instagram.view.auth

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import hr.itrojnar.instagram.R

@Preview
@Composable
fun AuthenticationScreen() {
    Column(
        Modifier
            .fillMaxSize()
            .background(Color.White)) {
        Image(
            painter = painterResource(id = R.drawable.instagram_logo),
            contentDescription = "Instagram logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier.padding(50.dp)
        )
    }
}
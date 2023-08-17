package hr.itrojnar.instagram.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import hr.itrojnar.instagram.R

@Composable
fun CameraScreen(navController: NavHostController) {
    // ... other UI elements

    Icon(
        painter = painterResource(id = R.drawable.ic_arrow_back),
        contentDescription = "Go back",
        modifier = Modifier
            .padding(16.dp)
            .size(24.dp)
            .clickable { navController.popBackStack() }
    )
}
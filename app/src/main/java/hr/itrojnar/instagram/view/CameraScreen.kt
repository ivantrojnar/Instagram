package hr.itrojnar.instagram.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.viewmodel.CameraViewModel

@Composable
fun CameraScreen(navController: NavHostController) {

    val viewModel: CameraViewModel = viewModel()

    Box(modifier = Modifier.fillMaxSize()) {

        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = "Go back",
            modifier = Modifier
                .padding(16.dp)
                .size(24.dp)
                .clickable { navController.popBackStack() }
                .align(Alignment.TopStart)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 60.dp) // give space for the back icon
                .verticalScroll(rememberScrollState())
        ) {

            Box(
                modifier = Modifier
                    .fillMaxWidth() // Makes the box as wide as the screen
                    .aspectRatio(1f) // Sets the aspect ratio to be 1:1
                    .background(Color.Gray)
            ) {
                val image = viewModel.selectedImage
                if (image != null) {
                    Image(
                        bitmap = image.asImageBitmap(),
                        contentDescription = "Selected preview",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    IconButton(onClick = {

                    },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .size(24.dp)) {
                        Icon(Icons.Default.Edit, contentDescription = "Edit")
                    }

                } else {
                    IconButton(onClick = {

                    },
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .padding(16.dp)
                            .size(24.dp)) {
                        Icon(Icons.Default.CameraAlt, contentDescription = "Camera")
                    }
                }
            }

        }
    }
}
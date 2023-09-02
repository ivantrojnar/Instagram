package hr.itrojnar.instagram.view.utility

import android.net.Uri
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import hr.itrojnar.instagram.R

@Composable
fun ProfileImage(imageUri: Uri?, onImageClick: () -> Unit) {
    val image: Painter = imageUri?.let {
        rememberImagePainter(data = it)
    } ?: painterResource(id = R.drawable.default_profile_picture)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(120.dp), horizontalArrangement = Arrangement.Center
    ) {
        Box(modifier = Modifier
            .size(120.dp)
            .clip(CircleShape)
            .clickable { onImageClick() }) {
            Image(
                painter = image,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
        }
    }
}
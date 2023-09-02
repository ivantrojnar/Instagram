package hr.itrojnar.instagram.view.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.util.LottieAnimation

@Composable
fun CircleImage(imageUrl: String?, modifier: Modifier = Modifier, outerSize: Int = 95, innerSize: Int = 85) {
    Box(
        modifier = modifier.size(outerSize.dp),
        contentAlignment = Alignment.Center
    ) {
        // 1. The outer grayish ring
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
                .border(2.dp, Color.LightGray, CircleShape)
        )

        // 2. The round padding (by setting a smaller size to the Box)
        Box(
            modifier = Modifier
                .size(innerSize.dp)
                .align(Alignment.Center)
                .clip(CircleShape)
        ) {
            if (imageUrl != null) {
                Image(
                    painter = rememberImagePainter(data = imageUrl, builder = {
                        LottieAnimation(
                            resId = R.raw.loading_animation,
                            modifier = Modifier.fillMaxSize()
                        )
                        crossfade(true)
                    }),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.onSurface.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null
                    )
                }
            }
        }
    }
}
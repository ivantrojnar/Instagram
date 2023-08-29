package hr.itrojnar.instagram.view.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.model.User
import hr.itrojnar.instagram.util.instagramGradientColors
import hr.itrojnar.instagram.util.loadPicture

@Composable
fun DrawerHeader(user: User) {

    val gradient = Brush.linearGradient(colors = instagramGradientColors)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(brush = gradient)
            .padding(16.dp)
    ) {
        val image = loadPicture(url = user.profilePictureUrl!!, defaultImage = R.drawable.default_profile_picture).value
        image?.let { img ->
            Image(
                bitmap = img.asImageBitmap(),
                contentDescription = stringResource(R.string.user_profile_picture),
                modifier = Modifier
                    .size(100.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = user.fullName,
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = user.email,
            fontSize = 16.sp,
            color = Color.White
        )

        Spacer(modifier = Modifier.height(8.dp))
    }
}
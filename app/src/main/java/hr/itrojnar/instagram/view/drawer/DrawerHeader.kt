package hr.itrojnar.instagram.view.drawer

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.User
import hr.itrojnar.instagram.util.loadPicture

@Composable
fun DrawerHeader(user: User) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        val image = loadPicture(url = user.profilePictureUrl!!, defaultImage = R.drawable.default_profile_picture).value
        image?.let { img ->
            Image(
                bitmap = img.asImageBitmap(),
                contentDescription = "User Profile Picture",
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = user.fullName,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = user.email,
            fontSize = 14.sp,
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(8.dp))

        Divider(color = Color.Gray)
    }
}
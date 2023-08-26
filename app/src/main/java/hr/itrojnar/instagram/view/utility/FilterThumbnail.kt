package hr.itrojnar.instagram.view.utility

import android.graphics.Bitmap
import androidx.compose.ui.graphics.Color
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.load.Transformation
import hr.itrojnar.instagram.model.ImageFilter

@Composable
fun FilterThumbnail(
    filter: ImageFilter,
    onFilterSelected: (Transformation<Bitmap>) -> Unit
) {
    Box(modifier = Modifier.padding(4.dp)) {
        Text(
            text = filter.name,
            modifier = Modifier.align(Alignment.Center)
                .background(Color.Black.copy(0.7f))
                .padding(2.dp)
                .clickable { onFilterSelected(filter.transformation) },
            color = Color.White,
            fontSize = 12.sp
        )
    }
}
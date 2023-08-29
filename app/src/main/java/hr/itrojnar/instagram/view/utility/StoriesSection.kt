package hr.itrojnar.instagram.view.utility

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import hr.itrojnar.instagram.util.instagramGradientColors

@Composable
fun StoriesSection() {

    val stories = listOf(
        Pair("Šumaher", "https://cdn.hashnode.com/res/hashnode/image/upload/v1637410748416/jIQXSpXgq.jpeg"),
        Pair("Senna", "https://firebasestorage.googleapis.com/v0/b/instagram-c212b.appspot.com/o/profile_images%2FScreenshot%202023-08-29%20174617.png?alt=media&token=101d672c-b185-4109-8d01-604a754b3ca9"),
        Pair("CR7", "https://assets.manutd.com/AssetPicker/images/0/0/10/126/687707/Legends-Profile_Cristiano-Ronaldo1523460877263.jpg"),
        Pair("Messi", "https://www.reuters.com/resizer/MDGS1iPYUhyrw7J057g9snNYu_Y=/1200x1500/smart/filters:quality(80)/cloudfront-us-east-2.images.arcpublishing.com/reuters/KWFB4SNZIVMBZBMO5FCAAIMEOU.jpg"),
        Pair("BMW M5", "https://assets-eu-01.kc-usercontent.com/3b3d460e-c5ae-0195-6b86-3ac7fb9d52db/9a743954-daa7-4f3f-9b28-d7c355a0e11d/BMW%20M5%20%289%29.jpg?width=800&fm=jpg&auto=format"),
        Pair("Audi R8", "https://cdn.motor1.com/images/mgl/vxoJ0Y/s1/4x3/2023-audi-r8-v10-gt-rwd.webp"),
        Pair("Bugatti Chiron", "https://images.wsj.net/im-325492?width=1280&size=1.77777778")
    )

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(count = stories.size) { index ->
            val story = stories[index]
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .border(3.dp, Brush.horizontalGradient(instagramGradientColors), CircleShape)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                ) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(6.dp)
                            .clip(CircleShape)
                            .background(Color.White)
                    ) {
                        Image(
                            painter = rememberImagePainter(data = story.second),
                            contentDescription = "${story.first}'s story",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = story.first, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}
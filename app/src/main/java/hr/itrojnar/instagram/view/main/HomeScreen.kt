@file:OptIn(ExperimentalMaterial3Api::class)

package hr.itrojnar.instagram.view.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.rememberImagePainter
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.model.Post
import hr.itrojnar.instagram.util.formatDate
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@OptIn(ExperimentalPagingApi::class)
@Composable
fun HomeScreen(postsState: PostsState) {
    val posts = postsState.posts.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        item {

        }
        items(
            count = posts.itemCount,
            key = posts.itemKey { it.postId }
        ) { index ->  
            val post = posts[index]
            if (post != null) {
                PostItem(post = post)
            }
        }
    }
}

@Composable
fun PostItem1(modifier: Modifier = Modifier, post: Post, /* Other parameters or callbacks */) {
    // Define the UI representation of a single post item here
    Card(modifier = modifier.padding(8.dp)) {
        Text(text = post.postDescription)  // Example of displaying the post title
        // Add other post details as required
    }
}

@Composable
fun PostItem(modifier: Modifier = Modifier, post: Post) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val postDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(post.postDate) ?: Date()
    val formattedDate = formatDate(postDate, currentYear)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp)
    ) {
        // User info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Image(
                    painter = rememberImagePainter(data = post.userProfileImageUrl),
                    contentDescription = "User profile image",
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                )
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(text = post.userName, fontWeight = FontWeight.Bold)
                        Image(
                            painter = painterResource(id = R.drawable.instagram_verified),
                            contentDescription = "Verified Icon",
                            modifier = Modifier.size(16.dp)  // Adjust the size as needed
                        )
                    }
                    Text(
                        text = post.postAddress,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
            Icon(imageVector = Icons.Default.MoreHoriz, contentDescription = "Options icon")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Post Image
        Image(
            painter = rememberImagePainter(data = post.postImageUrl),
            contentDescription = "Post image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Icons row (like, comment, send)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Favorite, contentDescription = "Like icon")
            Icon(imageVector = Icons.Default.ChatBubbleOutline, contentDescription = "Comment icon")
            Icon(imageVector = Icons.Default.Send, contentDescription = "Direct message icon")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Post description
        Text(text = post.postDescription, modifier = Modifier.padding(horizontal = 8.dp))
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = post.postDate, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 8.dp))
    }
}






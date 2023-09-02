package hr.itrojnar.instagram.view.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import hr.itrojnar.instagram.model.Post

@Composable
fun UserPostsGrid(posts: List<Post>, setCurrentScreen: (String) -> Unit, setSelectedPost: (Post) -> Unit) {
    val padding = 0.5.dp

    LazyVerticalGrid(
        columns = GridCells.Fixed(3),
        modifier = Modifier.padding(bottom = 57.dp)
    ) {
        items(posts.size) { index ->
            val post = posts[index]
            val modifier = when (index % 3) {
                0 -> Modifier.padding(end = padding, top = padding, bottom = padding)  // Leftmost
                1 -> Modifier.padding(start = padding, end = padding, top = padding, bottom = padding)  // Middle
                2 -> Modifier.padding(start = padding, top = padding, bottom = padding)  // Rightmost
                else -> Modifier  // Should not be reached
            }

            Image(
                painter = rememberImagePainter(data = post.postImageUrl),
                contentDescription = "Post Image",
                modifier = modifier
                    .aspectRatio(1f)
                    .clickable {
                        setCurrentScreen("PostDetails")
                        setSelectedPost(post)
                    },
                contentScale = ContentScale.Crop
            )
        }
    }
}
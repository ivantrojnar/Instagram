@file:OptIn(ExperimentalMaterial3Api::class)

package hr.itrojnar.instagram.view.main

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import hr.itrojnar.instagram.model.Post

@OptIn(ExperimentalPagingApi::class)
@Composable
fun PostsScreen(postsState: PostsState) {
    val posts = postsState.posts.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(16.dp)
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
fun PostItem(modifier: Modifier = Modifier, post: Post, /* Other parameters or callbacks */) {
    // Define the UI representation of a single post item here
    Card(modifier = modifier.padding(8.dp)) {
        Text(text = post.userName)  // Example of displaying the post title
        // Add other post details as required
    }
}
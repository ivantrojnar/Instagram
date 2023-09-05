package hr.itrojnar.instagram.view.main

import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import hr.itrojnar.instagram.view.utility.PostItem
import hr.itrojnar.instagram.view.utility.StoriesSection

@OptIn(ExperimentalPagingApi::class)
@Composable
fun HomeScreen(postsState: PostsState) {

    val darkTheme = isSystemInDarkTheme()
    val backgroundColor = if (darkTheme) Color.Black else Color.Transparent
    val bottomBorderColor = if (darkTheme) Color(0xFF232323) else Color(0xFFCCCCCC)

    val posts = postsState.posts.collectAsLazyPagingItems()

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(top = 5.dp),
        contentPadding = PaddingValues(0.dp)
    ) {
        item {Spacer(modifier = Modifier.height(60.dp)) }

        item { StoriesSection() }

        item { Divider(
            color = bottomBorderColor,
            modifier = Modifier.fillMaxWidth()
        ) }

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
package hr.itrojnar.instagram.view.profile

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hr.itrojnar.instagram.model.Story

@Composable
fun UserStories(stories: List<Story>) {
    LazyRow (modifier = Modifier.padding(start = 5.dp)) {
        item {
            NewStoryItem()
        }
        items(stories.size) { index ->
            StoryItem(stories[index])
        }
    }
}
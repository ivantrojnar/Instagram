package hr.itrojnar.instagram.view.profile

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import hr.itrojnar.instagram.model.Story

@Composable
fun StoryItem(story: Story) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        CircleImage(imageUrl = story.imageUrl, modifier = Modifier.size(69.dp), outerSize = 65, innerSize = 60)
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = story.label, modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}
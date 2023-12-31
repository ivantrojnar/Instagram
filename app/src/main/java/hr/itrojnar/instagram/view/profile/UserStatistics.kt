package hr.itrojnar.instagram.view.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import hr.itrojnar.instagram.R

@Composable
fun UserStatistics(posts: Int, followers: Int, following: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp)
    ) {
        // Posts
        StatisticItem(number = posts, label = stringResource(R.string.posts))

        // Followers
        StatisticItem(number = followers, label = stringResource(R.string.followers))

        // Following
        StatisticItem(number = following, label = stringResource(R.string.following))
    }
}
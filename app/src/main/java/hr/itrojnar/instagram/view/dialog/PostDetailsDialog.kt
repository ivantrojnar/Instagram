package hr.itrojnar.instagram.view.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import coil.compose.rememberImagePainter
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.model.Post
import hr.itrojnar.instagram.util.LottieAnimation
import hr.itrojnar.instagram.util.formatDate
import hr.itrojnar.instagram.view.utility.LikedBySection
import hr.itrojnar.instagram.view.utility.StyledDescription
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

@Composable
fun PostDetailDialog(post: Post, onDismiss: () -> Unit) {

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp)),
            elevation = 16.dp
        ) {
            PostDialogContent(post)
        }
    }
}

@Composable
fun PostDialogContent(post: Post) {

    val darkTheme = isSystemInDarkTheme()
    val backgroundColor = if (darkTheme) Color.Black else Color.Transparent

    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val postDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(post.postDate) ?: Date()
    val formattedDate = formatDate(postDate, currentYear)

    val zoomState = rememberZoomState()

    val fakeUsernames = listOf("John Doe", "Milica Krmpotic", "Pero Peric")
    val randomLikedBy = remember { fakeUsernames.random() }

    Column(
        modifier = Modifier.fillMaxWidth().background(backgroundColor)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberImagePainter(data = post.userProfileImageUrl),
                contentDescription = stringResource(R.string.user_profile_image),
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 8.dp, end = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(text = post.userName, fontWeight = FontWeight.Bold)
                    Image(
                        painter = painterResource(id = R.drawable.instagram_verified),
                        contentDescription = stringResource(R.string.verified_icon),
                        modifier = Modifier.size(16.dp)
                    )
                }
                Text(
                    text = post.postAddress,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberImagePainter(data = post.postImageUrl,
                    builder = {
                        LottieAnimation(
                            resId = R.raw.loading_animation, modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                        )
                        crossfade(true)
                    }),
                contentDescription = stringResource(R.string.post_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .zoomable(zoomState)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Outlined.FavoriteBorder,
                    contentDescription = stringResource(R.string.like_icon),
                    modifier = Modifier.size(30.dp)
                )
                Icon(
                    imageVector = Icons.Outlined.ChatBubbleOutline,
                    contentDescription = stringResource(R.string.comment_icon),
                    modifier = Modifier.size(28.dp)
                )
                Icon(
                    imageVector = Icons.Outlined.Send,
                    contentDescription = stringResource(R.string.direct_message_icon),
                    modifier = Modifier.size(28.dp)
                )
            }

            Icon(
                imageVector = Icons.Outlined.BookmarkBorder,
                contentDescription = stringResource(R.string.bookmark_icon),
                modifier = Modifier.size(28.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        LikedBySection(likedByUser = randomLikedBy)

        StyledDescription(userName = post.userName, postDescription = post.postDescription)

        Text(
            text = formattedDate,
            style = MaterialTheme.typography.bodyLarge,
            modifier = Modifier.padding(horizontal = 8.dp),
            color = Color.Gray
        )

        Spacer(modifier = Modifier.height(15.dp))
    }
}
package hr.itrojnar.instagram.view.profile

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import coil.compose.rememberImagePainter
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.model.Post
import hr.itrojnar.instagram.util.formatDate
import hr.itrojnar.instagram.view.utility.LikedBySection
import hr.itrojnar.instagram.view.utility.StyledDescription
import hr.itrojnar.instagram.viewmodel.ProfileViewModel
import net.engawapg.lib.zoomable.rememberZoomState
import net.engawapg.lib.zoomable.zoomable
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt

@Composable
fun PostDetailsScreen(modifier: Modifier = Modifier, post: Post, onBackClick: (String) -> Unit, profileViewModel: ProfileViewModel) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val postDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(post.postDate) ?: Date()
    val formattedDate = formatDate(postDate, currentYear)

    var showMenu by remember { mutableStateOf(false) }
    val iconPosition = remember { mutableStateOf(IntOffset(0, 0)) }
    val iconSize = remember { mutableStateOf(IntSize(0, 0)) }

    val fakeUsernames = listOf("John Doe", "Milica Krmpotic", "Pero Peric")
    val randomLikedBy = remember { fakeUsernames.random() }

    val instagramGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFC13584),
            Color(0xFFD1913C),
            Color(0xFFE95950),
            Color(0xFF89216B)
        )
    )

    val zoomState = rememberZoomState()

    var showDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = R.drawable.ic_arrow_back),
                contentDescription = stringResource(R.string.back_arrow),
                modifier = Modifier.size(24.dp).clickable { onBackClick("Profile") }
            )

            Icon(
                painter = painterResource(id = R.drawable.instagram_logo),
                contentDescription = "Instagram Logo",
                modifier = Modifier.width(140.dp)
            )

            Spacer(modifier = Modifier.size(24.dp))
        }

        Spacer(modifier = Modifier.height(1.dp).fillMaxWidth().background(Color.LightGray))

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

            Box(
                modifier = Modifier
                    .clickable { showMenu = !showMenu }
                    .onGloballyPositioned { coordinates ->
                        iconPosition.value = IntOffset(
                            coordinates.positionInParent().x.roundToInt(),
                            coordinates.positionInParent().y.roundToInt()
                        )
                        iconSize.value = coordinates.size
                    }
            ) {
                Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options icon")
            }

            val xOffset = with(LocalDensity.current) { iconPosition.value.x.toDp() }
            val yOffset = with(LocalDensity.current) { (iconPosition.value.y - 50).toDp() }

            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false },
                offset = DpOffset(xOffset, yOffset),
                modifier = Modifier
                    .border(
                        width = 3.dp,
                        brush = instagramGradient,
                        shape = MaterialTheme.shapes.small
                    )
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = MaterialTheme.shapes.small
                    )
                    .padding(2.dp)
            ) {
                DropdownMenuItem(onClick = {
                    showMenu = false
                    showDialog = true
                }, text = {
                    Text(text = stringResource(R.string.delete))
                })
                DropdownMenuItem(onClick = {
                    showMenu = false
                }, text = {
                    Text(text = stringResource(R.string.cancel))
                })
            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberImagePainter(data = post.postImageUrl),
                contentDescription = stringResource(R.string.post_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .zoomable(zoomState)
                    .zIndex(1000f)
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
                modifier = Modifier.weight(1f), // assign equal weight
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = stringResource(
                    R.string.like_icon), modifier = Modifier.size(30.dp))
                Icon(imageVector = Icons.Outlined.ChatBubbleOutline, contentDescription = stringResource(
                    R.string.comment_icon), modifier = Modifier.size(28.dp))
                Icon(imageVector = Icons.Outlined.Send, contentDescription = stringResource(R.string.direct_message_icon), modifier = Modifier.size(28.dp))
            }

            Box(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
                Box(
                    modifier = Modifier
                        .size(6.dp)
                        .background(Color(0xFF45B8F0), shape = CircleShape)
                        .align(Alignment.CenterStart)
                )
            }

            Icon(imageVector = Icons.Outlined.BookmarkBorder, contentDescription = stringResource(R.string.bookmark_icon), modifier = Modifier.size(28.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        LikedBySection(likedByUser = randomLikedBy)

        StyledDescription(userName = post.userName, postDescription = post.postDescription)

        Text(text = formattedDate, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 8.dp), color = Color.Gray)

        Spacer(modifier = Modifier.height(15.dp))

        if (showDialog) {
            AlertDialog(
                onDismissRequest = {
                    showDialog = false
                },
                title = {
                    Text(text = stringResource(R.string.confirm_delete_title))
                },
                text = {
                    Text(text = stringResource(R.string.confirm_delete_message))
                },
                confirmButton = {
                    TextButton(onClick = {
                        showDialog = false
                        profileViewModel.deletePost(postId = post.postId)
                        onBackClick("Profile")
                    }) {
                        Text(
                            text = stringResource(R.string.confirm),
                            color = Color.Red
                        )
                    }
                },
                dismissButton = {
                    TextButton(onClick = {
                        showDialog = false
                    }) {
                        Text(text = stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}
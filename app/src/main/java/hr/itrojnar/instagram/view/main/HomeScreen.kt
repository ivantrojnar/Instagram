@file:OptIn(ExperimentalMaterial3Api::class)

package hr.itrojnar.instagram.view.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChatBubbleOutline
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MoreHoriz
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.BookmarkAdd
import androidx.compose.material.icons.outlined.BookmarkBorder
import androidx.compose.material.icons.outlined.Chat
import androidx.compose.material.icons.outlined.ChatBubble
import androidx.compose.material.icons.outlined.ChatBubbleOutline
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
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
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import coil.compose.rememberImagePainter
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.model.Post
import hr.itrojnar.instagram.util.formatDate
import hr.itrojnar.instagram.util.instagramGradient
import hr.itrojnar.instagram.view.utility.StyledDescription
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import kotlin.math.roundToInt

@OptIn(ExperimentalPagingApi::class)
@Composable
fun HomeScreen(postsState: PostsState) {

    val posts = postsState.posts.collectAsLazyPagingItems()

    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        Spacer(modifier = Modifier.height(60.dp)) // Add some space between stories and posts

        // Stories section
        StoriesSection()

        Spacer(modifier = Modifier.height(16.dp)) // Add some space between stories and posts

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
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
}

@Composable
fun PostItem(modifier: Modifier = Modifier, post: Post) {
    val currentYear = Calendar.getInstance().get(Calendar.YEAR)
    val postDate = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(post.postDate) ?: Date()
    val formattedDate = formatDate(postDate, currentYear)

    // State for DropdownMenu
    var showMenu by remember { mutableStateOf(false) }
    val iconPosition = remember { mutableStateOf(IntOffset(0, 0)) }
    val iconSize = remember { mutableStateOf(IntSize(0, 0)) }

    val instagramGradient = Brush.linearGradient(
        colors = listOf(
            Color(0xFFC13584),
            Color(0xFFD1913C),
            Color(0xFFE95950),
            Color(0xFF89216B)
        )
    )

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
                    // Handle item click
                    showMenu = false
                }, text = {
                    Text(text = "Show profile")
                })
                DropdownMenuItem(onClick = {
                    // Handle item click
                    showMenu = false
                }, text = {
                    Text(text = "Cancel")
                })
            }

//            Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options icon", modifier = Modifier.clickable { showMenu = !showMenu })
//
//            DropdownMenu(
//                expanded = showMenu,
//                onDismissRequest = { showMenu = false },
//                offset = DpOffset(0.dp, 10.dp)  // offset to position it right below the three-dot icon
//            ) {
//                DropdownMenuItem(onClick = {
//                    // Handle item click
//                    showMenu = false
//                },
//                    text = {
//                        Text(text = "Option 1")
//                    })
//            }
        }

        Spacer(modifier = Modifier.height(2.dp))

        // Post Image
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = rememberImagePainter(data = post.postImageUrl),
                contentDescription = "Post image",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)  // This will ensure a square shape
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Icons row (like, comment, send)
//        Row(
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(horizontal = 8.dp),
//            horizontalArrangement = Arrangement.spacedBy(16.dp)
//        ) {
//            Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "Like icon", modifier = Modifier.size(30.dp))
//            Icon(imageVector = Icons.Outlined.ChatBubbleOutline, contentDescription = "Comment icon", modifier = Modifier.size(28.dp))
//            Icon(imageVector = Icons.Outlined.Send, contentDescription = "Direct message icon", modifier = Modifier.size(28.dp))
//        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left icons
            Row(
                modifier = Modifier.weight(1f), // assign equal weight
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "Like icon", modifier = Modifier.size(30.dp))
                Icon(imageVector = Icons.Outlined.ChatBubbleOutline, contentDescription = "Comment icon", modifier = Modifier.size(28.dp))
                Icon(imageVector = Icons.Outlined.Send, contentDescription = "Direct message icon", modifier = Modifier.size(28.dp))
            }

            // Centered blue dot (also with equal weight, so it stays in the middle)
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

            // Right bookmark icon
            Icon(imageVector = Icons.Outlined.BookmarkBorder, contentDescription = "Bookmark icon", modifier = Modifier.size(28.dp))
        }

        Spacer(modifier = Modifier.height(8.dp))

        StyledDescription(userName = post.userName, postDescription = post.postDescription)

        Text(text = formattedDate, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(horizontal = 8.dp), color = Color.Gray)

        Spacer(modifier = Modifier.height(15.dp))
    }
}


@Composable
fun StoriesSection() {

    // Sample data for stories
    val stories = listOf(
        Pair("John Doe", "https://www.biowritingservice.com/wp-content/themes/tuborg/images/Executive%20Bio%20Sample%20Photo.png"),
        Pair("CR7", "https://assets.manutd.com/AssetPicker/images/0/0/10/126/687707/Legends-Profile_Cristiano-Ronaldo1523460877263.jpg"),
        Pair("Messi", "https://www.reuters.com/resizer/MDGS1iPYUhyrw7J057g9snNYu_Y=/1200x1500/smart/filters:quality(80)/cloudfront-us-east-2.images.arcpublishing.com/reuters/KWFB4SNZIVMBZBMO5FCAAIMEOU.jpg"),
        Pair("BMW M5", "https://assets-eu-01.kc-usercontent.com/3b3d460e-c5ae-0195-6b86-3ac7fb9d52db/9a743954-daa7-4f3f-9b28-d7c355a0e11d/BMW%20M5%20%289%29.jpg?width=800&fm=jpg&auto=format"),
        Pair("Audi R8", "https://cdn.motor1.com/images/mgl/vxoJ0Y/s1/4x3/2023-audi-r8-v10-gt-rwd.webp"),
        Pair("Bugatti Chiron", "https://images.wsj.net/im-325492?width=1280&size=1.77777778")
        // ... Add more as needed
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
                        .size(96.dp) // Increased the size to accommodate the spacing
                        .border(3.dp, Brush.horizontalGradient(instagramGradient), CircleShape)
                        .clip(CircleShape)
                        .background(MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                ) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .padding(6.dp) // This is the spacing
                            .clip(CircleShape)
                            .background(Color.White) // This is the white color spacing
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





package hr.itrojnar.instagram.view.main

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberImagePainter
import coil.transform.CircleCropTransformation
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.model.Post
import hr.itrojnar.instagram.model.Story
import hr.itrojnar.instagram.util.LottieAnimation
import hr.itrojnar.instagram.util.getRandomNumber
import hr.itrojnar.instagram.util.getUser
import hr.itrojnar.instagram.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {

    var currentScreen by remember { mutableStateOf("Profile") }
    var selectedPost by remember { mutableStateOf<Post?>(null) }

    val context = LocalContext.current

    val sharedPref = context.getSharedPreferences("user_details", Context.MODE_PRIVATE)
    val user = sharedPref.getUser()

    val userPosts = profileViewModel.posts
    val followers = remember { getRandomNumber() }
    val following = remember { getRandomNumber() }

    val sampleStories = listOf(
        Story("F1", "https://cdn-1.motorsport.com/images/amp/0oODaa70/s6/charles-leclerc-ferrari-f1-75-.jpg"),
        Story("WRC", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2a/Jari_Ketomaa_-_Rally_Finland_2009.JPG/1200px-Jari_Ketomaa_-_Rally_Finland_2009.JPG"),
        // Add more stories as needed
    )

    AnimatedVisibility(
        visible = currentScreen == "Profile",
        enter = if (currentScreen == "Profile") slideInHorizontally(initialOffsetX = { -it }) else slideInHorizontally(
            initialOffsetX = { it }),
        exit = if (currentScreen == "Profile") slideOutHorizontally(targetOffsetX = { it }) else slideOutHorizontally(
            targetOffsetX = { -it })
    ) {
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Profile Picture and Statistics
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp)
            ) {
                // Profile Picture
                CircleImage(imageUrl = user.profilePictureUrl)

                // User Statistics (Posts, Followers, Following)
                UserStatistics(userPosts.size, followers, following)
            }

            //User Details (Name and Email)
            Text(modifier = Modifier.padding(start = 10.dp), text = user.fullName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(modifier = Modifier.padding(start = 10.dp), text = user.email)

            // Edit Profile Button
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { /* TODO: Implement edit profile action */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 5.dp),
                colors = ButtonDefaults.buttonColors(Color.White),
                shape = RoundedCornerShape(7.dp),
                border = BorderStroke(1.dp, Color.LightGray)
            ) {
                Text("Edit Profile", color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            // User Stories
            Spacer(modifier = Modifier.height(0.dp))
            UserStories(sampleStories)

            // User Posts
            Divider(color = Color.LightGray, thickness = 1.dp)
            Spacer(modifier = Modifier.height(2.dp))
            UserPostsGrid(posts = userPosts, setCurrentScreen = { currentScreen = it }, setSelectedPost = { selectedPost = it})
        }
    }

    AnimatedVisibility(
        visible = currentScreen == "PostDetails",
        enter = if (currentScreen == "PostDetails") slideInHorizontally(initialOffsetX = { it }) else slideInHorizontally(
            initialOffsetX = { -it }),
        exit = if (currentScreen == "PostDetails") slideOutHorizontally(targetOffsetX = { -it }) else slideOutHorizontally(
            targetOffsetX = { it })
    ) {
        PostDetailsScreen(post = selectedPost!!)
    }
}

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

@Composable
fun CircleImage(imageUrl: String?, modifier: Modifier = Modifier, outerSize: Int = 95, innerSize: Int = 85) {
    Box(
        modifier = modifier.size(outerSize.dp),
        contentAlignment = Alignment.Center// Increase by 8dp to accommodate the ring and padding
    ) {
        // 1. The outer grayish ring
        Box(
            modifier = Modifier
                .matchParentSize()
                .clip(CircleShape)
                .border(2.dp, Color.LightGray, CircleShape)
        )

        // 2. The round padding (by setting a smaller size to the Box)
        Box(
            modifier = Modifier
                .size(innerSize.dp)
                .align(Alignment.Center)
                .clip(CircleShape)
        ) {
            if (imageUrl != null) {
                Image(
                    painter = rememberImagePainter(data = imageUrl, builder = {
                        LottieAnimation(
                            resId = R.raw.loading_animation,
                            modifier = Modifier.fillMaxSize()
                        )
                        crossfade(true)
                    }),
                    contentDescription = null,  // Consider providing a meaningful description
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                // Default icon if no imageUrl is provided
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colors.onSurface.copy(alpha = 0.2f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.Person,
                        contentDescription = null
                    )
                }
            }
        }
    }
}

@Composable
fun UserStatistics(posts: Int, followers: Int, following: Int) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,  // Centers items vertically
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 25.dp)
    ) {
        // Posts
        StatisticItem(number = posts, label = "Posts")

        // Followers
        StatisticItem(number = followers, label = "Followers")

        // Following
        StatisticItem(number = following, label = "Following")
    }
}

@Composable
fun StatisticItem(number: Int, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Text(
            text = number.toString(),
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp
        )
        Text(
            text = label,
            fontSize = 15.sp
        )
    }
}

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

@Composable
fun NewStoryItem() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(8.dp)
    ) {
        Box(
            modifier = Modifier
                .size(69.dp)
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, Color.Gray, CircleShape)
                .padding(4.dp),
            contentAlignment = Alignment.Center
        ) {
            Icon(Icons.Default.Add, contentDescription = "New Story", tint = Color.Black)
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = "New", modifier = Modifier.align(Alignment.CenterHorizontally))
    }
}

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

@Composable
fun PostDetailsScreen(post: Post) {
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = rememberImagePainter(data = post.postImageUrl),
            contentDescription = "Detailed Post Image",
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentScale = ContentScale.Crop
        )
        // Add more details like post description, comments, etc.
    }
}
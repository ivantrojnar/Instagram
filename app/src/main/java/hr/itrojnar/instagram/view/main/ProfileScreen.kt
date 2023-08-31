package hr.itrojnar.instagram.view.main

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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
import hr.itrojnar.instagram.util.LottieAnimation
import hr.itrojnar.instagram.util.getRandomNumber
import hr.itrojnar.instagram.util.getUser
import hr.itrojnar.instagram.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {

    val context = LocalContext.current

    val sharedPref = context.getSharedPreferences("user_details", Context.MODE_PRIVATE)
    val user = sharedPref.getUser()

    val userPosts = profileViewModel.posts

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        // Profile Picture and Statistics
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth().padding(10.dp)
        ) {
            // Profile Picture
            CircleImage(imageUrl = user.profilePictureUrl)

            // User Statistics (Posts, Followers, Following)
            UserStatistics(userPosts.size, getRandomNumber(), getRandomNumber())
        }

        //User Details (Name and Email)
        Text(modifier = Modifier.padding(start = 10.dp), text = user.fullName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
        Spacer(modifier = Modifier.height(4.dp))
        Text(modifier = Modifier.padding(start = 10.dp), text = user.email)

        // Edit Profile Button
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { /* TODO: Implement edit profile action */ }) {
            Text("Edit Profile")
        }

        // User Stories
        Spacer(modifier = Modifier.height(16.dp))
        UserStories()

        // User Posts
        Spacer(modifier = Modifier.height(16.dp))
        //UserPostsGrid(posts = userPosts)
    }
}

@Composable
fun CircleImage(imageUrl: String?, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier.size(95.dp),
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
                .size(85.dp)
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
        modifier = Modifier.fillMaxWidth().padding(start = 25.dp)
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
fun UserStories() {
    LazyRow {
        item {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape)
                    .background(Color.White)
                    .border(2.dp, Color.Gray, CircleShape)
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Add, contentDescription = "New Story", tint = Color.Black)
                Text(text = "New", modifier = Modifier.align(Alignment.BottomCenter))
            }
        }
    }
}
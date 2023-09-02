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
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.model.Post
import hr.itrojnar.instagram.model.Story
import hr.itrojnar.instagram.util.LottieAnimation
import hr.itrojnar.instagram.util.getRandomNumber
import hr.itrojnar.instagram.util.getUser
import hr.itrojnar.instagram.view.profile.CircleImage
import hr.itrojnar.instagram.view.profile.PostDetailsScreen
import hr.itrojnar.instagram.view.profile.UserPostsGrid
import hr.itrojnar.instagram.view.profile.UserStatistics
import hr.itrojnar.instagram.view.profile.UserStories
import hr.itrojnar.instagram.viewmodel.ProfileViewModel

@Composable
fun ProfileScreen(profileViewModel: ProfileViewModel) {

    var currentScreen by remember { mutableStateOf("Profile") }
    var selectedPost by remember { mutableStateOf<Post?>(null) }

    val context = LocalContext.current

    val sharedPref = context.getSharedPreferences("user_details", Context.MODE_PRIVATE)
    val user = sharedPref.getUser()

    val userPosts = profileViewModel.posts.value
    val followers = remember { getRandomNumber() }
    val following = remember { getRandomNumber() }

    val sampleStories = listOf(
        Story("F1", "https://cdn-1.motorsport.com/images/amp/0oODaa70/s6/charles-leclerc-ferrari-f1-75-.jpg"),
        Story("WRC", "https://upload.wikimedia.org/wikipedia/commons/thumb/2/2a/Jari_Ketomaa_-_Rally_Finland_2009.JPG/1200px-Jari_Ketomaa_-_Rally_Finland_2009.JPG"),
        Story("Sea", "https://climate.copernicus.eu/sites/default/files/styles/hero_image_extra_large_2x/public/2023-07/iStock-1267333118.jpg?itok=1udeNtwZ"),
        Story("Sport", "https://www.bfh.ch/dam/jcr:b69aa727-c5ea-46d6-abe3-4f323be68083/Studiengang_Bsc%20EHSM%20in%20Sports.jpg"),
        Story("Work", "https://ychef.files.bbci.co.uk/976x549/p0982k70.jpg"),
        Story("Friends", "https://upload.wikimedia.org/wikipedia/commons/f/f0/2018_IMG_8253_Helsinki%2C_Finland_%2840249531641%29_%28cropped%29.jpg"),
        Story("Lopovi", "https://tris.com.hr/wp-content/uploads/2020/10/hdz-logo.jpg"),
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
        PostDetailsScreen(post = selectedPost!!, onBackClick = { currentScreen = it }, profileViewModel = profileViewModel)
    }
}
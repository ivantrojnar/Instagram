package hr.itrojnar.instagram.view.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import hr.itrojnar.instagram.model.Post
import hr.itrojnar.instagram.util.instagramGradient
import hr.itrojnar.instagram.viewmodel.SearchPostsViewModel
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(searchPostsViewModel: SearchPostsViewModel) {

    var searchQuery by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("All") }

    val posts = searchPostsViewModel.posts
    val filteredPosts = filterPosts(posts, searchQuery, selectedOption)

    val optionsList = listOf("All", "User", "Location", "Description", "Date of post")

    val lazyListState = rememberLazyListState()

    // 1. Create a remembered mutable state map for visibility:
    val postVisibilityMap = remember { mutableStateOf(mutableMapOf<String, Boolean>()) }

    // 2. Update visibility map
    filteredPosts.forEach { post ->
        postVisibilityMap.value[post.postId] = true
    }
    posts.forEach { post ->
        if (post !in filteredPosts) {
            postVisibilityMap.value[post.postId] = false
        }
    }

    Column {
        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { value -> searchQuery = value },
            label = { Text("Search") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 8.dp, end = 8.dp), // This adds padding to the left and right
            singleLine = true,
            shape = RoundedCornerShape(30.dp), // This will round the corners of the TextField
            colors = TextFieldDefaults.textFieldColors( // Customize the colors
                containerColor = Color(0xFFEDEDED), // Very light gray
                textColor = Color.Black,
                cursorColor = Color.Black,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                focusedLabelColor = Color.Black,  // Set your desired color when focused
                unfocusedLabelColor = Color.Black
            ),
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search Icon") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Options
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(count = optionsList.size) { index ->
                val option = optionsList[index]
                FilterOption(
                    // Add start padding only for the first item
                    modifier = when (index) {
                        0 -> Modifier.padding(start = 8.dp)
                        optionsList.size - 1 -> Modifier.padding(end = 8.dp)
                        else -> Modifier
                    },
                    option,
                    isSelected = option == selectedOption,
                    onOptionSelected = {
                        selectedOption = it
                    }
                )
            }
        }

        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            items(
                count = filteredPosts.count(),
            ) { index ->
                val post = posts[index]
                val isVisible = postVisibilityMap.value[post.postId] ?: false
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(500)) + slideInVertically(initialOffsetY = { 50 }),
                    exit = fadeOut(animationSpec = tween(500)) + slideOutVertically(targetOffsetY = { -50 })
                ) {
                    PostItem(post = post)
                }
                //PostItem(post = post)
            }
        }

        LaunchedEffect(filteredPosts) {
            lazyListState.scrollToItem(0)
        }
    }
}

@Composable
fun filterPosts(posts: List<Post>, query: String, option: String): List<Post> {
    val monthMapEnglish = mapOf(
        "january" to 1, "february" to 2, "march" to 3, "april" to 4,
        "may" to 5, "june" to 6, "july" to 7, "august" to 8,
        "september" to 9, "october" to 10, "november" to 11, "december" to 12
    )

    val monthMapCroatian = mapOf(
        "siječanj" to 1, "veljača" to 2, "ožujak" to 3, "travanj" to 4,
        "svibanj" to 5, "lipanj" to 6, "srpanj" to 7, "kolovoz" to 8,
        "rujan" to 9, "listopad" to 10, "studeni" to 11, "prosinac" to 12
    )

    return posts.filter { post ->
        val postDateTime = LocalDateTime.parse(post.postDate, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))
        val postMonth = postDateTime.month.value
        val postYear = postDateTime.year

        when (option) {
            "All" -> {
                post.userName.contains(query, true) ||
                        post.postAddress.contains(query, true) ||
                        post.postDescription.contains(query, true) ||
                        monthMapEnglish.keys.any { it.startsWith(query, ignoreCase = true) && monthMapEnglish[it] == postMonth } ||
                        monthMapCroatian.keys.any { it.startsWith(query, ignoreCase = true) && monthMapCroatian[it] == postMonth } ||
                        query.contains(postYear.toString())
            }
            "User" -> post.userName.contains(query, true)
            "Location" -> post.postAddress.contains(query, true)
            "Description" -> post.postDescription.contains(query, true)
            "Date of post" -> {
                monthMapEnglish.keys.any { it.startsWith(query, ignoreCase = true) && monthMapEnglish[it] == postMonth } ||
                        monthMapCroatian.keys.any { it.startsWith(query, ignoreCase = true) && monthMapCroatian[it] == postMonth } ||
                        query.contains(postYear.toString())
            }
            else -> false
        }
    }
}

@Composable
fun FilterOption(
    modifier: Modifier = Modifier,
    option: String,
    isSelected: Boolean = false,
    onOptionSelected: (String) -> Unit,
) {
    // Check if instagramGradient has at least 2 colors, or else set a default list
    val safeInstagramGradient = if (instagramGradient.size >= 2) {
        instagramGradient
    } else {
        listOf(Color.Gray, Color.Gray) // Default to a single-color gradient for safety
    }

    val gradientBrush = Brush.horizontalGradient(safeInstagramGradient)

    // These animate*AsState functions allow properties to smoothly animate between their values
    val backgroundColor by animateColorAsState(
        if (isSelected) Color.Gray else Color(0xFFF0F0F0)
    )

    val textColor by animateColorAsState(
        if (isSelected) Color.White else Color.DarkGray
    )

    Text(
        text = option,
        modifier = modifier.run {
            if (isSelected) {
                this.background(brush = gradientBrush, shape = RoundedCornerShape(8.dp))
            } else {
                this.background(color = backgroundColor, shape = RoundedCornerShape(8.dp))
            }
        }
            .padding(horizontal = 8.dp, vertical = 2.dp)
            .clickable { onOptionSelected(option) }
            .padding(8.dp),
        style = TextStyle(
            color = textColor,
            fontSize = 14.sp, // Single font size
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    )
}
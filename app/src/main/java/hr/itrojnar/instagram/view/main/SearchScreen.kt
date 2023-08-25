package hr.itrojnar.instagram.view.main

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.paging.ExperimentalPagingApi
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import hr.itrojnar.instagram.model.Post
import hr.itrojnar.instagram.viewmodel.SearchPostsViewModel
import kotlinx.coroutines.launch
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(searchPostsViewModel: SearchPostsViewModel) {

    var searchQuery by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("All") }

    val posts = searchPostsViewModel.posts
    val filteredPosts = FilterPosts(posts, searchQuery, selectedOption)

    val optionsList = listOf("All", "User", "Location", "Description", "Date of post")

    Column {
        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = { value -> searchQuery = value },
            label = { Text("Search") },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Options
        LazyRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(count = optionsList.size) { index ->
                val option = optionsList[index]
                FilterOption(option, isSelected = option == selectedOption) {
                    selectedOption = it
                }
            }
        }

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp),
            contentPadding = PaddingValues(0.dp)
        ) {
            items(
                count = filteredPosts.count(),
            ) { index ->
                val post = filteredPosts[index]
                if (post != null) {
                    PostItem(post = post)
                }
            }
        }
    }
}

@Composable
fun FilterPosts(posts: List<Post>, query: String, option: String): List<Post> {
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
    option: String,
    isSelected: Boolean = false,
    onOptionSelected: (String) -> Unit
) {
    Text(
        text = option,
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .background(
                if (isSelected) Color.Gray else Color.Transparent,
                shape = RoundedCornerShape(8.dp)
            )
            .clickable { onOptionSelected(option) }
            .padding(8.dp),
        color = if (isSelected) Color.White else Color.Gray,
        style = TextStyle(fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
    )
}

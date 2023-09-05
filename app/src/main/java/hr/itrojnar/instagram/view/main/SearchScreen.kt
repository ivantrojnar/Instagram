package hr.itrojnar.instagram.view.main

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import hr.itrojnar.instagram.R
import hr.itrojnar.instagram.model.Post
import hr.itrojnar.instagram.view.camera.FilterOption
import hr.itrojnar.instagram.view.utility.PostItem
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

    val optionsList = listOf(
        stringResource(R.string.all),
        stringResource(R.string.user),
        stringResource(R.string.location),
        stringResource(R.string.description),
        stringResource(R.string.date_of_post)
    )

    val lazyListState = rememberLazyListState()

    val postVisibilityMap = remember { mutableStateOf(mutableMapOf<String, Boolean>()) }

    val focusManager = LocalFocusManager.current

    filteredPosts.forEach { post ->
        postVisibilityMap.value[post.postId] = true
    }
    posts.forEach { post ->
        if (post !in filteredPosts) {
            postVisibilityMap.value[post.postId] = false
        }
    }

    var searchBarVisible by remember { mutableStateOf(true) }

    var lastScrollOffset by remember { mutableStateOf(0f) }

    LaunchedEffect(lazyListState) {
        snapshotFlow { lazyListState.firstVisibleItemScrollOffset.toFloat() }
            .collect { scrollOffset ->
                searchBarVisible = scrollOffset <= lastScrollOffset
                lastScrollOffset = scrollOffset
            }
    }

    Column {
        AnimatedVisibility(
            visible = searchBarVisible,
            enter = slideInVertically(initialOffsetY = { -50 }) + fadeIn(),
            exit = slideOutVertically(targetOffsetY = { -50 }) + fadeOut()
        ) {
            Column {
                TextField(
                    value = searchQuery,
                    onValueChange = { value -> searchQuery = value },
                    label = { Text("Search") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, end = 8.dp),
                    singleLine = true,
                    shape = RoundedCornerShape(30.dp),
                    colors = TextFieldDefaults.textFieldColors(
                        containerColor = Color(0xFFEDEDED),
                        textColor = Color.Black,
                        cursorColor = Color.Black,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color.Black,
                        unfocusedLabelColor = Color.Black
                    ),
                    leadingIcon = { Icon(Icons.Default.Search, contentDescription = stringResource(R.string.search_icon)) },
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Text,
                        imeAction = ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = { focusManager.clearFocus() }
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(count = optionsList.size) { index ->
                        val option = optionsList[index]
                        FilterOption(
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
            }
        }

        LazyColumn(
            state = lazyListState,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 5.dp),
            contentPadding = PaddingValues(bottom = 85.dp)
        ) {
            items(
                count = filteredPosts.count(),
            ) { index ->
                val post = filteredPosts[index]
                val isVisible = postVisibilityMap.value[post.postId] ?: false
                AnimatedVisibility(
                    visible = isVisible,
                    enter = fadeIn(animationSpec = tween(500)) + slideInVertically(initialOffsetY = { 50 }),
                    exit = fadeOut(animationSpec = tween(500)) + slideOutVertically(targetOffsetY = { -50 })
                ) {
                    PostItem(post = post)
                }
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
            stringResource(id = R.string.all) -> {
                post.userName.contains(query, true) ||
                        post.postAddress.contains(query, true) ||
                        post.postDescription.contains(query, true) ||
                        monthMapEnglish.keys.any { it.startsWith(query, ignoreCase = true) && monthMapEnglish[it] == postMonth } ||
                        monthMapCroatian.keys.any { it.startsWith(query, ignoreCase = true) && monthMapCroatian[it] == postMonth } ||
                        query.contains(postYear.toString())
            }
            stringResource(id = R.string.user) -> post.userName.contains(query, true)
            stringResource(id = R.string.location) -> post.postAddress.contains(query, true)
            stringResource(id = R.string.description) -> post.postDescription.contains(query, true)
            stringResource(id = R.string.date_of_post) -> {
                monthMapEnglish.keys.any { it.startsWith(query, ignoreCase = true) && monthMapEnglish[it] == postMonth } ||
                        monthMapCroatian.keys.any { it.startsWith(query, ignoreCase = true) && monthMapCroatian[it] == postMonth } ||
                        query.contains(postYear.toString())
            }
            else -> false
        }
    }
}
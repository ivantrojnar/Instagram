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
import hr.itrojnar.instagram.viewmodel.SearchPostsViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagingApi::class)
@Composable
fun SearchScreen(searchPostsViewModel: SearchPostsViewModel) {

    val coroutineScope = rememberCoroutineScope()
    var searchQuery by remember { mutableStateOf("") }
    var selectedOption by remember { mutableStateOf("All") }

    val posts = searchPostsViewModel.posts

    val optionsList = listOf("All", "User", "Location", "Description")

    Column {
        // Search Bar
        TextField(
            value = searchQuery,
            onValueChange = {  },
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
                count = posts.count(),
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

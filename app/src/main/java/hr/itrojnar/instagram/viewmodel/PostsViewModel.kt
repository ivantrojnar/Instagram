package hr.itrojnar.instagram.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewModelScope
import androidx.paging.ExperimentalPagingApi
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.itrojnar.instagram.api.FirebasePostRepository
import hr.itrojnar.instagram.model.Post
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class PostsViewModel @Inject constructor(
    postRepository: FirebasePostRepository
) : ViewModel() {

    val posts = postRepository.getPosts()
}
package hr.itrojnar.instagram.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.ExperimentalPagingApi
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.itrojnar.instagram.api.FirebasePostRepository
import javax.inject.Inject

@OptIn(ExperimentalPagingApi::class)
@HiltViewModel
class PostsViewModel @Inject constructor(
    postRepository: FirebasePostRepository
) : ViewModel() {

    val posts = postRepository.getPosts()
}
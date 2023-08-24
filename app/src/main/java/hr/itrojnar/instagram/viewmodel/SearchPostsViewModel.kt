package hr.itrojnar.instagram.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.itrojnar.instagram.api.FirebasePostRepository
import javax.inject.Inject

@HiltViewModel
class SearchPostsViewModel @OptIn(ExperimentalPagingApi::class)
@Inject constructor(
    postRepository: FirebasePostRepository
) : ViewModel() {

    @OptIn(ExperimentalPagingApi::class)
    val posts = postRepository.getAllPosts()
}
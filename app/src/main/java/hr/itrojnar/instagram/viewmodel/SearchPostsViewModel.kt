package hr.itrojnar.instagram.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.ExperimentalPagingApi
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.itrojnar.instagram.api.FirebasePostRepository
import hr.itrojnar.instagram.api.PostRepository
import javax.inject.Inject

@HiltViewModel
class SearchPostsViewModel
@Inject constructor(
    postRepository: PostRepository
) : ViewModel() {

    val posts = postRepository.getAllPosts()
}
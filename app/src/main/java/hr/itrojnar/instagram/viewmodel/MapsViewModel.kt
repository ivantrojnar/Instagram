package hr.itrojnar.instagram.viewmodel

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.itrojnar.instagram.api.PostRepository
import javax.inject.Inject

@HiltViewModel
class MapsViewModel
@Inject constructor(
    postRepository: PostRepository
) : ViewModel() {

    val posts = postRepository.getAllPosts()
}
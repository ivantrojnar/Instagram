package hr.itrojnar.instagram.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.itrojnar.instagram.api.PostRepository
import hr.itrojnar.instagram.model.Post
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    private val postRepository: PostRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val userId: String = sharedPreferences.getString("firebaseUserId", "") ?: ""
    var posts: MutableState<List<Post>> = mutableStateOf(listOf())
    init {
        refreshPosts()
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            postRepository.deletePost(postId)
            refreshPosts()
        }
    }

    fun refreshPosts() {
        posts.value = postRepository.getAllUserPosts(userId)
    }
}
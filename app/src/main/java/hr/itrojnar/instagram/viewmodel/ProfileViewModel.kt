package hr.itrojnar.instagram.viewmodel

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.itrojnar.instagram.api.PostRepository
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel
@Inject constructor(
    postRepository: PostRepository,
    private val sharedPreferences: SharedPreferences
) : ViewModel() {

    private val userId: String = sharedPreferences.getString("firebaseUserId", "") ?: ""

    val posts = postRepository.getAllUserPosts(userId)
}
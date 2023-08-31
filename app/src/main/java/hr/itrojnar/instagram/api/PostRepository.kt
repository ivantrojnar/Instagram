package hr.itrojnar.instagram.api

import androidx.paging.PagingData
import com.google.firebase.firestore.DocumentReference
import hr.itrojnar.instagram.model.Post
import kotlinx.coroutines.flow.Flow

interface PostRepository {
    fun getPosts(): Flow<PagingData<Post>>
    suspend fun addNewPost(post: Post): Result<DocumentReference>
    fun invalidatePosts()
    fun getAllPosts(): List<Post>
    fun getAllUserPosts(userId: String): List<Post>
}
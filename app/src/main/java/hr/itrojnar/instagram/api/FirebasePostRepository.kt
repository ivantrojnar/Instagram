package hr.itrojnar.instagram.api

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import hr.itrojnar.instagram.dao.PostDao
import hr.itrojnar.instagram.db.PostDatabase
import hr.itrojnar.instagram.model.Post
import hr.itrojnar.instagram.paging.PostsRemoteMediator
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@ExperimentalPagingApi
class FirebasePostRepository @Inject constructor(
    private val postDatabase: PostDatabase
) : PostRepository {
    private val db = FirebaseFirestore.getInstance()
    private val postsCollection = db.collection("posts")

    override fun getPosts(): Flow<PagingData<Post>> {
        val pagingSource = { postDatabase.postDao().getAllPosts() }

        return Pager(
            config = PagingConfig(pageSize = Int.MAX_VALUE),
            remoteMediator = PostsRemoteMediator(
                postsCollection,
                postDatabase
            ),
            pagingSourceFactory = pagingSource
        ).flow
    }

    override suspend fun addNewPost(post: Post): Result<DocumentReference> {
        return try {
            val newPostMap = mapOf(
                "postId" to post.postId,
                "userId" to post.userId,
                "userName" to post.userName,
                "userProfileImageUrl" to post.userProfileImageUrl,
                "postImageUrl" to post.postImageUrl,
                "postAddress" to post.postAddress,
                "postLatitude" to post.postLatitude,
                "postLongitude" to post.postLongitude,
                "postDescription" to post.postDescription,
                "postDate" to post.postDate
            )
            val documentReference = postsCollection.add(newPostMap).await()
            Result.success(documentReference)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun invalidatePosts() {
        postDatabase.postDao().deletePosts()
    }

    override fun getAllPosts(): List<Post> = runBlocking {
        postDatabase.postDao().getAllPostsListFlow().first()
    }

    override fun getAllUserPosts(userId: String): List<Post> = runBlocking {
        postDatabase.postDao().getAllUserPostsListFlow(userId).first()
    }

    override fun deletePost(postId: String): Result<Unit> {
        val firebaseResult = try {
            runBlocking {
                postsCollection.document(postId).delete().await()
            }
            Result.success(Unit)
        } catch (e: Exception) {
            Log.e("DELETE", "Unable to delete post with ID: $postId due to ${e.message}")
            Result.failure(e)
        }
        if (firebaseResult.isSuccess) {
            runBlocking(Dispatchers.IO) {
                postDatabase.postDao().deletePostById(postId)
            }
        }
        return firebaseResult
    }
}
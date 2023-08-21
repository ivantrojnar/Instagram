package hr.itrojnar.instagram.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.google.firebase.firestore.CollectionReference
import hr.itrojnar.instagram.db.PostDatabase
import hr.itrojnar.instagram.model.Post
import kotlinx.coroutines.tasks.await

@ExperimentalPagingApi
class PostsRemoteMediator(
    private val firebaseCollection: CollectionReference,
    private val postDatabase: PostDatabase
) : RemoteMediator<Int, Post>() {

    private val postDao = postDatabase.postDao()

    override suspend fun load(loadType: LoadType, state: PagingState<Int, Post>): MediatorResult {
        return try {
            val currentPage = 1 // Since you mentioned every post has a value page set to 1

            val snapshot = firebaseCollection.get().await()
            val posts = snapshot.documents.map { document ->
                Post(
                    postId = document.id,
                    userId = document.getString("userId") ?: "",
                    userName = document.getString("userName") ?: "",
                    userProfileImageUrl = document.getString("userProfileImageUrl") ?: "",
                    postImageUrl = document.getString("postImageUrl") ?: "",
                    postAddress = document.getString("postAddress") ?: "",
                    postLatitude = document.getDouble("postLatitude") ?: 0.0,
                    postLongitude = document.getDouble("postLongitude") ?: 0.0,
                    postDescription = document.getString("postDescription") ?: "",
                    postDate = document.getString("postDate") ?: "",
                    page = currentPage
                )
            }

            val endOfPaginationReached = posts.isEmpty()

            postDatabase.withTransaction {
                if (loadType == LoadType.REFRESH) {
                    postDao.deletePosts()
                }
                postDao.insertPosts(posts)
            }

            MediatorResult.Success(endOfPaginationReached = endOfPaginationReached)
        } catch (e: Exception) {
            MediatorResult.Error(e)
        }
    }
}
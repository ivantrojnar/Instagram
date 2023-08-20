package hr.itrojnar.instagram.api

import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import hr.itrojnar.instagram.model.NewPost
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebasePostRepository @Inject constructor() : PostRepository {
    private val db = FirebaseFirestore.getInstance()
    private val postsCollection = db.collection("posts")

    override suspend fun addNewPost(newPost: NewPost): Result<DocumentReference> {
        return try {
            val newPostMap = mapOf(
                "postId" to newPost.postId,
                "userId" to newPost.userId,
                "userName" to newPost.userName,
                "userProfileImageUrl" to newPost.userProfileImageUrl,
                "postImageUrl" to newPost.postImageUrl,
                "postAddress" to newPost.postAddress,
                "postLatitude" to newPost.postLatitude,
                "postLongitude" to newPost.postLongitude,
                "postDescription" to newPost.postDescription,
                "postDate" to newPost.postDate
            )
            val documentReference = postsCollection.add(newPostMap).await()
            Result.success(documentReference)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
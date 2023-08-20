package hr.itrojnar.instagram.api

import com.google.firebase.firestore.DocumentReference
import hr.itrojnar.instagram.model.NewPost

interface PostRepository {
    suspend fun addNewPost(newPost: NewPost): Result<DocumentReference>
}
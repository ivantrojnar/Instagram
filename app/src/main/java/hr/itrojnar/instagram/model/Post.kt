package hr.itrojnar.instagram.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "posts_table")
data class Post(
    @PrimaryKey()
    val postId: String,
    val userId: String,
    val userName: String,
    val userProfileImageUrl: String,
    val postImageUrl: String,
    val postAddress: String,
    val postLatitude: Double,
    val postLongitude: Double,
    val postDescription: String,
    val postDate: String,
    val page: Int
)
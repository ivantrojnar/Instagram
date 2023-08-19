package hr.itrojnar.instagram.model

data class NewPost(
    val postId: String,
    val userId: String,
    val userName: String,
    val userProfileImageUrl: String,
    val postImageUrl: String,
    val postAddress: String,
    val postLatitude: Double,
    val postLongitude: Double,
    val postDescription: String,
    val postDate: String
)

data class SavedPost(
    val postId: String,
    val userId: String,
    val userName: String,
    val userProfileImageUrl: String,
    val postImageUrl: String,
    val postAddress: String,
    val postLatitude: Double,
    val postLongitude: Double,
    val postDescription: String,
    val postDate: String
)
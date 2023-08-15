package hr.itrojnar.instagram

import java.time.LocalDate

data class User(
    val firebaseUserId: String,
    val fullName: String,
    val email: String,
    val profilePictureUrl: String?,
    val subscriptionId: Int,
    val lastSignInDate: LocalDate,
    val mbUsedToday: Int,
    val numOfPicsUploadedToday: Int
)

object DummyData {
    val dummyUser = User(
        firebaseUserId = "12345",
        fullName = "John Doe",
        email = "john@example.com",
        profilePictureUrl = "https://firebasestorage.googleapis.com/v0/b/instagram-c212b.appspot.com/o/profile_images%2Ff9e6898f-d550-4565-ab12-945088a6967b.jpg?alt=media&token=e29c6db5-2587-45d9-9485-a832bc8db98c",
        subscriptionId = 1,
        lastSignInDate = LocalDate.now(),
        mbUsedToday = 200,
        numOfPicsUploadedToday = 5
    )
}
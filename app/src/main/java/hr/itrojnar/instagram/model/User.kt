package hr.itrojnar.instagram.model

import java.time.LocalDate

data class User(
    val firebaseUserId: String = "",
    val fullName: String = "",
    val email: String = "",
    val profilePictureUrl: String? = null,
    val subscriptionId: Int = 0,
    val lastSignInDate: String = LocalDate.now().toString(),
    val mbUsedToday: Int = 0,
    val numOfPicsUploadedToday: Int = 0
)

object DummyData {
    val dummyUser = User(
        firebaseUserId = "12345",
        fullName = "John Doe",
        email = "john@example.com",
        profilePictureUrl = "https://firebasestorage.googleapis.com/v0/b/instagram-c212b.appspot.com/o/profile_images%2Fa22b681f-4791-4ec4-a7e6-63ead4694055.jpg?alt=media&token=013fe1c4-8d45-4c1b-b16b-1b65f8893214",
        subscriptionId = 1,
        lastSignInDate = LocalDate.now().toString(),
        mbUsedToday = 200,
        numOfPicsUploadedToday = 5
    )
}
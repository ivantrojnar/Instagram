package hr.itrojnar.instagram

import java.time.LocalDate

data class User(
    val firebaseUserId: String,
    val fullName: String,
    val email: String,
    val profilePictureUrl: String?,
    val subscriptionId: Int,
    val lastSignInDate: LocalDate,  // timestamp representation
    val mbUsedToday: Int,
    val numOfPicsUploadedToday: Int
)

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

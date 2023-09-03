package hr.itrojnar.instagram.model

import java.time.LocalDate

data class User(
    val firebaseUserId: String = "",
    val fullName: String = "",
    val email: String = "",
    val profilePictureUrl: String? = null,
    val subscriptionId: Int = 0,
    val lastSignInDate: String = LocalDate.now().toString(),
    val mbUsedToday: Float = 0f,
    val numOfPicsUploadedToday: Int = 0
)
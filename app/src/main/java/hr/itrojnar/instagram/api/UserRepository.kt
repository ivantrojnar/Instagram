package hr.itrojnar.instagram.api

import hr.itrojnar.instagram.model.User

interface UserRepository {
    suspend fun getCurrentUserDetail(): User?
}
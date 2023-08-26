package hr.itrojnar.instagram.repository

import androidx.paging.ExperimentalPagingApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hr.itrojnar.instagram.api.FirebasePostRepository
import hr.itrojnar.instagram.api.FirebaseUserRepository
import hr.itrojnar.instagram.api.PostRepository
import hr.itrojnar.instagram.api.UserRepository
import hr.itrojnar.instagram.db.PostDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    @Singleton
    fun provideUserRepository(): UserRepository = FirebaseUserRepository()

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    fun provideFirebasePostRepository(postDatabase: PostDatabase): PostRepository {
        return FirebasePostRepository(postDatabase)
    }
}
package hr.itrojnar.instagram.repository

import androidx.paging.ExperimentalPagingApi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hr.itrojnar.instagram.api.FirebasePostRepository
import hr.itrojnar.instagram.db.PostDatabase

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideUserRepository(): UserRepository = UserRepository()

    @OptIn(ExperimentalPagingApi::class)
    @Provides
    fun provideFirebasePostRepository(postDatabase: PostDatabase): FirebasePostRepository {
        return FirebasePostRepository(postDatabase)
    }
}
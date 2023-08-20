package hr.itrojnar.instagram.repository

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import hr.itrojnar.instagram.api.FirebasePostRepository

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {

    @Provides
    fun provideUserRepository(): UserRepository = UserRepository()

    @Provides
    fun provideFirebasePostRepository(): FirebasePostRepository {
        return FirebasePostRepository()
    }
}
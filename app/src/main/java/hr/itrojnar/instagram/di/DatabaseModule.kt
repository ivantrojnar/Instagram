package hr.itrojnar.instagram.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import hr.itrojnar.instagram.db.PostDatabase
import javax.inject.Singleton

private const val POST_DATABASE = "post_database"

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun providePostDatabase(@ApplicationContext context: Context): PostDatabase {
        return Room.databaseBuilder(
            context,
            PostDatabase::class.java,
            POST_DATABASE
        ).build()
    }
}
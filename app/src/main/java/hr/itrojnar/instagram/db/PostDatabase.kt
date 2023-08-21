package hr.itrojnar.instagram.db

import androidx.room.Database
import androidx.room.RoomDatabase
import hr.itrojnar.instagram.dao.PostDao
import hr.itrojnar.instagram.model.Post

@Database(entities = [Post::class], version = 1, exportSchema = false)
abstract class PostDatabase : RoomDatabase() {
    abstract fun postDao(): PostDao
}
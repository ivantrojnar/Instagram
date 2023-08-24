package hr.itrojnar.instagram.dao

import androidx.lifecycle.LiveData
import androidx.paging.PagingSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import hr.itrojnar.instagram.model.Post
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts_table ORDER BY postDate DESC")
    fun getAllPosts(): PagingSource<Int, Post>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertPosts(posts: List<Post>)

    @Query("SELECT * FROM posts_table WHERE page = :page")
    fun getPostsByPage(page: Int): PagingSource<Int, Post>

    @Query("DELETE FROM posts_table")
    fun deletePosts(): Int

    @Query("SELECT * FROM posts_table ORDER BY postDate DESC")
    fun getAllPostsList(): List<Post>

    @Query("SELECT * FROM posts_table ORDER BY postDate DESC")
    fun getAllPostsListFlow(): Flow<List<Post>>

    @Query("SELECT COUNT(*) FROM posts_table")
    fun countPosts(): Int
}
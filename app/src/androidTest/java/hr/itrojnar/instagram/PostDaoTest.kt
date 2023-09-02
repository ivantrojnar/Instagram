package hr.itrojnar.instagram

import androidx.room.Room
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import hr.itrojnar.instagram.dao.PostDao
import hr.itrojnar.instagram.db.PostDatabase
import hr.itrojnar.instagram.model.Post
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.hasItem
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class PostDaoTest {

    private lateinit var database: PostDatabase
    private lateinit var postDao: PostDao

    @Before
    fun setup() {
        // Using the in-memory database for testing
        database = Room.inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            PostDatabase::class.java
        ).allowMainThreadQueries() // Allows Room to execute operations on the main thread during testing
            .build()

        postDao = database.postDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertAndGetAllPosts() {
        val post1 = Post(
            postId = "1",
            userId = "user1",
            userName = "John Doe",
            userProfileImageUrl = "http://example.com/john_doe.jpg",
            postImageUrl = "http://example.com/post1.jpg",
            postAddress = "123 Main St",
            postLatitude = 34.0522,
            postLongitude = -118.2437,
            postDescription = "This is a sample description for post 1",
            postDate = "2022-01-01 10:00:00",
            page = 1
        )

        val post2 = Post(
            postId = "2",
            userId = "user2",
            userName = "Jane Doe",
            userProfileImageUrl = "http://example.com/jane_doe.jpg",
            postImageUrl = "http://example.com/post2.jpg",
            postAddress = "456 Elm St",
            postLatitude = 40.7128,
            postLongitude = -74.0060,
            postDescription = "This is a sample description for post 2",
            postDate = "2022-01-02 11:00:00",
            page = 1
        )

        postDao.insertPosts(listOf(post1, post2))

        val retrievedPosts = postDao.getAllPostsList()

        // Checking if the data we inserted matches the data we retrieved
        assertThat(retrievedPosts, hasItem(post1))
        assertThat(retrievedPosts, hasItem(post2))
    }

    @Test
    fun testDeleteAllPosts() {
        val post1 = Post(
            postId = "1",
            userId = "user1",
            userName = "John Doe",
            userProfileImageUrl = "http://example.com/john_doe.jpg",
            postImageUrl = "http://example.com/post1.jpg",
            postAddress = "123 Main St",
            postLatitude = 34.0522,
            postLongitude = -118.2437,
            postDescription = "This is a sample description for post 1",
            postDate = "2022-01-01 10:00:00",
            page = 1
        )

        val post2 = Post(
            postId = "2",
            userId = "user2",
            userName = "Jane Doe",
            userProfileImageUrl = "http://example.com/jane_doe.jpg",
            postImageUrl = "http://example.com/post2.jpg",
            postAddress = "456 Elm St",
            postLatitude = 40.7128,
            postLongitude = -74.0060,
            postDescription = "This is a sample description for post 2",
            postDate = "2022-01-02 11:00:00",
            page = 1
        )

        postDao.insertPosts(listOf(post1, post2))

        postDao.deletePosts()
        val retrievedPosts = postDao.getAllPostsList()

        // After deleting, the list should be empty
        assertTrue(retrievedPosts.isEmpty())
    }

    @Test
    fun countPostsAfterInserting() = runBlocking {
        // Given
        val posts = listOf(
            Post(
                postId = "1",
                userId = "user1",
                userName = "Alice",
                userProfileImageUrl = "url1",
                postImageUrl = "postUrl1",
                postAddress = "Address1",
                postLatitude = 12.0,
                postLongitude = 14.0,
                postDescription = "Description1",
                postDate = "2023-09-01",
                page = 1
            ),
            Post(
                postId = "2",
                userId = "user2",
                userName = "Bob",
                userProfileImageUrl = "url2",
                postImageUrl = "postUrl2",
                postAddress = "Address2",
                postLatitude = 15.0,
                postLongitude = 16.0,
                postDescription = "Description2",
                postDate = "2023-09-02",
                page = 1
            )
        )

        postDao.insertPosts(posts)

        // When
        val count = postDao.countPosts()

        // Then
        assertEquals(2, count)
    }
}

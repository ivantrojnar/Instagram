package hr.itrojnar.instagram

import android.util.Log
import androidx.paging.ExperimentalPagingApi
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hr.itrojnar.instagram.api.FirebasePostRepository
import hr.itrojnar.instagram.db.PostDatabase
import hr.itrojnar.instagram.model.Post
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.tasks.await
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class FirebasePostRepositoryTest {

    @OptIn(ExperimentalPagingApi::class)
    private lateinit var repository: FirebasePostRepository
    private lateinit var postDatabase: PostDatabase

    private val testEmail = "test@test.com"
    private val testPassword = "Test123#*"

    @OptIn(ExperimentalPagingApi::class)
    @Before
    fun setup() {
        postDatabase = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            PostDatabase::class.java
        ).allowMainThreadQueries().build()

        repository = FirebasePostRepository(postDatabase)
    }

    @Test
    fun testLoginWithEmail() = runBlocking {
        // When
        val result = FirebaseAuth.getInstance().signInWithEmailAndPassword(testEmail, testPassword).await()

        // Then
        assertTrue(result.user?.email == testEmail)
    }

    @OptIn(ExperimentalPagingApi::class)
    @Test
    fun testAddPost() = runBlocking {
        // Given
        val post = Post(
            postId = "testPostId123",
            userId = "testUserId456",
            userName = "JohnDoe",
            userProfileImageUrl = "http://example.com/johndoe.jpg",
            postImageUrl = "http://example.com/testpost.jpg",
            postAddress = "123 Test St, Test City, TC",
            postLatitude = 40.7128,
            postLongitude = -74.0060,
            postDescription = "This is a test post description.",
            postDate = "2023-08-21 20:50:01",
            page = 1
        )

        // When
        val addResult = repository.addNewPost(post)

        // Then
        assertTrue(addResult.isSuccess) // Check if the addition was successful
    }

    @After
    fun tearDown() {
        // Clean up any test data from Firebase to avoid polluting the database
        postDatabase.close()

        // Delete the test post from Firebase Firestore
        runBlocking {
            try {
                val firestore = FirebaseFirestore.getInstance()
                val querySnapshot = firestore.collection("posts")
                    .whereEqualTo("postId", "testPostId123")
                    .get()
                    .await()

                for (document in querySnapshot.documents) {
                    document.reference.delete().await()
                }
            } catch (e: Exception) {
                // Handle exception, e.g., post not found
            }
        }
    }
}

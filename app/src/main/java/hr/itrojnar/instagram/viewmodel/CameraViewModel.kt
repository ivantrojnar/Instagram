package hr.itrojnar.instagram.viewmodel

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.itrojnar.instagram.api.PostRepository
import hr.itrojnar.instagram.model.Post
import hr.itrojnar.instagram.model.User
import hr.itrojnar.instagram.api.FirebaseUserRepository
import hr.itrojnar.instagram.api.UserRepository
import hr.itrojnar.instagram.util.addUserDailyConsumption
import hr.itrojnar.instagram.util.getImageSizeInMegabytes
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: PostRepository,
    private val firebaseAnalytics: FirebaseAnalytics
) : ViewModel() {

    private val storageReference = FirebaseStorage.getInstance().reference

    private var _userDetails = MutableLiveData<User?>()

    var isLoading = mutableStateOf(false)
    var isUploadedSuccessful = mutableStateOf(false)
    val userDetails: LiveData<User?> get() = _userDetails

    init {
        fetchUserDetails()
    }

    private fun fetchUserDetails() {
        viewModelScope.launch {
            _userDetails.value = userRepository.getCurrentUserDetail()
        }
    }

    // MutableState to track the imageUri
    var imageUri = mutableStateOf<Uri?>(null)

    // MutableState to track the location
    var location = mutableStateOf<Place?>(null)

    // MutableState to track the description
    var description = mutableStateOf("")

    // Check if all values are given
    val isReadyToPost: Boolean
        get() = imageUri.value != null && location.value != null && description.value.isNotBlank()

    fun createPost(context: Context) {
        if (isReadyToPost) {

            isLoading.value = true

            val postId = System.currentTimeMillis().toString()

            val imageRef = storageReference.child("posts/$postId.jpg")
            val uploadTask = imageUri.value?.let { imageRef.putFile(it) }

            uploadTask?.continueWithTask { task ->
                if (!task.isSuccessful) {
                    task.exception?.let {
                        throw it
                    }
                }
                imageRef.downloadUrl
            }?.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val postImageUrl = task.result.toString()
                    val postDescription = description.value
                    val postAddress = location.value?.address ?: ""
                    val postLatitude = location.value?.latLng?.latitude ?: 0.0
                    val postLongitude = location.value?.latLng?.longitude ?: 0.0

                    val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")

                    val post = Post(
                        postId,
                        userDetails.value!!.firebaseUserId,
                        userDetails.value!!.fullName,
                        userDetails.value!!.profilePictureUrl!!,
                        postImageUrl,
                        postAddress,
                        postLatitude,
                        postLongitude,
                        postDescription,
                        dateFormat.format(Date()),
                        1
                    )

                    val imageSizeInMb = getImageSizeInMegabytes(context, imageUri.value!!)
                    CoroutineScope(Dispatchers.IO).launch {
                        userRepository.updateUserConsumption(imageSizeInMb, 1)
                        addUserDailyConsumption(context, imageSizeInMb, 1)
                    }

                    val bundle = Bundle()
                    bundle.putString("user_id", userDetails.value!!.firebaseUserId)
                    bundle.putString("created_post_id", postId)
                    Firebase.analytics.setUserId(userDetails.value!!.firebaseUserId)
                    Firebase.analytics.logEvent("post_created", bundle)

                    CoroutineScope(Dispatchers.IO).launch {
                        val result = postRepository.addNewPost(post)
                    }
                    isLoading.value = false
                    isUploadedSuccessful.value = true
                } else {
                    isLoading.value = false
                    Log.e("ERROR", "Error while creating new post")
                }
            }
        }
    }

    fun setImageUri(uri: Uri?) {
        imageUri.value = uri
        Log.d("CameraViewModel", "Image URI: $uri")
    }

    fun setLocation(place: Place?) {
        Log.d("CameraViewModel", "Location: ${place?.name}")
        location.value = place
    }

    fun setDescription(desc: String) {
        Log.d("CameraViewModel", "Description: $desc")
        description.value = desc
    }
}
package hr.itrojnar.instagram.viewmodel

import android.net.Uri
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.libraries.places.api.model.Place
import com.google.firebase.storage.FirebaseStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import hr.itrojnar.instagram.api.FirebasePostRepository
import hr.itrojnar.instagram.model.NewPost
import hr.itrojnar.instagram.model.User
import hr.itrojnar.instagram.repository.UserRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val postRepository: FirebasePostRepository
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

    fun createPost() {
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

                    val newPost = NewPost(
                        postId,
                        userDetails.value!!.firebaseUserId,
                        userDetails.value!!.fullName,
                        userDetails.value!!.profilePictureUrl!!,
                        postImageUrl,
                        postAddress,
                        postLatitude,
                        postLongitude,
                        postDescription,
                        dateFormat.format(Date())
                    )

                    CoroutineScope(Dispatchers.IO).launch {
                        val result = postRepository.addNewPost(newPost)
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
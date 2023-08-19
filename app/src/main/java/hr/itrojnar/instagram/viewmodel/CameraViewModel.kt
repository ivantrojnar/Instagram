package hr.itrojnar.instagram.viewmodel

import android.graphics.Bitmap
import android.net.Uri
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.libraries.places.api.model.Place
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

class CameraViewModel : ViewModel() {

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
        // TODO: Trigger the upload action here.
        // You might want to call a use case or a repository function here that
        // handles the upload of the image, location, and description to your server or database.
        if (isReadyToPost) {
            // All the fields are filled, so perform the post creation.
            // For example, you can use the following properties:
            // imageUri.value, location.value, and description.value to create the post.
        }
    }

    fun setImageUri(uri: Uri?) {
        imageUri.value = uri
    }

    fun setLocation(place: Place?) {
        location.value = place
    }

    fun setDescription(desc: String) {
        description.value = desc
    }
}
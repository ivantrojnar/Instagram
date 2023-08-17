package hr.itrojnar.instagram.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

class CameraViewModel : ViewModel() {
    var selectedImage: Bitmap? by mutableStateOf(null)
        private set

    fun onImageSelected(newImage: Bitmap) {
        selectedImage = newImage
    }
}
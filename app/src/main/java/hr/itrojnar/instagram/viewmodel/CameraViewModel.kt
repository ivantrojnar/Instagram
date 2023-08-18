package hr.itrojnar.instagram.viewmodel

import android.graphics.Bitmap
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CameraViewModel @Inject constructor() : ViewModel() {
    var selectedImage: Bitmap? by mutableStateOf(null)
        private set

    fun onImageSelected(newImage: Bitmap) {
        selectedImage = newImage
    }
}
package hr.itrojnar.instagram.model

import android.graphics.Bitmap
import com.bumptech.glide.load.Transformation

data class ImageFilter(val name: String, val transformation: Transformation<Bitmap>)


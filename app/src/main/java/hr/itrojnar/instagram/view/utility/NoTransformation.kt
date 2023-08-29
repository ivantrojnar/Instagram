package hr.itrojnar.instagram.view.utility

import android.graphics.Bitmap
import coil.size.Size
import coil.transform.Transformation

class NoTransformation : Transformation {
    override val cacheKey: String
        get() = "NoTransformation"

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        return input
    }
}
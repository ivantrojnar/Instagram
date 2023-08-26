package hr.itrojnar.instagram.view.utility

import android.content.Context
import android.graphics.Bitmap
import com.bumptech.glide.load.Transformation
import com.bumptech.glide.load.engine.Resource
import java.security.MessageDigest

class NoTransformation : Transformation<Bitmap> {

    override fun transform(
        context: Context,
        resource: Resource<Bitmap>,
        outWidth: Int,
        outHeight: Int
    ): Resource<Bitmap> {
        return resource
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        TODO("Not yet implemented")
    }
}
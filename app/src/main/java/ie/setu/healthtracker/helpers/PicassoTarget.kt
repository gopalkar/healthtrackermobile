package ie.setu.healthtracker.helpers

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.core.graphics.drawable.toDrawable
import com.squareup.picasso.Picasso

class PicassoTarget(private val bitmapPainter: MutableState<ImageBitmap?>) : com.squareup.picasso.Target {
    var painter: Painter? by mutableStateOf(null)
    override fun onBitmapLoaded(bitmap: Bitmap?, from: Picasso.LoadedFrom?) {
        bitmap?.let {
            bitmapPainter.value = it.asImageBitmap()
        }
    }

    override fun onBitmapFailed(e: Exception?, errorDrawable: Drawable?) {
        // Handle the failure to load the bitmap, such as showing an error message
    }

    override fun onPrepareLoad(placeHolderDrawable: Drawable?) {
        // Optionally handle the preparation of loading the bitmap
    }
}
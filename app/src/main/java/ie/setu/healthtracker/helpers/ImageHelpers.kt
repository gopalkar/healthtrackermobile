package ie.setu.placemark.helpers

import android.content.Context
import android.content.Intent
import androidx.activity.result.ActivityResultLauncher
import ie.setu.healthtracker.R

fun showImagePicker(intentLauncher : ActivityResultLauncher<Intent>, context: Context) {
    var imagePickerTargetIntent = Intent()

    imagePickerTargetIntent.action = Intent.ACTION_OPEN_DOCUMENT
    imagePickerTargetIntent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)
    imagePickerTargetIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
    imagePickerTargetIntent.type = "image/*"
    imagePickerTargetIntent = Intent.createChooser(imagePickerTargetIntent,
        context.getString(R.string.app_name))
    intentLauncher.launch(imagePickerTargetIntent)
}
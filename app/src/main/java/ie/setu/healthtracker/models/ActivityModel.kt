package ie.setu.healthtracker.models

import android.net.Uri
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

var image: Uri = Uri.EMPTY
@Parcelize
data class ActivityModel(
    var id: Long = 0,
    var activityTime: String = "",
    var duration: String = "",
    var calories: String = "",
    var image: Uri = Uri.EMPTY,
    var lat : Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f
): Parcelable

@Parcelize
data class Location(var lat: Double = 0.0,
                    var lng: Double = 0.0,
                    var zoom: Float = 0f) : Parcelable
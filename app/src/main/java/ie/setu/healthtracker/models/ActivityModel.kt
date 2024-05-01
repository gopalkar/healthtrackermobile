package ie.setu.healthtracker.models

import android.net.Uri
import android.os.Parcelable
import com.google.firebase.database.Exclude
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize

@IgnoreExtraProperties
@Parcelize
data class ActivityModel(
    var uid: String? = "",
    var activityName: String? = "",
    var activityTime: String? = "",
    var duration: String? = "",
    var calories: String? = "",
    var image: String? = "",
    var lat : Double = 0.0,
    var lng: Double = 0.0,
    var zoom: Float = 0f)
    : Parcelable
{
    @Exclude
    fun toMap(): Map<String, Any?> {
        return mapOf(
            "uid" to uid,
            "activityName" to activityName,
            "activityTime" to activityTime,
            "duration" to duration,
            "calories" to calories,
            "image" to image,
            "lat" to lat,
            "lng" to lng,
            "zoom" to zoom
        )
    }

}

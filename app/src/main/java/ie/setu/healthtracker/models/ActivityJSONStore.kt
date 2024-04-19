package ie.setu.healthtracker.models

import android.content.Context
import android.net.Uri
import com.google.gson.*
import com.google.gson.reflect.TypeToken
import ie.setu.placemark.helpers.exists
import ie.setu.placemark.helpers.read
import ie.setu.placemark.helpers.write
import timber.log.Timber
import java.lang.reflect.Type
import java.util.*

const val JSON_FILE = "activity.json"
val gsonBuilder: Gson = GsonBuilder().setPrettyPrinting()
    .registerTypeAdapter(Uri::class.java, UriParser())
    .create()
val listType: Type = object : TypeToken<ArrayList<ActivityModel>>() {}.type

fun generateRandomId(): Long {
    return Random().nextLong()
}

class ActivityJsonStore(private val context: Context) : ActivityStore {

    var activities = mutableListOf<ActivityModel>()

    init {
        if (exists(context, JSON_FILE)) {
            deserialize()
        }
    }

    override fun findAll(): MutableList<ActivityModel> {
        logAll()
        return activities
    }

    override fun create(activity: ActivityModel) {
        activity.id = generateRandomId()
        activities.add(activity)
        serialize()
    }


    override fun update(activity: ActivityModel) {
        activities.forEach{
            if (it.id == activity.id) {
                it.activityTime = activity.activityTime
                it.duration = activity.duration
                it.calories = activity.calories
                it.image = activity.image
                it.lat = activity.lat
                it.lng = activity.lng
                it.zoom = activity.zoom
                logAll()
                serialize()
            }
        }
    }

    override fun delete(activity: ActivityModel) {
        activities.remove(activity)
        serialize()
    }

    private fun serialize() {
        val jsonString = gsonBuilder.toJson(activities, listType)
        write(context, JSON_FILE, jsonString)
    }

    private fun deserialize() {
        val jsonString = read(context, JSON_FILE)
        activities = gsonBuilder.fromJson(jsonString, listType)
    }

    private fun logAll() {
        activities.forEach { Timber.i("$it") }
    }

    override fun findById(id:Long) : ActivityModel? {
        val foundActivity: ActivityModel? = activities.find { it.id == id }
        return foundActivity
    }
}

class UriParser : JsonDeserializer<Uri>,JsonSerializer<Uri> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): Uri {
        return Uri.parse(json?.asString)
    }

    override fun serialize(
        src: Uri?,
        typeOfSrc: Type?,
        context: JsonSerializationContext?
    ): JsonElement {
        return JsonPrimitive(src.toString())
    }
}
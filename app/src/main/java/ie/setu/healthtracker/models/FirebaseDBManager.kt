package ie.setu.healthtracker.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.DatabaseException
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import timber.log.Timber

var database: DatabaseReference = FirebaseDatabase.getInstance().reference
object FirebaseDBManager : ActivityStore {
    override fun findAll(activityList: MutableLiveData<List<ActivityModel>>) {
        TODO("Not yet implemented")
    }

    override fun findAll(userid: String, activityList: MutableLiveData<List<ActivityModel>>) {
        TODO("Not yet implemented")
    }

    override fun findById(userid: String, activityId: String, activity: MutableLiveData<List<ActivityModel>>) {
        TODO("Not yet implemented")
    }

    override fun create(firebaseUser: FirebaseUser?, activity: ActivityModel) {
        Timber.i("Firebase DB Reference : $database")
        Timber.i("Firebase User at DB Manager : ${firebaseUser!!.uid}")
        val uid = firebaseUser!!.uid
        //val uid =  "njWhV6cUrqYZhwcwiARELvI3Nmn2"
        val key = database.child("activities").push().key
        if (key == null) {
            Timber.i("Firebase Error : Key Empty")
            return
        }
        activity.uid = key
        val activityValues = activity.toMap()

        val childAdd = HashMap<String, Any>()
        Timber.i("activityValues: $activityValues")
        Timber.i("childAdd: $childAdd")
        try {
            childAdd["/activities/$key"] = activityValues
            childAdd["/user-activities/$uid/$key"] = activityValues

            database.updateChildren(childAdd)
        } catch (e: DatabaseException) {
            // Handle Firebase Database exception
            println("Firebase Database Exception: ${e.message}")
        }
    }

    override fun delete(userid: String, activityId: String) {
        TODO("Not yet implemented")
    }

    override fun update(userid: String, activityId: String, activity: ActivityModel) {
        TODO("Not yet implemented")
    }
}
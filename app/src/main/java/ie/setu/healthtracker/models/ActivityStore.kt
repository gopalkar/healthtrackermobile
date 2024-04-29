package ie.setu.healthtracker.models

import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseUser

interface ActivityStore {
    fun findAll(activityList: MutableLiveData<List<ActivityModel>>)

    fun findAll(userid:String, activityList: MutableLiveData<List<ActivityModel>>)

    fun findById(userid:String, activityId: String, activity: MutableLiveData<List<ActivityModel>>)

    fun create(firebaseUser: FirebaseUser?, activity: ActivityModel)
    fun update(userid:String, activityId: String, activity: ActivityModel)
    fun delete(userid:String, activityId: String)

}
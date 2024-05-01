package ie.setu.healthtracker.views.activitylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ie.setu.healthtracker.models.ActivityModel
import ie.setu.healthtracker.models.FirebaseDBManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber
import java.lang.Exception

class ActivityListViewModel: ViewModel() {

    var activitiesList =
        MutableLiveData<List<ActivityModel>>()

    private var firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()

    var liveFirebaseUser = firebaseAuth!!.currentUser

    //init { getAllActivities() }

    fun refreshData() {
        getAllActivities()
        Timber.i("Refresh : ${activitiesList.value}")
    }
    fun getAllActivities() {
        try {
            //DonationManager.findAll(liveFirebaseUser.value?.email!!, donationsList)
                FirebaseDBManager.findAll(liveFirebaseUser!!.uid, activitiesList)
        } catch (e: Exception) {
            Timber.i("Activity Error : $e.message")
        }
    }

    fun delete(userid: String, id: String) {
        try {
            //DonationManager.delete(userid,id)
            FirebaseDBManager.delete(userid,id)
            Timber.i("Activity Deleted Successfully")
        }
        catch (e: Exception) {
            Timber.i("Activity Delete Error : $e.message")
        }
    }

}
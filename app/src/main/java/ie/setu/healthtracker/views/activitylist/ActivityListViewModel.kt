package ie.setu.healthtracker.views.activitylist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import ie.setu.healthtracker.models.ActivityModel
import ie.setu.healthtracker.models.FirebaseDBManager
import timber.log.Timber
import java.lang.Exception

class ActivityListViewModel: ViewModel() {

    var activitiesList =
        MutableLiveData<List<ActivityModel>>()
    private var firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    var liveFirebaseUser = firebaseAuth!!.currentUser

    init { getAllActivities() }

    fun refreshData() {
        getAllActivities()
    }
    fun getAllActivities() {
        try {
            //DonationManager.findAll(liveFirebaseUser.value?.email!!, donationsList)
            FirebaseDBManager.findAll(liveFirebaseUser!!.uid, activitiesList)
            Timber.i("Report Load Success : ${activitiesList.value.toString()}")
        } catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }
}
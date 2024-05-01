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

    init { getAllActivities() }

    fun refreshData() {
        getAllActivities()
        Timber.i("Refresh : ${activitiesList.value}")
    }
    fun getAllActivities() {
        try {
            //DonationManager.findAll(liveFirebaseUser.value?.email!!, donationsList)
            CoroutineScope(Dispatchers.IO).launch {
                FirebaseDBManager.findAll(liveFirebaseUser!!.uid, activitiesList)
            }
            Timber.i("Init : ${activitiesList.value}")
        } catch (e: Exception) {
            Timber.i("Report Load Error : $e.message")
        }
    }

}
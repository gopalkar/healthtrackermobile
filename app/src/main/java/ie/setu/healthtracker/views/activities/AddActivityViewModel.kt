package ie.setu.healthtracker.views.activities

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseUser
import ie.setu.healthtracker.models.ActivityModel
import ie.setu.healthtracker.models.FirebaseDBManager

class AddActivityViewModel : ViewModel() {

    private val status = MutableLiveData<Boolean>()

    val observableStatus: LiveData<Boolean>
        get() = status
    fun addUserActivity(
        firebaseUser: FirebaseUser?,
        userActivity: ActivityModel
    ) {
        status.value = try {
            //DonationManager.create(donation)
            FirebaseDBManager.create(firebaseUser,userActivity)
            true
        } catch (e: IllegalArgumentException) {
            false
        }
    }
}
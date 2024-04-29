package ie.setu.healthtracker.helpers

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import timber.log.Timber

class FirebaseHelper : ViewModel() {

    private var firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    var currentUser = MutableLiveData<FirebaseUser>()
    var errorStatus = MutableLiveData<Boolean>()
    var loggedOut = MutableLiveData<Boolean>()

    init {

        FirebaseAuth.getInstance().addAuthStateListener { auth ->
            currentUser.value = auth.currentUser
        }
    }
        fun performSignup(
            email: String,
            password: String,
            onSuccess : () -> Unit,
            onError : (String) -> Unit
        ) {
            Timber.d("createAccount:$email")
            // [START create_user_with_email]
            firebaseAuth!!.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.i("createUserWithEmail:success")
                        currentUser.value = firebaseAuth!!.currentUser
                        errorStatus.postValue(false)
                        onSuccess()
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.i("Authentication failed. Please check your credentials.")
                        onError("Authentication failed. Please check your credentials.")
                        errorStatus.postValue(true)
                    }
                }
        }

        fun performLogin(
            email: String,
            password: String,
            onSuccess : () -> Unit,
            onError : (String) -> Unit
        ) {
            Timber.d("signIn:$email")

            // [START sign_in_with_email]
            firebaseAuth!!.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Timber.i("createUserWithEmail:success")
                        currentUser.value = firebaseAuth!!.currentUser
                        errorStatus.postValue(false)
                        onSuccess()
                    } else {
                        // If sign in fails, display a message to the user.
                        Timber.i("Authentication failed. Please check your credentials.")
                        onError("Authentication failed. Please check your credentials.")
                        errorStatus.postValue(true)
                    }
                }
        }

        fun logOut() {
            firebaseAuth!!.signOut()
            loggedOut.postValue(true)
            errorStatus.postValue(false)
        }

        fun checkIfEmailRegistered(email: String): Boolean {
            Timber.i("signIn:$email")
            val auth = FirebaseAuth.getInstance()
            val result = auth.fetchSignInMethodsForEmail(email)
            return result.isSuccessful && result.result?.signInMethods?.isNotEmpty() == true
        }

        /*fun signOut() {
    auth.signOut()
    updateUI(null)
}*/

        /*fun updateUI(user: FirebaseUser?) {
    hideLoader(loader)
    if (user != null) {
        loginBinding.status.text = getString(
            R.string.emailpassword_status_fmt,
            user.email, user.isEmailVerified)
        loginBinding.detail.text = getString(R.string.firebase_status_fmt, user.uid)

        loginBinding.emailPasswordButtons.visibility = View.GONE
        loginBinding.emailPasswordFields.visibility = View.GONE
        loginBinding.signedInButtons.visibility = View.VISIBLE

        loginBinding.verifyEmailButton.isEnabled = !user.isEmailVerified
    } else {
        loginBinding.status.setText(R.string.signed_out)
        loginBinding.detail.text = null

        loginBinding.emailPasswordButtons.visibility = View.VISIBLE
        loginBinding.emailPasswordFields.visibility = View.VISIBLE
        loginBinding.signedInButtons.visibility = View.GONE
    }
}*/
}
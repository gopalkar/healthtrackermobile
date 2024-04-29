package ie.setu.healthtracker.views.login

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Surface
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.firebase.auth.FirebaseAuth
import ie.setu.healthtracker.ui.theme.HealthTrackerTheme
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.firebase.auth.FirebaseUser
import ie.setu.healthtracker.R
import ie.setu.healthtracker.helpers.FirebaseHelper
import ie.setu.healthtracker.views.activitylist.ActivityListView


class Login : ComponentActivity() {

    // [START declare_auth]
    var liveFirebaseUser = MutableLiveData<FirebaseUser>()
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val navController = rememberNavController()
            val firebaseHelper: FirebaseHelper = viewModel()
            HealthTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    //modifier = Modifier.fillMaxSize(),
                    color = Color(red = 0.631f, green = 0.765f, blue = 0.525f),

                    ) {
                    Column(modifier = Modifier.fillMaxWidth())
                    {
                        NavHost(navController, startDestination = "loginScreen") {
                            composable("loginScreen") {
                                LoginScreen(firebaseHelper = firebaseHelper, onSuccess = {
                                    navActivityListView(firebaseHelper)
                                })
                            }
                        }
                    }
                    }
            }
        }
                        //navController.navigate(R.id.action_loginScreen_to_activityListView)
    }

    private fun navActivityListView(firebaseHelper: FirebaseHelper) {
        val intent = Intent(this, ActivityListView::class.java)
        //intent.putExtra("liveFirebaseUser", firebaseHelper.liveFirebaseUser.value)
        startActivity(intent)
        finish()
    }
}



package ie.setu.healthtracker.views.navigation

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.relay.compose.RelayContainer
import ie.setu.healthtracker.R
import ie.setu.healthtracker.healthtrackernavigationnew.HealthTrackerNavigationNew
import ie.setu.healthtracker.helpers.FirebaseHelper
import ie.setu.healthtracker.ui.theme.HealthTrackerTheme
import ie.setu.healthtracker.views.activitylist.ActivityListView
import ie.setu.healthtracker.views.login.GoogleSignInWrapper
import ie.setu.healthtracker.views.login.Login
import ie.setu.healthtracker.views.login.LoginScreen
import ie.setu.healthtracker.views.login.LoginViewModel
import timber.log.Timber

class NavDrawerActivity : ComponentActivity() {

    private var firebaseAuth: FirebaseAuth? = FirebaseAuth.getInstance()
    private val viewModel: LoginViewModel by viewModels()
    var loginUserName : String? = null
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.plant(Timber.DebugTree())

        viewModel.loginThrough.observe(this, Observer { newData ->
            Timber.i("Login User newData: ${viewModel.loginThrough.value}")
            loginUserName = viewModel.loginThrough.value
        })

        var displayEmail : String? = ""
        Timber.i("Login User: $loginUserName")
        if (loginUserName != null) {
            displayEmail = loginUserName
        } else {
            displayEmail = firebaseAuth!!.currentUser!!.email
        }

        setContent {
            val firebaseHelper: FirebaseHelper = viewModel()
            val context = LocalContext.current
            val googleSignInClient = ie.setu.healthtracker.views.login.GoogleSignInWrapper.getGoogleSignInClient(context)



            HealthTrackerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    //modifier = Modifier.fillMaxSize(),
                    color = Color(red = 0.631f, green = 0.765f, blue = 0.525f),

                    ) {
                    MaterialTheme {
                        RelayContainer {
                            HealthTrackerNavigationNew(
                                userNameTextContent = "$displayEmail",
                                onNavHomeTapped = {
                                    val intent = Intent(context, ActivityListView::class.java)
                                    startActivity(intent)
                                    finish()},
                                onNavActivityTapped = {
                                    val intent = Intent(context, ActivityListView::class.java)
                                    startActivity(intent)
                                    finish()},
                                onNavNutritionTapped = {},
                                onNavLogoutTapped = {
                                    firebaseHelper.loggedOut
                                    googleSignInClient.signOut()
                                    val intent = Intent(context, Login::class.java)
                                    startActivity(intent)
                                    finish()
                                                    },
                                navImage = painterResource(R.drawable.image_logo),
                                modifier = Modifier
                                    .rowWeight(1.0f)
                                    .columnWeight(1.0f)
                            )
                        }
                    }
                    }
                }
            }
        }

}